package io.prizewheel.api.facade;

import io.prizewheel.api.dto.CampaignRequest;
import io.prizewheel.api.dto.CampaignResponse;
import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.port.input.CampaignServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务门面
 * 
 * @author Allein
 * @since 2.0.0
 */
@DubboService(version = "1.0.0")
public class CampaignFacade {

    private final CampaignServicePort campaignService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CampaignFacade(CampaignServicePort campaignService) {
        this.campaignService = campaignService;
    }

    public ApiResult<Long> createCampaign(CampaignRequest request) {
        try {
            Campaign campaign = toEntity(request);
            campaignService.createCampaign(campaign);
            return ApiResult.success(campaign.getCampaignId());
        } catch (Exception e) {
            return ApiResult.fail("创建活动失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> updateCampaign(CampaignRequest request) {
        try {
            Campaign campaign = toEntity(request);
            boolean result = campaignService.updateCampaign(campaign);
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("更新活动失败: " + e.getMessage());
        }
    }

    public ApiResult<CampaignResponse> queryCampaign(Long campaignId) {
        try {
            Campaign campaign = campaignService.queryCampaign(campaignId);
            if (campaign == null) {
                return ApiResult.fail("活动不存在");
            }
            return ApiResult.success(toResponse(campaign));
        } catch (Exception e) {
            return ApiResult.fail("查询活动失败: " + e.getMessage());
        }
    }

    public ApiResult<List<CampaignResponse>> queryAvailableCampaigns() {
        try {
            List<Campaign> campaigns = campaignService.queryAvailableCampaigns();
            List<CampaignResponse> responses = campaigns.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询可用活动失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> updateStatus(Long campaignId, Integer status) {
        try {
            boolean result = campaignService.updateStatus(campaignId, status);
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("更新活动状态失败: " + e.getMessage());
        }
    }

    private Campaign toEntity(CampaignRequest request) {
        Campaign campaign = new Campaign();
        campaign.setCampaignId(request.getCampaignId());
        campaign.setCampaignName(request.getCampaignName());
        campaign.setCampaignDesc(request.getCampaignDesc());
        if (request.getStartTime() != null) {
            campaign.setStartTime(LocalDateTime.parse(request.getStartTime(), FORMATTER));
        }
        if (request.getEndTime() != null) {
            campaign.setEndTime(LocalDateTime.parse(request.getEndTime(), FORMATTER));
        }
        campaign.setTotalStock(request.getTotalStock());
        campaign.setRemainingStock(request.getTotalStock());
        campaign.setMaxParticipations(request.getMaxParticipations());
        campaign.setPolicyId(request.getPolicyId());
        campaign.setCreator(request.getCreator());
        return campaign;
    }

    private CampaignResponse toResponse(Campaign campaign) {
        CampaignResponse response = new CampaignResponse();
        response.setCampaignId(campaign.getCampaignId());
        response.setCampaignName(campaign.getCampaignName());
        response.setCampaignDesc(campaign.getCampaignDesc());
        if (campaign.getStartTime() != null) {
            response.setStartTime(campaign.getStartTime().format(FORMATTER));
        }
        if (campaign.getEndTime() != null) {
            response.setEndTime(campaign.getEndTime().format(FORMATTER));
        }
        response.setTotalStock(campaign.getTotalStock());
        response.setRemainingStock(campaign.getRemainingStock());
        response.setMaxParticipations(campaign.getMaxParticipations());
        response.setPolicyId(campaign.getPolicyId());
        response.setStatus(campaign.getStatus());
        response.setCreator(campaign.getCreator());
        return response;
    }
}
