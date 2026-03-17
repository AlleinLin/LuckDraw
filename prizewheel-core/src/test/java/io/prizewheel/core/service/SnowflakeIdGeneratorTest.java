package io.prizewheel.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 雪花算法ID生成器单元测试
 * 
 * @author Allein
 * @since 1.0.0
 */
class SnowflakeIdGeneratorTest {

    private SnowflakeIdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new SnowflakeIdGenerator(1, 1);
    }

    @Test
    @DisplayName("生成单个ID - 成功")
    void testNextId_Success() {
        Long id = idGenerator.nextId();

        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("生成多个ID - 唯一性验证")
    void testNextId_Uniqueness() {
        Set<Long> ids = new HashSet<>();
        int count = 10000;

        for (int i = 0; i < count; i++) {
            Long id = idGenerator.nextId();
            assertNotNull(id);
            ids.add(id);
        }

        assertEquals(count, ids.size(), "所有生成的ID应该唯一");
    }

    @Test
    @DisplayName("批量生成ID - 成功")
    void testNextIds_Success() {
        int count = 100;
        List<Long> ids = idGenerator.nextIds(count);

        assertNotNull(ids);
        assertEquals(count, ids.size());
    }

    @Test
    @DisplayName("批量生成ID - 唯一性验证")
    void testNextIds_Uniqueness() {
        List<Long> ids = idGenerator.nextIds(1000);
        Set<Long> uniqueIds = new HashSet<>(ids);

        assertEquals(1000, uniqueIds.size(), "批量生成的ID应该全部唯一");
    }

    @Test
    @DisplayName("不同Worker生成不同ID")
    void testDifferentWorkers() {
        SnowflakeIdGenerator generator1 = new SnowflakeIdGenerator(1, 1);
        SnowflakeIdGenerator generator2 = new SnowflakeIdGenerator(2, 1);

        Long id1 = generator1.nextId();
        Long id2 = generator2.nextId();

        assertNotEquals(id1, id2, "不同Worker生成的ID应该不同");
    }

    @Test
    @DisplayName("不同Datacenter生成不同ID")
    void testDifferentDatacenters() {
        SnowflakeIdGenerator generator1 = new SnowflakeIdGenerator(1, 1);
        SnowflakeIdGenerator generator2 = new SnowflakeIdGenerator(1, 2);

        Long id1 = generator1.nextId();
        Long id2 = generator2.nextId();

        assertNotEquals(id1, id2, "不同Datacenter生成的ID应该不同");
    }

    @Test
    @DisplayName("Worker ID超出范围 - 抛出异常")
    void testInvalidWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(32, 1);
        });
    }

    @Test
    @DisplayName("Datacenter ID超出范围 - 抛出异常")
    void testInvalidDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(1, 32);
        });
    }

    @Test
    @DisplayName("Worker ID为负数 - 抛出异常")
    void testNegativeWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(-1, 1);
        });
    }

    @Test
    @DisplayName("Datacenter ID为负数 - 抛出异常")
    void testNegativeDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(1, -1);
        });
    }

    @Test
    @DisplayName("ID生成性能测试")
    void testPerformance() {
        int count = 100000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            idGenerator.nextId();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 5000, 
                String.format("生成%d个ID耗时%dms，应该在5秒内完成", count, duration));
    }
}
