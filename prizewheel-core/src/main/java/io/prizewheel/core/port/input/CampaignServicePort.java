package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.Campaign;

import java.util.List;

/**
 * 活动服务输入端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface CampaignServicePort {

    /**
     * 创建活动
     *
     * @param campaign 活动信息
     * @return 活动ID
     */
    Long createCampaign(Campaign campaign);

    /**
     * 更新活动
     *
     * @param campaign 活动信息
     * @return 是否成功
     */
    boolean updateCampaign(Campaign campaign);

    /**
     * 查询活动
     *
     * @param campaignId 活动ID
     * @return 活动信息
     */
    Campaign queryCampaign(Long campaignId);

    /**
     * 查询可用活动列表
     *
     * @return 活动列表
     */
    List<Campaign> queryAvailableCampaigns();

    /**
     * 更新活动状态
     *
     * @param campaignId 活动ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long campaignId, Integer status);
}
