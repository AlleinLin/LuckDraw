package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.Prize;

import java.util.List;

/**
 * 奖品仓储输出端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface PrizeRepositoryPort {

    int save(Prize prize);

    int update(Prize prize);

    Prize findById(String prizeId);

    List<Prize> findByCampaignId(Long campaignId);

    List<Prize> findAll();

    int decreaseQuantity(String prizeId);

    int updateQuantity(String prizeId, Integer quantity);
}
