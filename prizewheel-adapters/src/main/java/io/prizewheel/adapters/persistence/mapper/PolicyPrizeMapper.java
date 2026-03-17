package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.PolicyPrizePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略奖品配置Mapper接口
 * 
 * @author Allein
 * @since 1.0.0
 */
@Mapper
public interface PolicyPrizeMapper {

    int insert(PolicyPrizePO prize);

    int batchInsert(@Param("list") List<PolicyPrizePO> prizes);

    List<PolicyPrizePO> selectByPolicyId(@Param("policyId") Long policyId);

    int decreaseQuantity(@Param("policyId") Long policyId, @Param("prizeId") String prizeId);
}
