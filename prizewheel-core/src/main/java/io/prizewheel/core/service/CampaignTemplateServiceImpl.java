package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.CampaignTemplate;
import io.prizewheel.core.domain.entity.DrawPolicy;
import io.prizewheel.core.port.input.CampaignServicePort;
import io.prizewheel.core.port.input.CampaignTemplateServicePort;
import io.prizewheel.core.port.output.CampaignTemplateRepositoryPort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动模板服务实现
 * 
 * @author Allein
 * @since 2.0.0
 */
public class CampaignTemplateServiceImpl implements CampaignTemplateServicePort {

    private static final Logger log = LoggerFactory.getLogger(CampaignTemplateServiceImpl.class);

    private final CampaignTemplateRepositoryPort templateRepository;
    private final CampaignServicePort campaignService;
    private final IdGeneratorPort idGenerator;

    public CampaignTemplateServiceImpl(CampaignTemplateRepositoryPort templateRepository,
                                        CampaignServicePort campaignService,
                                        IdGeneratorPort idGenerator) {
        this.templateRepository = templateRepository;
        this.campaignService = campaignService;
        this.idGenerator = idGenerator;
    }

    @Override
    public CampaignTemplate createTemplate(CampaignTemplate template) {
        log.info("创建活动模板 templateName:{}", template.getTemplateName());

        template.setTemplateId(idGenerator.nextId());
        template.setStatus(1);
        template.setUseCount(0);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());

        if (template.getPrizeConfigs() != null) {
            for (CampaignTemplate.TemplatePrizeConfig config : template.getPrizeConfigs()) {
                config.setConfigId(idGenerator.nextId());
                config.setTemplateId(template.getTemplateId());
                config.setCreatedAt(LocalDateTime.now());
            }
        }

        templateRepository.save(template);
        log.info("活动模板创建成功 templateId:{}", template.getTemplateId());
        return template;
    }

    @Override
    public CampaignTemplate queryTemplate(Long templateId) {
        log.info("查询活动模板 templateId:{}", templateId);
        return templateRepository.findById(templateId);
    }

    @Override
    public List<CampaignTemplate> queryTemplateList() {
        log.info("查询活动模板列表");
        return templateRepository.findAll();
    }

    @Override
    public List<CampaignTemplate> queryTemplateListByCreator(String creator) {
        log.info("查询用户创建的模板列表 creator:{}", creator);
        return templateRepository.findByCreator(creator);
    }

    @Override
    public Campaign createCampaignFromTemplate(Long templateId, String campaignName, LocalDateTime startTime, int durationDays) {
        log.info("从模板创建活动 templateId:{} campaignName:{}", templateId, campaignName);

        CampaignTemplate template = templateRepository.findById(templateId);
        if (template == null) {
            log.warn("模板不存在 templateId:{}", templateId);
            return null;
        }

        Campaign campaign = template.createCampaign(campaignName, startTime, durationDays);
        campaign.setCampaignId(idGenerator.nextId());

        campaignService.createCampaign(campaign);

        templateRepository.incrementUseCount(templateId);

        log.info("从模板创建活动成功 campaignId:{} templateId:{}", campaign.getCampaignId(), templateId);
        return campaign;
    }

    @Override
    public boolean deleteTemplate(Long templateId) {
        log.info("删除活动模板 templateId:{}", templateId);

        CampaignTemplate template = templateRepository.findById(templateId);
        if (template == null) {
            log.warn("模板不存在 templateId:{}", templateId);
            return false;
        }

        templateRepository.delete(templateId);
        log.info("活动模板删除成功 templateId:{}", templateId);
        return true;
    }
}
