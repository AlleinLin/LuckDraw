package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.WinRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 中奖记录Mapper接口
 * 
 * @author Allein
 * @since 1.0.0
 */
@Mapper
public interface WinRecordMapper {

    int insert(WinRecordPO record);

    WinRecordPO selectById(@Param("recordId") Long recordId);

    List<WinRecordPO> selectByParticipantId(@Param("participantId") String participantId);

    List<WinRecordPO> selectByCampaignId(@Param("campaignId") Long campaignId);

    List<WinRecordPO> selectPendingRecords();

    int updateStatus(@Param("recordId") Long recordId, @Param("status") Integer status);

    int countByParticipantAndCampaign(@Param("participantId") String participantId, @Param("campaignId") Long campaignId);
}
