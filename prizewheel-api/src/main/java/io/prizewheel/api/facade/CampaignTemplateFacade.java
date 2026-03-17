package io.prizewheel.api.facade;

import io.prizewheel.api.dto.CampaignResponse;
import io.prizewheel.api.dto.TemplateRequest;
import io.prizewheel.api.dto.TemplateResponse;
import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.CampaignTemplate;
import io.prizewheel.core.port.input.CampaignTemplateServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动模板服务门面
 * 
 * @author Allein
 * @since 2.0.0
 */
@DubboService(version = "1.0.0")
public class CampaignTemplateFacade {

    private final CampaignTemplateServicePort templateService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CampaignTemplateFacade(CampaignTemplateServicePort templateService) {
        this.templateService = templateService;
    }

    public ApiResult<Long> createTemplate(TemplateRequest request) {
        try {
            CampaignTemplate template = toEntity(request);
            CampaignTemplate created = templateService.createTemplate(template);
            return ApiResult.success(created.getTemplateId());
        } catch (Exception e) {
            return ApiResult.fail("创建模板失败: " + e.getMessage());
        }
    }

    public ApiResult<TemplateResponse> queryTemplate(Long templateId) {
        try {
            CampaignTemplate template = templateService.queryTemplate(templateId);
            if (template == null) {
                return ApiResult.fail("模板不存在");
            }
            return ApiResult.success(toResponse(template));
        } catch (Exception e) {
            return ApiResult.fail("查询模板失败: " + e.getMessage());
        }
    }

    public ApiResult<List<TemplateResponse>> queryTemplateList() {
        try {
            List<CampaignTemplate> templates = templateService.queryTemplateList();
            List<TemplateResponse> responses = templates.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询模板列表失败: " + e.getMessage());
        }
    }

    public ApiResult<List<TemplateResponse>> queryTemplateListByCreator(String creator) {
        try {
            List<CampaignTemplate> templates = templateService.queryTemplateListByCreator(creator);
            List<TemplateResponse> responses = templates.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询用户模板列表失败: " + e.getMessage());
        }
    }

    public ApiResult<CampaignResponse> createCampaignFromTemplate(Long templateId, String campaignName, 
                                                                   String startTime, Integer durationDays) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime, FORMATTER) : LocalDateTime.now();
            Campaign campaign = templateService.createCampaignFromTemplate(templateId, campaignName, start, durationDays);
            if (campaign == null) {
                return ApiResult.fail("从模板创建活动失败");
            }
            return ApiResult.success(toCampaignResponse(campaign));
        } catch (Exception e) {
            return ApiResult.fail("从模板创建活动失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> deleteTemplate(Long templateId) {
        try {
            boolean result = templateService.deleteTemplate(templateId);
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("删除模板失败: " + e.getMessage());
        }
    }

    private CampaignTemplate toEntity(TemplateRequest request) {
        CampaignTemplate template = new CampaignTemplate();
        template.setTemplateId(request.getTemplateId());
        template.setTemplateName(request.getTemplateName());
        template.setTemplateDesc(request.getTemplateDesc());
        template.setDefaultDurationDays(request.getDefaultDurationDays());
        template.setDefaultTotalStock(request.getDefaultTotalStock());
        template.setDefaultMaxParticipations(request.getDefaultMaxParticipations());
        template.setDrawMode(request.getDrawMode());
        template.setGrantType(request.getGrantType());
        template.setCreator(request.getCreator());
        return template;
    }

    private TemplateResponse toResponse(CampaignTemplate template) {
        TemplateResponse response = new TemplateResponse();
        response.setTemplateId(template.getTemplateId());
        response.setTemplateName(template.getTemplateName());
        response.setTemplateDesc(template.getTemplateDesc());
        response.setDefaultDurationDays(template.getDefaultDurationDays());
        response.setDefaultTotalStock(template.getDefaultTotalStock());
        response.setDefaultMaxParticipations(template.getDefaultMaxParticipations());
        response.setDrawMode(template.getDrawMode());
        response.setGrantType(template.getGrantType());
        response.setStatus(template.getStatus());
        response.setCreator(template.getCreator());
        response.setUseCount(template.getUseCount());
        return response;
    }

    private CampaignResponse toCampaignResponse(Campaign campaign) {
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
