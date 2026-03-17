package io.prizewheel.core.port.output;

/**
 * 缓存服务输出端口
 * 
 * 提供缓存操作和分布式锁功能
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface CacheServicePort {

    void set(String key, Object value);

    void set(String key, Object value, long expireSeconds);

    Object get(String key);

    <T> T get(String key, Class<T> type);

    boolean delete(String key);

    boolean hasKey(String key);

    long increment(String key);

    long increment(String key, long delta);

    long decrement(String key);

    boolean tryLock(String lockKey, long expireSeconds);

    boolean tryLock(String lockKey, long waitTime, long leaseTime);

    boolean tryLockWithWatchdog(String lockKey);

    void unlock(String lockKey);

    boolean isLocked(String lockKey);
}
