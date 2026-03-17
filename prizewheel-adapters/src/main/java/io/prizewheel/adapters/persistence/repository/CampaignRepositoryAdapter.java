package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.CampaignPO;
import io.prizewheel.adapters.persistence.mapper.CampaignMapper;
import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动仓储适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Repository
public class CampaignRepositoryAdapter implements CampaignRepositoryPort {

    @Resource
    private CampaignMapper campaignMapper;

    @Override
    public int save(Campaign campaign) {
        return campaignMapper.insert(toPO(campaign));
    }

    @Override
    public int update(Campaign campaign) {
        return campaignMapper.update(toPO(campaign));
    }

    @Override
    public Campaign findById(Long campaignId) {
        CampaignPO po = campaignMapper.selectById(campaignId);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Campaign> findAll() {
        return campaignMapper.selectAll().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Campaign> findAvailable() {
        return campaignMapper.selectAvailable().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Campaign> findActiveCampaigns() {
        return campaignMapper.selectActiveCampaigns().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Campaign> findExpiredCampaigns(LocalDateTime beforeTime) {
        return campaignMapper.selectExpiredCampaigns(beforeTime).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int updateStatus(Long campaignId, Integer status) {
        return campaignMapper.updateStatus(campaignId, status);
    }

    @Override
    public int decreaseStock(Long campaignId) {
        return campaignMapper.decreaseStock(campaignId);
    }

    @Override
    public int increaseStock(Long campaignId) {
        return campaignMapper.increaseStock(campaignId);
    }

    @Override
    public int updateStock(Long campaignId, Integer stock) {
        return campaignMapper.updateStock(campaignId, stock);
    }

    @Override
    public int calculateRemainingStock(Long campaignId) {
        Integer stock = campaignMapper.calculateRemainingStock(campaignId);
        return stock != null ? stock : 0;
    }

    private Campaign toEntity(CampaignPO po) {
        Campaign entity = new Campaign();
        entity.setCampaignId(po.getCampaignId());
        entity.setCampaignName(po.getCampaignName());
        entity.setCampaignDesc(po.getCampaignDesc());
        entity.setStartTime(po.getStartTime());
        entity.setEndTime(po.getEndTime());
        entity.setTotalStock(po.getTotalStock());
        entity.setRemainingStock(po.getRemainingStock());
        entity.setMaxParticipations(po.getMaxParticipations());
        entity.setPolicyId(po.getPolicyId());
        entity.setStatus(po.getStatus());
        entity.setCreator(po.getCreator());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private CampaignPO toPO(Campaign entity) {
        CampaignPO po = new CampaignPO();
        po.setCampaignId(entity.getCampaignId());
        po.setCampaignName(entity.getCampaignName());
        po.setCampaignDesc(entity.getCampaignDesc());
        po.setStartTime(entity.getStartTime());
        po.setEndTime(entity.getEndTime());
        po.setTotalStock(entity.getTotalStock());
        po.setRemainingStock(entity.getRemainingStock());
        po.setMaxParticipations(entity.getMaxParticipations());
        po.setPolicyId(entity.getPolicyId());
        po.setStatus(entity.getStatus());
        po.setCreator(entity.getCreator());
        return po;
    }
}
