package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.WinRecord;

import java.util.List;

/**
 * 中奖记录仓储输出端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface WinRecordRepositoryPort {

    int save(WinRecord record);

    WinRecord findById(Long recordId);

    List<WinRecord> findByParticipantId(String participantId);

    List<WinRecord> findByCampaignId(Long campaignId);

    List<WinRecord> findPendingRecords();

    int updateStatus(Long recordId, Integer status);

    int countByParticipantAndCampaign(String participantId, Long campaignId);
}
