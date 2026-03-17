package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;
import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.port.output.PointsRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 积分服务单元测试
 * 
 * @author Allein
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class PointsServiceImplTest {

    @Mock
    private PointsRepositoryPort pointsRepository;

    @Mock
    private IdGeneratorPort idGenerator;

    private PointsServiceImpl pointsService;

    @BeforeEach
    void setUp() {
        pointsService = new PointsServiceImpl(pointsRepository, idGenerator);
    }

    @Test
    @DisplayName("查询用户积分 - 新用户自动创建")
    void testQueryUserPoints_NewUser() {
        when(pointsRepository.findByUserId("user001")).thenReturn(null);
        when(idGenerator.nextId()).thenReturn(1001L);

        UserPoints result = pointsService.queryUserPoints("user001");

        assertNotNull(result);
        assertEquals("user001", result.getUserId());
        assertEquals(0, result.getTotalPoints());
        verify(pointsRepository).save(any(UserPoints.class));
    }

    @Test
    @DisplayName("查询用户积分 - 已存在用户")
    void testQueryUserPoints_ExistingUser() {
        UserPoints userPoints = createTestUserPoints();
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);

        UserPoints result = pointsService.queryUserPoints("user001");

        assertNotNull(result);
        assertEquals("user001", result.getUserId());
        assertEquals(1000, result.getTotalPoints());
    }

    @Test
    @DisplayName("增加积分 - 成功")
    void testAddPoints_Success() {
        UserPoints userPoints = createTestUserPoints();
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);
        when(idGenerator.nextId()).thenReturn(1001L);

        boolean result = pointsService.addPoints("user001", 100, "1", "测试增加", null);

        assertTrue(result);
        verify(pointsRepository).save(any(UserPoints.class));
        verify(pointsRepository).saveRecord(any(PointsRecord.class));
    }

    @Test
    @DisplayName("增加积分 - 无效积分数量")
    void testAddPoints_InvalidPoints() {
        boolean result = pointsService.addPoints("user001", 0, "1", "测试", null);

        assertFalse(result);
        verify(pointsRepository, never()).save(any());
    }

    @Test
    @DisplayName("扣减积分 - 成功")
    void testDeductPoints_Success() {
        UserPoints userPoints = createTestUserPoints();
        userPoints.setCurrentPoints(500);
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);
        when(idGenerator.nextId()).thenReturn(1001L);

        boolean result = pointsService.deductPoints("user001", 100, "3", "兑换商品", null);

        assertTrue(result);
        verify(pointsRepository).save(any(UserPoints.class));
        verify(pointsRepository).saveRecord(any(PointsRecord.class));
    }

    @Test
    @DisplayName("扣减积分 - 积分不足")
    void testDeductPoints_InsufficientPoints() {
        UserPoints userPoints = createTestUserPoints();
        userPoints.setCurrentPoints(50);
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);

        boolean result = pointsService.deductPoints("user001", 100, "3", "兑换商品", null);

        assertFalse(result);
        verify(pointsRepository, never()).save(any());
    }

    @Test
    @DisplayName("签到 - 成功")
    void testSignIn_Success() {
        UserPoints userPoints = createTestUserPoints();
        userPoints.setLastSignInTime(LocalDateTime.now().minusDays(1));
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);
        when(idGenerator.nextId()).thenReturn(1001L);

        boolean result = pointsService.signIn("user001");

        assertTrue(result);
        verify(pointsRepository).save(any(UserPoints.class));
    }

    @Test
    @DisplayName("签到 - 今日已签到")
    void testSignIn_AlreadySignedIn() {
        UserPoints userPoints = createTestUserPoints();
        userPoints.setLastSignInTime(LocalDateTime.now());
        when(pointsRepository.findByUserId("user001")).thenReturn(userPoints);

        boolean result = pointsService.signIn("user001");

        assertFalse(result);
    }

    @Test
    @DisplayName("计算等级 - 正确计算")
    void testCalculateLevel() {
        assertEquals(1, pointsService.calculateLevel(0));
        assertEquals(2, pointsService.calculateLevel(100));
        assertEquals(3, pointsService.calculateLevel(500));
        assertEquals(4, pointsService.calculateLevel(1000));
        assertEquals(5, pointsService.calculateLevel(5000));
        assertEquals(6, pointsService.calculateLevel(10000));
        assertEquals(7, pointsService.calculateLevel(50000));
        assertEquals(8, pointsService.calculateLevel(100000));
    }

    @Test
    @DisplayName("获取等级名称 - 正确获取")
    void testGetLevelName() {
        assertEquals("新手", pointsService.getLevelName(1));
        assertEquals("初级", pointsService.getLevelName(2));
        assertEquals("中级", pointsService.getLevelName(3));
        assertEquals("高级", pointsService.getLevelName(4));
        assertEquals("专家", pointsService.getLevelName(5));
        assertEquals("大师", pointsService.getLevelName(6));
        assertEquals("宗师", pointsService.getLevelName(7));
        assertEquals("传奇", pointsService.getLevelName(8));
    }

    private UserPoints createTestUserPoints() {
        UserPoints userPoints = new UserPoints();
        userPoints.setUserId("user001");
        userPoints.setTotalPoints(1000);
        userPoints.setUsedPoints(0);
        userPoints.setCurrentPoints(1000);
        userPoints.setUserLevel(4);
        userPoints.setContinuousSignInDays(5);
        userPoints.setCreatedAt(LocalDateTime.now());
        userPoints.setUpdatedAt(LocalDateTime.now());
        return userPoints;
    }
}
