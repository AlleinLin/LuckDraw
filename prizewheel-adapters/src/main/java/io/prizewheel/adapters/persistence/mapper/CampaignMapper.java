package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.CampaignPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动Mapper接口
 * 
 * @author Allein
 * @since 1.0.0
 */
@Mapper
public interface CampaignMapper {

    int insert(CampaignPO campaign);

    int update(CampaignPO campaign);

    CampaignPO selectById(@Param("campaignId") Long campaignId);

    List<CampaignPO> selectAll();

    List<CampaignPO> selectAvailable();

    List<CampaignPO> selectActiveCampaigns();

    List<CampaignPO> selectExpiredCampaigns(@Param("beforeTime") LocalDateTime beforeTime);

    int updateStatus(@Param("campaignId") Long campaignId, @Param("status") Integer status);

    int decreaseStock(@Param("campaignId") Long campaignId);

    int increaseStock(@Param("campaignId") Long campaignId);

    int updateStock(@Param("campaignId") Long campaignId, @Param("stock") Integer stock);

    Integer calculateRemainingStock(@Param("campaignId") Long campaignId);
}
