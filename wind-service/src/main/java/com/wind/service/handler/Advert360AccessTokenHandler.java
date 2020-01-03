package com.wind.service.handler;

import com.zmn.advert.handler.strategy.Advert360ClientLoginStrategy;
import com.zmn.advert.manager.exception.AdvertException;
import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;
import com.zmn.manager.common.interfaces.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 360的登录accessToken的管理类
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 15:41
 **/
@Slf4j
@Component
public class Advert360AccessTokenHandler {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private Advert360ClientLoginStrategy advert360ClientLoginStrategy;

    private static final String ACCESS_NAME_PREFIX = "api-360:";
    private static final String ACCESS_LOCK_SUFFIX = "-lock";

    /**
     * accessToken超时时间
     * 360的accessToken失效时间是10个小时，则本地失效时间少10秒钟
     */
    private static final Integer TIME_OUT_DATE = 35990;


    /**
     * 360获取临时token
     *
     * @param info
     * @return accessToken
     */
    public String getAccessToken(AdvertBaseInfo info) {
        String key = ACCESS_NAME_PREFIX + info.getUserName();
        boolean isExistKey = redisManager.exists(key);
        if (isExistKey) {
            log.info("获取accessToken！");
            return redisManager.get(key);
        }
        String accessToken = null;
        String lockey = key + ACCESS_LOCK_SUFFIX;
        RLock lock = redissonClient.getLock(lockey);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            boolean isNotExist = !redisManager.exists(key);
            if (isNotExist) {
                //执行360登录，返回accessToken，set到redis
                clientLogin(info);
            }
            accessToken = redisManager.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException("获取 accessToken 失败！");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return accessToken;
    }

    /**
     * 执行360登录操作
     *
     * @param info
     */
    private void clientLogin(AdvertBaseInfo info) {
        //同步获取accessToken
        String accessToken = advert360ClientLoginStrategy.clientLogin(info);
        setAccessTokenMap(info.getUserName(), accessToken);
    }

    /**
     * 设置360临时token
     *
     * @param userName
     * @param accessToken
     */
    private void setAccessTokenMap(String userName, String accessToken) {
        String key = ACCESS_NAME_PREFIX + userName;
        String lockey = key + ACCESS_LOCK_SUFFIX;
        redisManager.setex(key, accessToken, TIME_OUT_DATE);
        redisManager.del(lockey);
    }


}
