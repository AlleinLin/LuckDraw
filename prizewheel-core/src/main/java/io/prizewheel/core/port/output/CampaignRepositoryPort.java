package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.Campaign;

import java.util.List;

/**
 * 活动仓储输出端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface CampaignRepositoryPort {

    int save(Campaign campaign);

    int update(Campaign campaign);

    Campaign findById(Long campaignId);

    List<Campaign> findAll();

    List<Campaign> findAvailable();

    List<Campaign> findActiveCampaigns();

    List<Campaign> findExpiredCampaigns();

    int updateStatus(Long campaignId, Integer status);

    int decreaseStock(Long campaignId);

    int increaseStock(Long campaignId);

    int updateStock(Long campaignId, Integer stock);

    int calculateRemainingStock(Long campaignId);
}
