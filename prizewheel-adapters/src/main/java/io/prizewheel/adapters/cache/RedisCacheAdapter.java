package io.prizewheel.adapters.cache;

import io.prizewheel.core.port.output.CacheServicePort;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Component
public class RedisCacheAdapter implements CacheServicePort {

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
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
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
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
