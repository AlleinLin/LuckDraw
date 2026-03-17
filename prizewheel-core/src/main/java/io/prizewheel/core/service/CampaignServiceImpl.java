package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.port.input.CampaignServicePort;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 活动服务实现
 * 
 * @author Allein
 * @since 1.0.0
 */
public class CampaignServiceImpl implements CampaignServicePort {

    private static final Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);

    private final CampaignRepositoryPort repository;
    private final IdGeneratorPort idGenerator;

    public CampaignServiceImpl(CampaignRepositoryPort repository, IdGeneratorPort idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Long createCampaign(Campaign campaign) {
        log.info("创建活动 name:{}", campaign.getCampaignName());
        
        Long campaignId = idGenerator.nextId();
        campaign.setCampaignId(campaignId);
        campaign.setRemainingStock(campaign.getTotalStock());
        campaign.setStatus(1);
        
        repository.save(campaign);
        
        log.info("活动创建成功 campaignId:{}", campaignId);
        return campaignId;
    }

    @Override
    public boolean updateCampaign(Campaign campaign) {
        log.info("更新活动 campaignId:{}", campaign.getCampaignId());
        return repository.update(campaign) > 0;
    }

    @Override
    public Campaign queryCampaign(Long campaignId) {
        return repository.findById(campaignId);
    }

    @Override
    public List<Campaign> queryAvailableCampaigns() {
        return repository.findAvailable();
    }

    @Override
    public boolean updateStatus(Long campaignId, Integer status) {
        log.info("更新活动状态 campaignId:{} status:{}", campaignId, status);
        return repository.updateStatus(campaignId, status) > 0;
    }
}
