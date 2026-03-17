package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.WinRecord;

/**
 * 抽奖服务输入端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface DrawServicePort {

    /**
     * 执行抽奖
     *
     * @param participantId 参与者ID
     * @param campaignId 活动ID
     * @return 中奖记录
     */
    WinRecord executeDraw(String participantId, Long campaignId);

    /**
     * 查询中奖记录
     *
     * @param recordId 记录ID
     * @return 中奖记录
     */
    WinRecord queryWinRecord(Long recordId);
}
