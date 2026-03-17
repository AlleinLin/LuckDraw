package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Statistics;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.StatisticsServicePort;
import io.prizewheel.core.port.output.StatisticsRepositoryPort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计服务实现
 * 
 * @author Allein
 * @since 2.0.0
 */
public class StatisticsServiceImpl implements StatisticsServicePort {

    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private final StatisticsRepositoryPort statisticsRepository;
    private final WinRecordRepositoryPort winRecordRepository;

    public StatisticsServiceImpl(StatisticsRepositoryPort statisticsRepository,
                                  WinRecordRepositoryPort winRecordRepository) {
        this.statisticsRepository = statisticsRepository;
        this.winRecordRepository = winRecordRepository;
    }

    @Override
    public List<Statistics> queryCampaignStatistics(Long campaignId) {
        log.info("查询活动统计 campaignId:{}", campaignId);
        return statisticsRepository.findByCampaignId(campaignId);
    }

    @Override
    public List<Statistics> queryUserStatistics(String userId) {
        log.info("查询用户统计 userId:{}", userId);
        return statisticsRepository.findByUserId(userId);
    }

    @Override
    public List<Statistics> queryPrizeStatistics(String prizeId) {
        log.info("查询奖品统计 prizeId:{}", prizeId);
        return statisticsRepository.findByPrizeId(prizeId);
    }

    @Override
    public List<Statistics> queryHotCampaigns(int limit) {
        log.info("查询热门活动 limit:{}", limit);
        return statisticsRepository.findHotCampaigns(limit);
    }

    @Override
    public List<WinRecord> queryUserWinRecords(String userId, int page, int size) {
        log.info("查询用户中奖记录 userId:{} page:{} size:{}", userId, page, size);
        return winRecordRepository.findByParticipantId(userId);
    }

    @Override
    public void recordDrawEvent(String userId, Long campaignId) {
        log.info("记录抽奖事件 userId:{} campaignId:{}", userId, campaignId);
        LocalDateTime today = LocalDate.now().atStartOfDay();

        statisticsRepository.incrementTotalCount(Statistics.DIMENSION_CAMPAIGN, String.valueOf(campaignId), campaignId, today);
        statisticsRepository.incrementTotalCount(Statistics.DIMENSION_USER, userId, campaignId, today);
        statisticsRepository.incrementTotalCount(Statistics.DIMENSION_DAILY, today.toString(), campaignId, today);
    }

    @Override
    public void recordWinEvent(String userId, Long campaignId, String prizeId) {
        log.info("记录中奖事件 userId:{} campaignId:{} prizeId:{}", userId, campaignId, prizeId);
        LocalDateTime today = LocalDate.now().atStartOfDay();

        statisticsRepository.incrementWinCount(Statistics.DIMENSION_CAMPAIGN, String.valueOf(campaignId), campaignId, today);
        statisticsRepository.incrementWinCount(Statistics.DIMENSION_USER, userId, campaignId, today);
        statisticsRepository.incrementWinCount(Statistics.DIMENSION_PRIZE, prizeId, campaignId, today);
    }

    @Override
    public void recordGrantEvent(String userId, Long campaignId, String prizeId) {
        log.info("记录发奖事件 userId:{} campaignId:{} prizeId:{}", userId, campaignId, prizeId);
        LocalDateTime today = LocalDate.now().atStartOfDay();

        statisticsRepository.incrementGrantCount(Statistics.DIMENSION_CAMPAIGN, String.valueOf(campaignId), campaignId, today);
        statisticsRepository.incrementGrantCount(Statistics.DIMENSION_USER, userId, campaignId, today);
        statisticsRepository.incrementGrantCount(Statistics.DIMENSION_PRIZE, prizeId, campaignId, today);
    }

    @Override
    public Statistics getCampaignSummary(Long campaignId) {
        log.info("获取活动汇总统计 campaignId:{}", campaignId);
        List<Statistics> stats = statisticsRepository.findByCampaignId(campaignId);
        
        Statistics summary = new Statistics();
        summary.setCampaignId(campaignId);
        summary.setDimensionType(Statistics.DIMENSION_CAMPAIGN);
        
        int totalDraws = 0;
        int totalWins = 0;
        int totalGrants = 0;
        
        for (Statistics stat : stats) {
            totalDraws += stat.getTotalCount() != null ? stat.getTotalCount() : 0;
            totalWins += stat.getWinCount() != null ? stat.getWinCount() : 0;
            totalGrants += stat.getGrantCount() != null ? stat.getGrantCount() : 0;
        }
        
        summary.setTotalCount(totalDraws);
        summary.setWinCount(totalWins);
        summary.setGrantCount(totalGrants);
        
        return summary;
    }

    @Override
    public Statistics getUserSummary(String userId) {
        log.info("获取用户汇总统计 userId:{}", userId);
        List<Statistics> stats = statisticsRepository.findByUserId(userId);
        
        Statistics summary = new Statistics();
        summary.setDimensionType(Statistics.DIMENSION_USER);
        summary.setDimensionValue(userId);
        
        int totalDraws = 0;
        int totalWins = 0;
        int totalGrants = 0;
        
        for (Statistics stat : stats) {
            totalDraws += stat.getTotalCount() != null ? stat.getTotalCount() : 0;
            totalWins += stat.getWinCount() != null ? stat.getWinCount() : 0;
            totalGrants += stat.getGrantCount() != null ? stat.getGrantCount() : 0;
        }
        
        summary.setTotalCount(totalDraws);
        summary.setWinCount(totalWins);
        summary.setGrantCount(totalGrants);
        
        return summary;
    }
}
