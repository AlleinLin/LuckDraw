package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.CampaignTemplate;

import java.util.List;

/**
 * 活动模板仓储端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface CampaignTemplateRepositoryPort {

    CampaignTemplate findById(Long templateId);

    List<CampaignTemplate> findAll();

    List<CampaignTemplate> findByCreator(String creator);

    void save(CampaignTemplate template);

    void delete(Long templateId);

    void incrementUseCount(Long templateId);
}
