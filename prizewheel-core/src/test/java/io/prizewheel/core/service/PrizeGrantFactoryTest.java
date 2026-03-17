package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.PointsServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 奖品发放策略工厂单元测试
 * 
 * @author Allein
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class PrizeGrantFactoryTest {

    @Mock
    private PointsServicePort pointsService;

    private PrizeGrantFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PrizeGrantFactory(pointsService);
    }

    @Test
    @DisplayName("发放虚拟奖品 - 成功")
    void testGrant_VirtualPrize_Success() {
        WinRecord record = createWinRecord(1, "虚拟奖品内容");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    @Test
    @DisplayName("发放优惠券 - 成功")
    void testGrant_CouponPrize_Success() {
        WinRecord record = createWinRecord(2, "COUPON123");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    @Test
    @DisplayName("发放实物奖品 - 成功")
    void testGrant_PhysicalPrize_Success() {
        WinRecord record = createWinRecord(3, "iPhone 15 Pro");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    @Test
    @DisplayName("发放积分奖品 - 成功")
    void testGrant_PointsPrize_Success() {
        WinRecord record = createWinRecord(4, "100");
        when(pointsService.addPoints(anyString(), anyInt(), anyString(), anyString(), anyLong()))
                .thenReturn(true);

        boolean result = factory.grant(record);

        assertTrue(result);
        verify(pointsService).addPoints(
                eq("user001"),
                eq(100),
                eq(String.valueOf(PointsRecord.SOURCE_DRAW_WIN)),
                eq("抽奖获得"),
                eq(1001L)
        );
    }

    @Test
    @DisplayName("发放积分奖品 - 积分数量解析失败")
    void testGrant_PointsPrize_ParseError() {
        WinRecord record = createWinRecord(4, "invalid_number");

        boolean result = factory.grant(record);

        assertFalse(result);
        verify(pointsService, never()).addPoints(anyString(), anyInt(), anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("发放积分奖品 - PointsService未注入")
    void testGrant_PointsPrize_NoPointsService() {
        PrizeGrantFactory factoryWithoutPointsService = new PrizeGrantFactory();
        WinRecord record = createWinRecord(4, "100");

        boolean result = factoryWithoutPointsService.grant(record);

        assertFalse(result);
    }

    @Test
    @DisplayName("发放现金红包 - 成功")
    void testGrant_CashPrize_Success() {
        WinRecord record = createWinRecord(5, "10.00");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    @Test
    @DisplayName("发放奖品 - 记录为空")
    void testGrant_NullRecord() {
        boolean result = factory.grant(null);

        assertFalse(result);
    }

    @Test
    @DisplayName("发放奖品 - 奖品类型为空")
    void testGrant_NullPrizeType() {
        WinRecord record = createWinRecord(null, "奖品内容");

        boolean result = factory.grant(record);

        assertFalse(result);
    }

    @Test
    @DisplayName("发放奖品 - 未知奖品类型使用默认策略")
    void testGrant_UnknownPrizeType() {
        WinRecord record = createWinRecord(999, "未知奖品");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    @Test
    @DisplayName("注册自定义策略")
    void testRegisterStrategy() {
        PrizeGrantFactory.PrizeGrantStrategy customStrategy = record -> {
            return true;
        };
        
        factory.registerStrategy(100, customStrategy);
        WinRecord record = createWinRecord(100, "自定义奖品");

        boolean result = factory.grant(record);

        assertTrue(result);
    }

    private WinRecord createWinRecord(Integer prizeType, String prizeContent) {
        WinRecord record = new WinRecord();
        record.setRecordId(1001L);
        record.setParticipantId("user001");
        record.setCampaignId(2001L);
        record.setPolicyId(1001L);
        record.setPrizeId("P001");
        record.setPrizeName("测试奖品");
        record.setPrizeType(prizeType);
        record.setPrizeContent(prizeContent);
        record.setStatus(1);
        record.setWinTime(LocalDateTime.now());
        return record;
    }
}
