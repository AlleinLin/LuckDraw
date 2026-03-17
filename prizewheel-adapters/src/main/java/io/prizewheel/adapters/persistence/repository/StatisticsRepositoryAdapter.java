package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.StatisticsPO;
import io.prizewheel.adapters.persistence.mapper.StatisticsMapper;
import io.prizewheel.core.domain.entity.Statistics;
import io.prizewheel.core.port.output.StatisticsRepositoryPort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class StatisticsRepositoryAdapter implements StatisticsRepositoryPort {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public Statistics findByDimension(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate) {
        LocalDate date = statDate != null ? statDate.toLocalDate() : LocalDate.now();
        StatisticsPO po = statisticsMapper.findByDimension(dimensionType, dimensionValue, campaignId, date);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Statistics> findByCampaignId(Long campaignId) {
        return statisticsMapper.findByCampaignId(campaignId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> findByUserId(String userId) {
        return statisticsMapper.findByUserId(userId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> findByPrizeId(String prizeId) {
        return statisticsMapper.findByPrizeId(prizeId).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> findHotCampaigns(int limit) {
        return statisticsMapper.findHotCampaigns(limit).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Statistics statistics) {
        statisticsMapper.insert(toPO(statistics));
    }

    @Override
    public void incrementTotalCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate) {
        LocalDate date = statDate != null ? statDate.toLocalDate() : LocalDate.now();
        statisticsMapper.incrementTotalCount(dimensionType, dimensionValue, campaignId, date);
    }

    @Override
    public void incrementWinCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate) {
        LocalDate date = statDate != null ? statDate.toLocalDate() : LocalDate.now();
        statisticsMapper.incrementWinCount(dimensionType, dimensionValue, campaignId, date);
    }

    @Override
    public void incrementGrantCount(String dimensionType, String dimensionValue, Long campaignId, LocalDateTime statDate) {
        LocalDate date = statDate != null ? statDate.toLocalDate() : LocalDate.now();
        statisticsMapper.incrementGrantCount(dimensionType, dimensionValue, campaignId, date);
    }

    private Statistics toEntity(StatisticsPO po) {
        Statistics entity = new Statistics();
        entity.setId(po.getId());
        entity.setDimensionType(po.getDimensionType());
        entity.setDimensionValue(po.getDimensionValue());
        entity.setCampaignId(po.getCampaignId());
        entity.setPrizeId(po.getPrizeId());
        entity.setTotalCount(po.getTotalCount());
        entity.setWinCount(po.getWinCount());
        entity.setGrantCount(po.getGrantCount());
        entity.setStatDate(po.getStatDate() != null ? po.getStatDate().atStartOfDay() : null);
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private StatisticsPO toPO(Statistics entity) {
        StatisticsPO po = new StatisticsPO();
        po.setId(entity.getId());
        po.setDimensionType(entity.getDimensionType());
        po.setDimensionValue(entity.getDimensionValue());
        po.setCampaignId(entity.getCampaignId());
        po.setPrizeId(entity.getPrizeId());
        po.setTotalCount(entity.getTotalCount());
        po.setWinCount(entity.getWinCount());
        po.setGrantCount(entity.getGrantCount());
        po.setStatDate(entity.getStatDate() != null ? entity.getStatDate().toLocalDate() : LocalDate.now());
        return po;
    }
}
