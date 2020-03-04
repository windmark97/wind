package com.wind.manager.utils.lock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


/**
 * Redis锁实现
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/3/29 15:43
 **/
public class RedisTemplateDistributedLock  implements DistributedLock {


    /**
     * 获取锁默认等待超时毫秒时间
     */
    public static final long DEFAULT_WAIT_TIMEOUT_MILL = 3000;

    private String key;

    private RedisTemplate redisTemplate;

    /**
     * 有效时间
     */
    private long validTimeMill;

    /**
     * 获取锁等待超时毫秒时间，默认3秒
     */
    private long waitTimeoutMill;

    /**
     * @param key           锁唯一键
     * @param redisTemplate
     * @param validTime     锁的有效时间(大于0)(尽量长)。
     * @param timeUnit      时间单位
     */
    public RedisTemplateDistributedLock(String key, RedisTemplate redisTemplate, long validTime, TimeUnit timeUnit) {
        this(key, redisTemplate, validTime, timeUnit, DEFAULT_WAIT_TIMEOUT_MILL, TimeUnit.MILLISECONDS);
    }

    /**
     * @param key             锁唯一键
     * @param redisTemplate
     * @param validTime       锁的有效时间(大于0)(尽量长)。
     * @param validTimeUnit   锁的有效时间单位
     * @param waitTimeout     获取锁等待超时时间
     * @param waitTimeoutUnit 获取锁等待超时时间单位
     */
    public RedisTemplateDistributedLock(String key, RedisTemplate redisTemplate, long validTime, TimeUnit validTimeUnit, long waitTimeout, TimeUnit waitTimeoutUnit) {
        if (StringUtils.isEmpty(key)){
            throw new NullPointerException();
        }

        if (redisTemplate == null || validTimeUnit == null || waitTimeoutUnit == null){
            throw new NullPointerException();
        }

        if (validTime <= 0){

            throw new IllegalArgumentException("validTimeMill <= 0");
        }
        if (waitTimeout <= 0){

            throw new IllegalArgumentException("waitTimeout <= 0");
        }
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.validTimeMill = validTimeUnit.toMillis(validTime);

        this.waitTimeoutMill = waitTimeoutUnit.toMillis(waitTimeout);
    }

    private volatile byte flag = 0;

    private volatile long lockExpireTime;

    /**
     * @return
     * @throw IllegalMonitorStateException 如果锁已经存在
     */
    @Override
    public synchronized boolean tryLock() throws IllegalMonitorStateException {

        if (flag == 1) {
            throw new IllegalMonitorStateException();
        }
        long et = System.currentTimeMillis() + validTimeMill;
        String ok = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(et)) ? "OK" : "NO";
        if ("OK".equals(ok)) {
            redisTemplate.expire(key, validTimeMill, TimeUnit.MILLISECONDS);
            lockExpireTime = et;
            flag = 1;
            return true;
        }

        String v2 = (String) redisTemplate.opsForValue().get(key);
        if (v2 != null && Long.parseLong(v2) >= System.currentTimeMillis()) { //未过期
            return false;
        }

        et = System.currentTimeMillis() + validTimeMill;
        String v3 = (String) redisTemplate.opsForValue().getAndSet(key, String.valueOf(et));

        if (v2 == null) {
            if (v3 != null) {
                return false;
            }
        } else if (v3 != null && !v2.equals(v3)) {
            return false;
        }

        lockExpireTime = et;
        flag = 1;
        redisTemplate.expire(key, validTimeMill, TimeUnit.MILLISECONDS);

        return true;
    }

    @Override
    public boolean lock() {
        return this.lock(waitTimeoutMill, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit) {
        long waitTimeoutMill = timeUnit.toMillis(timeout);
        long c = System.currentTimeMillis();
        while (!this.tryLock()) {
            if (System.currentTimeMillis() - c >= waitTimeoutMill) {
                return false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }


    /**
     * 释放锁
     * @throw IllegalMonitorStateException 如果未获得锁或者锁已经过期
     */
    @Override
    public synchronized void unlock() throws IllegalMonitorStateException {
        if (flag != 1) {
            throw new IllegalMonitorStateException("no lock");
        }
        if (lockExpireTime >= System.currentTimeMillis()) {
            redisTemplate.delete(key);
        } else {
            throw new IllegalMonitorStateException("lock expired");
        }
        flag = 0;
    }
}
