package io.prizewheel.adapters.cache;

import com.alibaba.fastjson.JSON;
import io.prizewheel.core.port.output.CacheServicePort;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务适配器
 * 
 * 功能特性：
 * - 支持分布式锁，默认等待时间5秒
 * - 支持锁自动续期（watchdog机制）
 * - 支持JSON序列化/反序列化解决类型转换问题
 * 
 * @author Allein
 * @since 1.0.0
 */
@Component
public class RedisCacheAdapter implements CacheServicePort {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheAdapter.class);
    
    private static final long DEFAULT_LOCK_WAIT_TIME = 5;
    private static final long DEFAULT_LOCK_LEASE_TIME = 30;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        
        try {
            String json = JSON.toJSONString(value);
            return JSON.parseObject(json, type);
        } catch (Exception e) {
            log.warn("缓存值类型转换失败 key:{} expectedType:{} actualType:{}", 
                    key, type.getName(), value.getClass().getName(), e);
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return result != null && result;
    }

    @Override
    public boolean hasKey(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return result != null && result;
    }

    @Override
    public long increment(String key) {
        Long result = redisTemplate.opsForValue().increment(key);
        return result != null ? result : 0L;
    }

    @Override
    public long increment(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0L;
    }

    @Override
    public long decrement(String key) {
        Long result = redisTemplate.opsForValue().decrement(key);
        return result != null ? result : 0L;
    }

    @Override
    public boolean tryLock(String lockKey, long expireSeconds) {
        return tryLock(lockKey, DEFAULT_LOCK_WAIT_TIME, expireSeconds > 0 ? expireSeconds : DEFAULT_LOCK_LEASE_TIME);
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (acquired) {
                log.debug("获取锁成功 lockKey:{} waitTime:{}s leaseTime:{}s", 
                        lockKey, waitTime, leaseTime);
            } else {
                log.warn("获取锁失败 lockKey:{} waitTime:{}s", lockKey, waitTime);
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("获取锁被中断 lockKey:{}", lockKey, e);
            return false;
        }
    }

    @Override
    public boolean tryLockWithWatchdog(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            log.debug("获取锁成功(自动续期) lockKey:{}", lockKey);
            return true;
        } catch (Exception e) {
            log.warn("获取锁失败 lockKey:{}", lockKey, e);
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.debug("释放锁成功 lockKey:{}", lockKey);
        }
    }
    
    @Override
    public boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isLocked();
    }
}
