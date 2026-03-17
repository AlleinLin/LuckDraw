package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.PointsServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 奖品发放策略工厂
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PrizeGrantFactory {

    private static final Logger log = LoggerFactory.getLogger(PrizeGrantFactory.class);

    private final Map<Integer, PrizeGrantStrategy> strategies = new HashMap<>();

    public PrizeGrantFactory() {
        registerDefaultStrategies();
    }

    private void registerDefaultStrategies() {
        registerStrategy(1, new VirtualPrizeStrategy());
        registerStrategy(2, new CouponPrizeStrategy());
        registerStrategy(3, new PhysicalPrizeStrategy());
        registerStrategy(4, new PointsPrizeStrategy());
        registerStrategy(5, new CashPrizeStrategy());
    }

    public void registerStrategy(int prizeType, PrizeGrantStrategy strategy) {
        strategies.put(prizeType, strategy);
        log.info("注册奖品发放策略 prizeType:{} strategy:{}", prizeType, strategy.getClass().getSimpleName());
    }

    public PrizeGrantStrategy getStrategy(int prizeType) {
        PrizeGrantStrategy strategy = strategies.get(prizeType);
        if (strategy == null) {
            log.warn("未找到奖品发放策略 prizeType:{} 使用默认策略", prizeType);
            return strategies.get(1);
        }
        return strategy;
    }

    public boolean grant(WinRecord record) {
        if (record == null || record.getPrizeType() == null) {
            log.warn("中奖记录无效");
            return false;
        }

        PrizeGrantStrategy strategy = getStrategy(record.getPrizeType());
        return strategy.grant(record);
    }

    public interface PrizeGrantStrategy {
        boolean grant(WinRecord record);
    }

    public static class VirtualPrizeStrategy implements PrizeGrantStrategy {
        @Override
        public boolean grant(WinRecord record) {
            log.info("发放虚拟奖品 recordId:{} prizeContent:{}", record.getRecordId(), record.getPrizeContent());
            return true;
        }
    }

    public static class CouponPrizeStrategy implements PrizeGrantStrategy {
        @Override
        public boolean grant(WinRecord record) {
            log.info("发放优惠券 recordId:{} prizeContent:{}", record.getRecordId(), record.getPrizeContent());
            return true;
        }
    }

    public static class PhysicalPrizeStrategy implements PrizeGrantStrategy {
        @Override
        public boolean grant(WinRecord record) {
            log.info("发放实物奖品 recordId:{} prizeContent:{}", record.getRecordId(), record.getPrizeContent());
            return true;
        }
    }

    public static class PointsPrizeStrategy implements PrizeGrantStrategy {

        private PointsServicePort pointsService;

        public void setPointsService(PointsServicePort pointsService) {
            this.pointsService = pointsService;
        }

        @Override
        public boolean grant(WinRecord record) {
            log.info("发放积分奖品 recordId:{} prizeContent:{}", record.getRecordId(), record.getPrizeContent());
            if (pointsService != null) {
                try {
                    int points = Integer.parseInt(record.getPrizeContent());
                    return pointsService.addPoints(record.getParticipantId(), points, 
                            String.valueOf(2), "抽奖获得", record.getRecordId());
                } catch (NumberFormatException e) {
                    log.error("积分数量解析失败 prizeContent:{}", record.getPrizeContent());
                    return false;
                }
            }
            return true;
        }
    }

    public static class CashPrizeStrategy implements PrizeGrantStrategy {
        @Override
        public boolean grant(WinRecord record) {
            log.info("发放现金红包 recordId:{} prizeContent:{}", record.getRecordId(), record.getPrizeContent());
            return true;
        }
    }
}
