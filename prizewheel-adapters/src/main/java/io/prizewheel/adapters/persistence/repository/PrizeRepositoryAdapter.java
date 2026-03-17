package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.PrizePO;
import io.prizewheel.adapters.persistence.mapper.PrizeMapper;
import io.prizewheel.core.domain.entity.Prize;
import io.prizewheel.core.port.output.PrizeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖品仓储适配器
 * 
 * 负责奖品基本信息的持久化操作
 * 奖品的数量和中奖率由PolicyPrizeRepository处理
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class PrizeRepositoryAdapter implements PrizeRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(PrizeRepositoryAdapter.class);

    @Resource
    private PrizeMapper prizeMapper;

    @Override
    public int save(Prize prize) {
        log.info("保存奖品 prizeId:{} prizeName:{}", prize.getPrizeId(), prize.getPrizeName());
        return prizeMapper.insert(toPO(prize));
    }

    @Override
    public int update(Prize prize) {
        log.info("更新奖品 prizeId:{}", prize.getPrizeId());
        return prizeMapper.update(toPO(prize));
    }

    @Override
    public Prize findById(String prizeId) {
        log.debug("查询奖品 prizeId:{}", prizeId);
        PrizePO po = prizeMapper.selectById(prizeId);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Prize> findByCampaignId(Long campaignId) {
        log.debug("查询活动奖品 campaignId:{}", campaignId);
        return prizeMapper.selectByCampaignId(campaignId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prize> findAll() {
        log.debug("查询所有奖品");
        return prizeMapper.selectAll().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private Prize toEntity(PrizePO po) {
        Prize entity = new Prize();
        entity.setPrizeId(po.getPrizeId());
        entity.setPrizeType(po.getPrizeType());
        entity.setPrizeName(po.getPrizeName());
        entity.setPrizeContent(po.getPrizeContent());
        return entity;
    }

    private PrizePO toPO(Prize entity) {
        PrizePO po = new PrizePO();
        po.setPrizeId(entity.getPrizeId());
        po.setPrizeType(entity.getPrizeType());
        po.setPrizeName(entity.getPrizeName());
        po.setPrizeContent(entity.getPrizeContent());
        return po;
    }
}
