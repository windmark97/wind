package com.wind.manager.utils.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 自定义锁工具类
 * @author HuangYongJie
 * @version v1.0
 * @since 2019/3/29 17:16
 **/
@Component
public class DistributedLockUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static int DefaultValidTime = 5;
    private final static TimeUnit DefaultTimeUnit = TimeUnit.SECONDS;


    public static DistributedLockUtils instance;

    @PostConstruct
    void init() {
        DistributedLockUtils.instance = this;
    }

    /**
     * 新建一个全局锁，锁时间: 默认五秒
     *
     * @param key
     * @return
     */
    public static DistributedLock build(String key) {

        return new RedisTemplateDistributedLock(key, instance.redisTemplate, DefaultValidTime, DefaultTimeUnit);
    }

    /**
     * @param key       全局唯一KEY
     * @param validTime 过期时间(秒)
     * @return 锁
     */
    public static DistributedLock build(String key, int validTime) {

        return new RedisTemplateDistributedLock(key, instance.redisTemplate, validTime, DefaultTimeUnit);
    }

    /**
     * @param key       全局唯一KEY
     * @param validTime 过期时间(秒)
     * @param waitTime  获取锁等待时间时间(秒)
     * @return 锁
     */
    public static DistributedLock build(String key, int validTime, int waitTime) {

        return new RedisTemplateDistributedLock(key, instance.redisTemplate, validTime, DefaultTimeUnit, waitTime, DefaultTimeUnit);
    }


    /**
     * @param key       全局唯一KEY
     * @param validTime 过期时间(秒)
     * @return 可重入锁
     */
    public static ReentrantRedisDistributedLock buildReentrantLock(String key, int validTime) {

        return new ReentrantRedisDistributedLock(key, instance.redisTemplate, validTime, DefaultTimeUnit);
    }

    /**
     * @param key       全局唯一KEY
     * @param validTime 过期时间(秒)
     * @param waitTime  获取锁等待时间(秒)
     * @return 可重入锁
     */
    public static ReentrantRedisDistributedLock buildReentrantLock(String key, int validTime, int waitTime) {

        return new ReentrantRedisDistributedLock(key, instance.redisTemplate, validTime, DefaultTimeUnit, waitTime, DefaultTimeUnit);
    }


}
