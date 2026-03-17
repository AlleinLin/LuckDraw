package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.DrawPolicy;

/**
 * 抽奖策略仓储输出端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface DrawPolicyRepositoryPort {

    int save(DrawPolicy policy);

    DrawPolicy findById(Long policyId);

    DrawPolicy findByCampaignId(Long campaignId);

    int decreasePrizeQuantity(Long policyId, String prizeId);

    boolean checkPrizeAvailable(Long policyId, String prizeId);
}
