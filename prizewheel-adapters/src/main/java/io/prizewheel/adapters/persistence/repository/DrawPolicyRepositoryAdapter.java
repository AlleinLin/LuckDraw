package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.DrawPolicyPO;
import io.prizewheel.adapters.persistence.entity.PolicyPrizePO;
import io.prizewheel.adapters.persistence.mapper.DrawPolicyMapper;
import io.prizewheel.adapters.persistence.mapper.PolicyPrizeMapper;
import io.prizewheel.core.domain.entity.DrawPolicy;
import io.prizewheel.core.port.output.DrawPolicyRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 抽奖策略仓储适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Repository
public class DrawPolicyRepositoryAdapter implements DrawPolicyRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(DrawPolicyRepositoryAdapter.class);

    @Resource
    private DrawPolicyMapper policyMapper;

    @Resource
    private PolicyPrizeMapper prizeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(DrawPolicy policy) {
        log.info("保存抽奖策略 policyId:{} policyName:{}", policy.getPolicyId(), policy.getPolicyName());
        
        int result = policyMapper.insert(toPO(policy));
        
        if (policy.getPrizeConfigs() != null && !policy.getPrizeConfigs().isEmpty()) {
            for (DrawPolicy.PrizeConfig config : policy.getPrizeConfigs()) {
                PolicyPrizePO prizePO = new PolicyPrizePO();
                prizePO.setPolicyId(policy.getPolicyId());
                prizePO.setPrizeId(config.getPrizeId());
                prizePO.setPrizeName(config.getPrizeName());
                prizePO.setTotalQuantity(config.getTotalQuantity());
                prizePO.setRemainingQuantity(config.getRemainingQuantity());
                prizePO.setWinRate(config.getWinRate());
                prizeMapper.insert(prizePO);
            }
            log.info("保存奖品配置数量: {}", policy.getPrizeConfigs().size());
        }
        
        return result;
    }

    @Override
    public DrawPolicy findById(Long policyId) {
        log.debug("查询抽奖策略 policyId:{}", policyId);
        DrawPolicyPO po = policyMapper.selectById(policyId);
        if (po == null) {
            log.warn("抽奖策略不存在 policyId:{}", policyId);
            return null;
        }
        DrawPolicy entity = toEntity(po);
        loadPrizeConfigs(entity);
        return entity;
    }

    @Override
    public DrawPolicy findByCampaignId(Long campaignId) {
        log.debug("根据活动ID查询抽奖策略 campaignId:{}", campaignId);
        DrawPolicyPO po = policyMapper.selectByCampaignId(campaignId);
        if (po == null) {
            log.warn("活动未配置抽奖策略 campaignId:{}", campaignId);
            return null;
        }
        DrawPolicy entity = toEntity(po);
        loadPrizeConfigs(entity);
        return entity;
    }

    @Override
    public int decreasePrizeQuantity(Long policyId, String prizeId) {
        log.info("扣减奖品库存 policyId:{} prizeId:{}", policyId, prizeId);
        int result = prizeMapper.decreaseQuantity(policyId, prizeId);
        if (result > 0) {
            log.info("奖品库存扣减成功 policyId:{} prizeId:{}", policyId, prizeId);
        } else {
            log.warn("奖品库存扣减失败(库存不足或不存在) policyId:{} prizeId:{}", policyId, prizeId);
        }
        return result;
    }

    @Override
    public boolean checkPrizeAvailable(Long policyId, String prizeId) {
        PolicyPrizePO prize = prizeMapper.selectByPolicyAndPrize(policyId, prizeId);
        if (prize == null) {
            log.warn("奖品配置不存在 policyId:{} prizeId:{}", policyId, prizeId);
            return false;
        }
        return prize.getRemainingQuantity() != null && prize.getRemainingQuantity() > 0;
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
        log.debug("加载奖品配置数量: {}", prizes.size());
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
