package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.CampaignTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动模板服务端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface CampaignTemplateServicePort {

    CampaignTemplate createTemplate(CampaignTemplate template);

    CampaignTemplate queryTemplate(Long templateId);

    List<CampaignTemplate> queryTemplateList();

    List<CampaignTemplate> queryTemplateListByCreator(String creator);

    Campaign createCampaignFromTemplate(Long templateId, String campaignName, LocalDateTime startTime, int durationDays);

    boolean deleteTemplate(Long templateId);
}
