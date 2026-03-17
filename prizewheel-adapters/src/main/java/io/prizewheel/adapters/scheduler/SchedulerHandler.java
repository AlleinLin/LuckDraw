package io.prizewheel.adapters.scheduler;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import io.prizewheel.core.port.output.MessageQueuePort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务处理器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class SchedulerHandler {

    private static final Logger log = LoggerFactory.getLogger(SchedulerHandler.class);

    private final CampaignRepositoryPort campaignRepository;
    private final WinRecordRepositoryPort winRecordRepository;
    private final MessageQueuePort messageQueue;

    public SchedulerHandler(CampaignRepositoryPort campaignRepository,
                            WinRecordRepositoryPort winRecordRepository,
                            MessageQueuePort messageQueue) {
        this.campaignRepository = campaignRepository;
        this.winRecordRepository = winRecordRepository;
        this.messageQueue = messageQueue;
    }

    public void processCampaignStateTransition() {
        log.info("开始执行活动状态流转任务");

        List<Campaign> campaigns = campaignRepository.findActiveCampaigns();
        LocalDateTime now = LocalDateTime.now();

        for (Campaign campaign : campaigns) {
            try {
                if (campaign.getStatus() == 4 && now.isAfter(campaign.getStartTime())) {
                    campaignRepository.updateStatus(campaign.getCampaignId(), 5);
                    log.info("活动状态更新: PASS -> DOING campaignId:{}", campaign.getCampaignId());
                }

                if (campaign.getStatus() == 5 && now.isAfter(campaign.getEndTime())) {
                    campaignRepository.updateStatus(campaign.getCampaignId(), 7);
                    log.info("活动状态更新: DOING -> CLOSED campaignId:{}", campaign.getCampaignId());
                }

                if (campaign.getStatus() == 5 && campaign.getRemainingStock() <= 0) {
                    campaignRepository.updateStatus(campaign.getCampaignId(), 7);
                    log.info("活动库存耗尽，自动关闭 campaignId:{}", campaign.getCampaignId());
                }

            } catch (Exception e) {
                log.error("活动状态流转异常 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("活动状态流转任务执行完成");
    }

    public void processMessageCompensation() {
        log.info("开始执行消息补偿任务");

        List<WinRecord> failedRecords = winRecordRepository.findPendingRecords();

        for (WinRecord record : failedRecords) {
            try {
                if (record.getStatus() == 1) {
                    messageQueue.send("prize-grant-topic", record);
                    log.info("补偿发送发奖消息 recordId:{}", record.getRecordId());
                }
            } catch (Exception e) {
                log.error("消息补偿发送失败 recordId:{}", record.getRecordId(), e);
            }
        }

        log.info("消息补偿任务执行完成 处理数量:{}", failedRecords.size());
    }

    public void processExpiredCampaigns() {
        log.info("开始执行过期活动清理任务");

        List<Campaign> expiredCampaigns = campaignRepository.findExpiredCampaigns(LocalDateTime.now().minusDays(30));

        for (Campaign campaign : expiredCampaigns) {
            try {
                campaignRepository.updateStatus(campaign.getCampaignId(), 8);
                log.info("过期活动归档 campaignId:{}", campaign.getCampaignId());
            } catch (Exception e) {
                log.error("过期活动归档失败 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("过期活动清理任务执行完成 处理数量:{}", expiredCampaigns.size());
    }

    public void processStockSync() {
        log.info("开始执行库存同步任务");

        List<Campaign> campaigns = campaignRepository.findActiveCampaigns();

        for (Campaign campaign : campaigns) {
            try {
                int actualStock = campaignRepository.calculateRemainingStock(campaign.getCampaignId());
                if (actualStock != campaign.getRemainingStock()) {
                    campaignRepository.updateStock(campaign.getCampaignId(), actualStock);
                    log.info("库存同步 campaignId:{} dbStock:{} cacheStock:{}", 
                            campaign.getCampaignId(), actualStock, campaign.getRemainingStock());
                }
            } catch (Exception e) {
                log.error("库存同步失败 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("库存同步任务执行完成");
    }
}
