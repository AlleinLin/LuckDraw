package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.PrizePO;
import io.prizewheel.adapters.persistence.mapper.PrizeMapper;
import io.prizewheel.core.domain.entity.Prize;
import io.prizewheel.core.port.output.PrizeRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖品仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class PrizeRepositoryAdapter implements PrizeRepositoryPort {

    @Resource
    private PrizeMapper prizeMapper;

    @Override
    public int save(Prize prize) {
        return prizeMapper.insert(toPO(prize));
    }

    @Override
    public int update(Prize prize) {
        return prizeMapper.update(toPO(prize));
    }

    @Override
    public Prize findById(String prizeId) {
        PrizePO po = prizeMapper.selectById(prizeId);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Prize> findByCampaignId(Long campaignId) {
        return prizeMapper.selectByCampaignId(campaignId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prize> findAll() {
        return prizeMapper.selectAll().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int decreaseQuantity(String prizeId) {
        return prizeMapper.decreaseQuantity(prizeId);
    }

    @Override
    public int updateQuantity(String prizeId, Integer quantity) {
        return prizeMapper.updateQuantity(prizeId, quantity);
    }

    private Prize toEntity(PrizePO po) {
        Prize entity = new Prize();
        entity.setPrizeId(po.getPrizeId());
        entity.setPrizeType(po.getPrizeType());
        entity.setPrizeName(po.getPrizeName());
        entity.setPrizeContent(po.getPrizeContent());
        entity.setTotalQuantity(po.getTotalQuantity());
        entity.setRemainingQuantity(po.getRemainingQuantity());
        entity.setWinRate(po.getWinRate());
        return entity;
    }

    private PrizePO toPO(Prize entity) {
        PrizePO po = new PrizePO();
        po.setPrizeId(entity.getPrizeId());
        po.setPrizeType(entity.getPrizeType());
        po.setPrizeName(entity.getPrizeName());
        po.setPrizeContent(entity.getPrizeContent());
        po.setTotalQuantity(entity.getTotalQuantity());
        po.setRemainingQuantity(entity.getRemainingQuantity());
        po.setWinRate(entity.getWinRate());
        return po;
    }
}
