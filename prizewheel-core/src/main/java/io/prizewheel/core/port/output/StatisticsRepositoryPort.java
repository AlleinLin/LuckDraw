package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.Statistics;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计仓储端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface StatisticsRepositoryPort {

    Statistics findByDimension(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate);

    List<Statistics> findByCampaignId(Long campaignId);

    List<Statistics> findByUserId(String userId);

    List<Statistics> findByPrizeId(String prizeId);

    List<Statistics> findHotCampaigns(int limit);

    void save(Statistics statistics);

    void incrementTotalCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate);

    void incrementWinCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate);

    void incrementGrantCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate);
}
