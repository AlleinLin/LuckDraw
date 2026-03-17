package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.DrawPolicyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 抽奖策略Mapper接口
 * 
 * @author Allein
 * @since 1.0.0
 */
@Mapper
public interface DrawPolicyMapper {

    int insert(DrawPolicyPO policy);

    DrawPolicyPO selectById(@Param("policyId") Long policyId);

    DrawPolicyPO selectByCampaignId(@Param("campaignId") Long campaignId);
}
