package io.prizewheel.api.facade;

import io.prizewheel.api.dto.StatisticsRequest;
import io.prizewheel.api.dto.StatisticsResponse;
import io.prizewheel.api.dto.WinRecordResponse;
import io.prizewheel.core.domain.entity.Statistics;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.StatisticsServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计服务门面
 * 
 * @author Allein
 * @since 2.0.0
 */
@DubboService(version = "1.0.0")
public class StatisticsFacade {

    private final StatisticsServicePort statisticsService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatisticsFacade(StatisticsServicePort statisticsService) {
        this.statisticsService = statisticsService;
    }

    public ApiResult<List<StatisticsResponse>> queryCampaignStatistics(Long campaignId) {
        try {
            List<Statistics> statistics = statisticsService.queryCampaignStatistics(campaignId);
            List<StatisticsResponse> responses = statistics.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询活动统计失败: " + e.getMessage());
        }
    }

    public ApiResult<List<StatisticsResponse>> queryUserStatistics(String userId) {
        try {
            List<Statistics> statistics = statisticsService.queryUserStatistics(userId);
            List<StatisticsResponse> responses = statistics.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询用户统计失败: " + e.getMessage());
        }
    }

    public ApiResult<List<StatisticsResponse>> queryHotCampaigns(int limit) {
        try {
            List<Statistics> statistics = statisticsService.queryHotCampaigns(limit);
            List<StatisticsResponse> responses = statistics.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询热门活动失败: " + e.getMessage());
        }
    }

    public ApiResult<List<WinRecordResponse>> queryUserWinRecords(String userId, int page, int size) {
        try {
            List<WinRecord> records = statisticsService.queryUserWinRecords(userId, page, size);
            List<WinRecordResponse> responses = records.stream()
                    .map(this::toWinRecordResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询用户中奖记录失败: " + e.getMessage());
        }
    }

    public ApiResult<StatisticsResponse> getCampaignSummary(Long campaignId) {
        try {
            Statistics summary = statisticsService.getCampaignSummary(campaignId);
            return ApiResult.success(toResponse(summary));
        } catch (Exception e) {
            return ApiResult.fail("获取活动汇总失败: " + e.getMessage());
        }
    }

    public ApiResult<StatisticsResponse> getUserSummary(String userId) {
        try {
            Statistics summary = statisticsService.getUserSummary(userId);
            return ApiResult.success(toResponse(summary));
        } catch (Exception e) {
            return ApiResult.fail("获取用户汇总失败: " + e.getMessage());
        }
    }

    private StatisticsResponse toResponse(Statistics statistics) {
        StatisticsResponse response = new StatisticsResponse();
        response.setDimensionType(statistics.getDimensionType());
        response.setDimensionValue(statistics.getDimensionValue());
        response.setCampaignId(statistics.getCampaignId());
        response.setPrizeId(statistics.getPrizeId());
        response.setTotalCount(statistics.getTotalCount());
        response.setWinCount(statistics.getWinCount());
        response.setGrantCount(statistics.getGrantCount());
        response.setWinRate(statistics.getWinRate());
        response.setGrantRate(statistics.getGrantRate());
        return response;
    }

    private WinRecordResponse toWinRecordResponse(WinRecord record) {
        WinRecordResponse response = new WinRecordResponse();
        response.setRecordId(record.getRecordId());
        response.setParticipantId(record.getParticipantId());
        response.setCampaignId(record.getCampaignId());
        response.setPolicyId(record.getPolicyId());
        response.setPrizeId(record.getPrizeId());
        response.setPrizeName(record.getPrizeName());
        response.setPrizeType(record.getPrizeType());
        response.setPrizeContent(record.getPrizeContent());
        response.setStatus(record.getStatus());
        if (record.getWinTime() != null) {
            response.setWinTime(record.getWinTime().format(FORMATTER));
        }
        if (record.getGrantTime() != null) {
            response.setGrantTime(record.getGrantTime().format(FORMATTER));
        }
        return response;
    }
}
