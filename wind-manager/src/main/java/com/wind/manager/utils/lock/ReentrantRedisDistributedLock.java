package com.wind.manager.utils.lock;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 可重入锁
 * 线程不安全
 * @author: HuangYongJie
 * @version: v1.0
 * @since:  2019/3/2915:46
 **/
public class ReentrantRedisDistributedLock implements DistributedLock {

    private DistributedLock lock;
    private int count;


    public ReentrantRedisDistributedLock(String key, RedisTemplate redisTemplate, long validTime, TimeUnit timeUnit) {
        lock = new RedisTemplateDistributedLock(key, redisTemplate, validTime, timeUnit);
    }

    public ReentrantRedisDistributedLock(String key, RedisTemplate redisTemplate, long validTime, TimeUnit validTimeUnit, long waitTimeout, TimeUnit waitTimeoutUnit) {
        lock = new RedisTemplateDistributedLock(key, redisTemplate, validTime, validTimeUnit, waitTimeout, waitTimeoutUnit);
    }


    @Override
    public boolean tryLock() {
        if (count > 0) {
            count++;
            return true;
        }
        boolean flag = lock.tryLock();
        if (flag) {
            count++;
        }
        return flag;
    }

    @Override
    public boolean lock() {
        if (count > 0) {
            count++;
            return true;
        }
        boolean flag = lock.lock();
        if (flag) {
            count++;
        }
        return flag;
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit) {
        if (count > 0) {
            count++;
            return true;
        }
        boolean flag = lock.lock(timeout, timeUnit);
        if (flag) {
            count++;
        }
        return flag;
    }

    @Override
    public void unlock() {
        if (count == 1) {
            lock.unlock();
        }
        count--;
    }
}

