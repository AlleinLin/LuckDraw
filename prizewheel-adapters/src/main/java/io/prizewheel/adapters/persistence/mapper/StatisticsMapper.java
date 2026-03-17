package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.StatisticsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计Mapper接口
 * 
 * @author Allein
 * @since 2.0.0
 */
@Mapper
public interface StatisticsMapper {

    StatisticsPO findByDimension(@Param("dimensionType") String dimensionType,
                                  @Param("dimensionValue") String dimensionValue,
                                  @Param("campaignId") Long campaignId,
                                  @Param("statDate") LocalDate statDate);

    List<StatisticsPO> findByCampaignId(@Param("campaignId") Long campaignId);

    List<StatisticsPO> findByUserId(@Param("userId") String userId);

    List<StatisticsPO> findByPrizeId(@Param("prizeId") String prizeId);

    List<StatisticsPO> findHotCampaigns(@Param("limit") int limit);

    int insert(StatisticsPO statistics);

    int update(StatisticsPO statistics);

    int incrementTotalCount(@Param("dimensionType") String dimensionType,
                            @Param("dimensionValue") String dimensionValue,
                            @Param("campaignId") Long campaignId,
                            @Param("statDate") LocalDate statDate);

    int incrementWinCount(@Param("dimensionType") String dimensionType,
                          @Param("dimensionValue") String dimensionValue,
                          @Param("campaignId") Long campaignId,
                          @Param("statDate") LocalDate statDate);

    int incrementGrantCount(@Param("dimensionType") String dimensionType,
                            @Param("dimensionValue") String dimensionValue,
                            @Param("campaignId") Long campaignId,
                            @Param("statDate") LocalDate statDate);
}
