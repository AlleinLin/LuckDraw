package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.DrawPolicy;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.DrawServicePort;
import io.prizewheel.core.port.output.CacheServicePort;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import io.prizewheel.core.port.output.DrawPolicyRepositoryPort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.port.output.MessageQueuePort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 抽奖服务实现
 * 
 * 核心流程：
 * 1. 获取分布式锁防止并发
 * 2. 校验活动状态和用户参与次数
 * 3. 执行概率抽奖算法
 * 4. 扣减奖品库存（乐观锁）
 * 5. 扣减活动库存
 * 6. 保存中奖记录
 * 7. 发送发奖消息
 * 
 * @author Allein
 * @since 1.0.0
 */
public class DrawServiceImpl implements DrawServicePort {

    private static final Logger log = LoggerFactory.getLogger(DrawServiceImpl.class);
    
    private static final long LOCK_WAIT_SECONDS = 5;
    private static final long LOCK_LEASE_SECONDS = 30;

    private final CampaignRepositoryPort campaignRepository;
    private final DrawPolicyRepositoryPort policyRepository;
    private final WinRecordRepositoryPort winRecordRepository;
    private final IdGeneratorPort idGenerator;
    private final CacheServicePort cacheService;
    private final MessageQueuePort messageQueue;

    public DrawServiceImpl(CampaignRepositoryPort campaignRepository,
                          DrawPolicyRepositoryPort policyRepository,
                          WinRecordRepositoryPort winRecordRepository,
                          IdGeneratorPort idGenerator,
                          CacheServicePort cacheService,
                          MessageQueuePort messageQueue) {
        this.campaignRepository = campaignRepository;
        this.policyRepository = policyRepository;
        this.winRecordRepository = winRecordRepository;
        this.idGenerator = idGenerator;
        this.cacheService = cacheService;
        this.messageQueue = messageQueue;
    }

    @Override
    public WinRecord executeDraw(String participantId, Long campaignId) {
        log.info("执行抽奖 participantId:{} campaignId:{}", participantId, campaignId);

        String lockKey = "draw:" + campaignId + ":" + participantId;
        if (!cacheService.tryLock(lockKey, LOCK_WAIT_SECONDS, LOCK_LEASE_SECONDS)) {
            log.warn("获取抽奖锁失败 participantId:{} campaignId:{}", participantId, campaignId);
            return null;
        }

        try {
            Campaign campaign = campaignRepository.findById(campaignId);
            if (campaign == null || !campaign.isAvailable()) {
                log.warn("活动不可用 campaignId:{}", campaignId);
                return null;
            }

            int participatedCount = winRecordRepository.countByParticipantAndCampaign(participantId, campaignId);
            if (!campaign.canParticipate(participantId, participatedCount)) {
                log.warn("用户参与次数已达上限 participantId:{} campaignId:{} participatedCount:{}", 
                        participantId, campaignId, participatedCount);
                return null;
            }

            DrawPolicy policy = policyRepository.findByCampaignId(campaignId);
            if (policy == null) {
                log.warn("抽奖策略不存在 campaignId:{}", campaignId);
                return null;
            }

            String prizeId = selectPrize(policy);
            if (prizeId == null) {
                log.info("未中奖 participantId:{} campaignId:{}", participantId, campaignId);
                return createEmptyRecord(participantId, campaignId, policy.getPolicyId());
            }

            DrawPolicy.PrizeConfig prizeConfig = policy.getPrizeConfig(prizeId);
            if (prizeConfig == null) {
                log.warn("奖品配置不存在 prizeId:{}", prizeId);
                return createEmptyRecord(participantId, campaignId, policy.getPolicyId());
            }

            if (!policyRepository.checkPrizeAvailable(policy.getPolicyId(), prizeId)) {
                log.info("奖品库存不足 prizeId:{}", prizeId);
                return createEmptyRecord(participantId, campaignId, policy.getPolicyId());
            }

            int decreaseResult = policyRepository.decreasePrizeQuantity(policy.getPolicyId(), prizeId);
            if (decreaseResult <= 0) {
                log.info("奖品库存扣减失败（并发冲突） prizeId:{}", prizeId);
                return createEmptyRecord(participantId, campaignId, policy.getPolicyId());
            }

            WinRecord record = new WinRecord();
            record.setRecordId(idGenerator.nextId());
            record.setParticipantId(participantId);
            record.setCampaignId(campaignId);
            record.setPolicyId(policy.getPolicyId());
            record.setPrizeId(prizeId);
            record.setPrizeName(prizeConfig.getPrizeName());
            record.setPrizeType(getPrizeType(prizeId));
            record.setPrizeContent(getPrizeContent(prizeId));
            record.setStatus(1);
            record.setWinTime(LocalDateTime.now());

            winRecordRepository.save(record);
            campaignRepository.decreaseStock(campaignId);

            messageQueue.send("prize-grant-topic", record);

            log.info("抽奖成功 participantId:{} prizeId:{} prizeName:{}", 
                    participantId, prizeId, prizeConfig.getPrizeName());
            return record;

        } finally {
            cacheService.unlock(lockKey);
        }
    }

    @Override
    public WinRecord queryWinRecord(Long recordId) {
        return winRecordRepository.findById(recordId);
    }

    private String selectPrize(DrawPolicy policy) {
        List<DrawPolicy.PrizeConfig> configs = policy.getPrizeConfigs();
        if (configs == null || configs.isEmpty()) {
            return null;
        }

        double random = ThreadLocalRandom.current().nextDouble() * 100;
        BigDecimal cumulative = BigDecimal.ZERO;

        for (DrawPolicy.PrizeConfig config : configs) {
            if (config.getRemainingQuantity() == null || config.getRemainingQuantity() <= 0) {
                continue;
            }
            cumulative = cumulative.add(config.getWinRate());
            if (BigDecimal.valueOf(random).compareTo(cumulative) < 0) {
                return config.getPrizeId();
            }
        }

        return null;
    }

    private WinRecord createEmptyRecord(String participantId, Long campaignId, Long policyId) {
        WinRecord record = new WinRecord();
        record.setRecordId(idGenerator.nextId());
        record.setParticipantId(participantId);
        record.setCampaignId(campaignId);
        record.setPolicyId(policyId);
        record.setStatus(0);
        record.setWinTime(LocalDateTime.now());
        winRecordRepository.save(record);
        return record;
    }

    private Integer getPrizeType(String prizeId) {
        return null;
    }

    private String getPrizeContent(String prizeId) {
        return null;
    }
}
