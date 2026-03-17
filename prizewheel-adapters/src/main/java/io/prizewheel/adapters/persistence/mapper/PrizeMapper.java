package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.PrizePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 奖品Mapper接口
 * 
 * @author Allein
 * @since 2.0.0
 */
@Mapper
public interface PrizeMapper {

    int insert(PrizePO prize);

    int update(PrizePO prize);

    PrizePO selectById(@Param("prizeId") String prizeId);

    List<PrizePO> selectByCampaignId(@Param("campaignId") Long campaignId);

    List<PrizePO> selectAll();
}
