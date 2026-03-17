package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.Statistics;
import io.prizewheel.core.domain.entity.WinRecord;

import java.util.List;

/**
 * 统计服务端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface StatisticsServicePort {

    List<Statistics> queryCampaignStatistics(Long campaignId);

    List<Statistics> queryUserStatistics(String userId);

    List<Statistics> queryPrizeStatistics(String prizeId);

    List<Statistics> queryHotCampaigns(int limit);

    List<WinRecord> queryUserWinRecords(String userId, int page, int size);

    void recordDrawEvent(String userId, Long campaignId);

    void recordWinEvent(String userId, Long campaignId, String prizeId);

    void recordGrantEvent(String userId, Long campaignId, String prizeId);

    Statistics getCampaignSummary(Long campaignId);

    Statistics getUserSummary(String userId);
}
