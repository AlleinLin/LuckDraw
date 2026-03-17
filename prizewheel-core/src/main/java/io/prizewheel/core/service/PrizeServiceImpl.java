package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Prize;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.PrizeServicePort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.port.output.PrizeRepositoryPort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 奖品服务实现
 * 
 * @author Allein
 * @since 1.0.0
 */
public class PrizeServiceImpl implements PrizeServicePort {

    private static final Logger log = LoggerFactory.getLogger(PrizeServiceImpl.class);

    private final PrizeRepositoryPort prizeRepository;
    private final WinRecordRepositoryPort winRecordRepository;
    private final IdGeneratorPort idGenerator;
    private final PrizeGrantFactory grantFactory;

    public PrizeServiceImpl(PrizeRepositoryPort prizeRepository,
                            WinRecordRepositoryPort winRecordRepository,
                            IdGeneratorPort idGenerator) {
        this.prizeRepository = prizeRepository;
        this.winRecordRepository = winRecordRepository;
        this.idGenerator = idGenerator;
        this.grantFactory = new PrizeGrantFactory();
    }

    @Override
    public String createPrize(Prize prize) {
        log.info("创建奖品 name:{}", prize.getPrizeName());
        String prizeId = String.valueOf(idGenerator.nextId());
        prize.setPrizeId(prizeId);
        prizeRepository.save(prize);
        log.info("奖品创建成功 prizeId:{}", prizeId);
        return prizeId;
    }

    @Override
    public Prize queryPrize(String prizeId) {
        log.info("查询奖品 prizeId:{}", prizeId);
        return prizeRepository.findById(prizeId);
    }

    @Override
    public List<Prize> queryPrizesByCampaign(Long campaignId) {
        log.info("查询活动奖品 campaignId:{}", campaignId);
        return prizeRepository.findByCampaignId(campaignId);
    }

    @Override
    public boolean grantPrize(Long recordId) {
        log.info("发放奖品 recordId:{}", recordId);
        
        WinRecord record = winRecordRepository.findById(recordId);
        if (record == null) {
            log.warn("中奖记录不存在 recordId:{}", recordId);
            return false;
        }

        if (record.isGranted()) {
            log.warn("奖品已发放 recordId:{}", recordId);
            return false;
        }

        boolean granted = grantFactory.grant(record);
        if (granted) {
            record.markAsGranted();
            winRecordRepository.updateStatus(recordId, record.getStatus());
            log.info("奖品发放成功 recordId:{}", recordId);
        } else {
            log.warn("奖品发放失败 recordId:{}", recordId);
        }
        
        return granted;
    }
}
