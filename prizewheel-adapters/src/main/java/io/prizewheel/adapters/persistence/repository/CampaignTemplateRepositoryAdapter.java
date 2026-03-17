package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.CampaignTemplatePO;
import io.prizewheel.adapters.persistence.mapper.CampaignTemplateMapper;
import io.prizewheel.core.domain.entity.CampaignTemplate;
import io.prizewheel.core.port.output.CampaignTemplateRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动模板仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class CampaignTemplateRepositoryAdapter implements CampaignTemplateRepositoryPort {

    @Resource
    private CampaignTemplateMapper campaignTemplateMapper;

    @Override
    public CampaignTemplate findById(Long templateId) {
        CampaignTemplatePO po = campaignTemplateMapper.findById(templateId);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<CampaignTemplate> findAll() {
        return campaignTemplateMapper.findAll().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignTemplate> findByCreator(String creator) {
        return campaignTemplateMapper.findByCreator(creator).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void save(CampaignTemplate template) {
        campaignTemplateMapper.insert(toPO(template));
    }

    @Override
    public void delete(Long templateId) {
        campaignTemplateMapper.delete(templateId);
    }

    @Override
    public void incrementUseCount(Long templateId) {
        campaignTemplateMapper.incrementUseCount(templateId);
    }

    private CampaignTemplate toEntity(CampaignTemplatePO po) {
        CampaignTemplate entity = new CampaignTemplate();
        entity.setTemplateId(po.getTemplateId());
        entity.setTemplateName(po.getTemplateName());
        entity.setTemplateDesc(po.getTemplateDesc());
        entity.setDefaultDurationDays(po.getDefaultDurationDays());
        entity.setDefaultTotalStock(po.getDefaultTotalStock());
        entity.setDefaultMaxParticipations(po.getDefaultMaxParticipations());
        entity.setDrawMode(po.getDrawMode());
        entity.setGrantType(po.getGrantType());
        entity.setStatus(po.getStatus());
        entity.setCreator(po.getCreator());
        entity.setUseCount(po.getUseCount());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private CampaignTemplatePO toPO(CampaignTemplate entity) {
        CampaignTemplatePO po = new CampaignTemplatePO();
        po.setTemplateId(entity.getTemplateId());
        po.setTemplateName(entity.getTemplateName());
        po.setTemplateDesc(entity.getTemplateDesc());
        po.setDefaultDurationDays(entity.getDefaultDurationDays());
        po.setDefaultTotalStock(entity.getDefaultTotalStock());
        po.setDefaultMaxParticipations(entity.getDefaultMaxParticipations());
        po.setDrawMode(entity.getDrawMode());
        po.setGrantType(entity.getGrantType());
        po.setStatus(entity.getStatus());
        po.setCreator(entity.getCreator());
        po.setUseCount(entity.getUseCount() != null ? entity.getUseCount() : 0);
        return po;
    }
}
