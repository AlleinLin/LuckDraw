package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.DrawPolicyPO;
import io.prizewheel.adapters.persistence.entity.PolicyPrizePO;
import io.prizewheel.adapters.persistence.mapper.DrawPolicyMapper;
import io.prizewheel.adapters.persistence.mapper.PolicyPrizeMapper;
import io.prizewheel.core.domain.entity.DrawPolicy;
import io.prizewheel.core.port.output.DrawPolicyRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽奖策略仓储适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Repository
public class DrawPolicyRepositoryAdapter implements DrawPolicyRepositoryPort {

    @Resource
    private DrawPolicyMapper policyMapper;

    @Resource
    private PolicyPrizeMapper prizeMapper;

    @Override
    public int save(DrawPolicy policy) {
        return policyMapper.insert(toPO(policy));
    }

    @Override
    public DrawPolicy findById(Long policyId) {
        DrawPolicyPO po = policyMapper.selectById(policyId);
        if (po == null) {
            return null;
        }
        DrawPolicy entity = toEntity(po);
        loadPrizeConfigs(entity);
        return entity;
    }

    @Override
    public DrawPolicy findByCampaignId(Long campaignId) {
        DrawPolicyPO po = policyMapper.selectByCampaignId(campaignId);
        if (po == null) {
            return null;
        }
        DrawPolicy entity = toEntity(po);
        loadPrizeConfigs(entity);
        return entity;
    }

    private void loadPrizeConfigs(DrawPolicy policy) {
        List<PolicyPrizePO> prizes = prizeMapper.selectByPolicyId(policy.getPolicyId());
        for (PolicyPrizePO prize : prizes) {
            DrawPolicy.PrizeConfig config = new DrawPolicy.PrizeConfig();
            config.setPrizeId(prize.getPrizeId());
            config.setPrizeName(prize.getPrizeName());
            config.setTotalQuantity(prize.getTotalQuantity());
            config.setRemainingQuantity(prize.getRemainingQuantity());
            config.setWinRate(prize.getWinRate());
            policy.addPrizeConfig(config);
        }
    }

    private DrawPolicy toEntity(DrawPolicyPO po) {
        DrawPolicy entity = new DrawPolicy();
        entity.setPolicyId(po.getPolicyId());
        entity.setPolicyName(po.getPolicyName());
        entity.setDrawMode(po.getDrawMode());
        entity.setGrantType(po.getGrantType());
        return entity;
    }

    private DrawPolicyPO toPO(DrawPolicy entity) {
        DrawPolicyPO po = new DrawPolicyPO();
        po.setPolicyId(entity.getPolicyId());
        po.setPolicyName(entity.getPolicyName());
        po.setDrawMode(entity.getDrawMode());
        po.setGrantType(entity.getGrantType());
        return po;
    }
}
