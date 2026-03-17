package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.WinRecordPO;
import io.prizewheel.adapters.persistence.mapper.WinRecordMapper;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 中奖记录仓储适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Repository
public class WinRecordRepositoryAdapter implements WinRecordRepositoryPort {

    @Resource
    private WinRecordMapper winRecordMapper;

    @Override
    public int save(WinRecord record) {
        return winRecordMapper.insert(toPO(record));
    }

    @Override
    public WinRecord findById(Long recordId) {
        WinRecordPO po = winRecordMapper.selectById(recordId);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<WinRecord> findByParticipantId(String participantId) {
        return winRecordMapper.selectByParticipantId(participantId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WinRecord> findByCampaignId(Long campaignId) {
        return winRecordMapper.selectByCampaignId(campaignId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WinRecord> findPendingRecords() {
        return winRecordMapper.selectPendingRecords().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int updateStatus(Long recordId, Integer status) {
        return winRecordMapper.updateStatus(recordId, status);
    }

    @Override
    public int countByParticipantAndCampaign(String participantId, Long campaignId) {
        return winRecordMapper.countByParticipantAndCampaign(participantId, campaignId);
    }

    private WinRecord toEntity(WinRecordPO po) {
        WinRecord entity = new WinRecord();
        entity.setRecordId(po.getRecordId());
        entity.setParticipantId(po.getParticipantId());
        entity.setCampaignId(po.getCampaignId());
        entity.setPolicyId(po.getPolicyId());
        entity.setPrizeId(po.getPrizeId());
        entity.setPrizeName(po.getPrizeName());
        entity.setPrizeType(po.getPrizeType());
        entity.setPrizeContent(po.getPrizeContent());
        entity.setStatus(po.getStatus());
        entity.setWinTime(po.getWinTime());
        entity.setGrantTime(po.getGrantTime());
        return entity;
    }

    private WinRecordPO toPO(WinRecord entity) {
        WinRecordPO po = new WinRecordPO();
        po.setRecordId(entity.getRecordId());
        po.setParticipantId(entity.getParticipantId());
        po.setCampaignId(entity.getCampaignId());
        po.setPolicyId(entity.getPolicyId());
        po.setPrizeId(entity.getPrizeId());
        po.setPrizeName(entity.getPrizeName());
        po.setPrizeType(entity.getPrizeType());
        po.setPrizeContent(entity.getPrizeContent());
        po.setStatus(entity.getStatus());
        po.setWinTime(entity.getWinTime());
        po.setGrantTime(entity.getGrantTime());
        return po;
    }
}
