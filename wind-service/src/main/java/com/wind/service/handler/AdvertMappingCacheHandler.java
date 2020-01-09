package com.wind.service.handler;

import com.wind.dao.model.AdvertArea;
import com.wind.dao.model.AdvertAreaQuery;
import com.wind.dao.model.AdvertCateg;
import com.wind.manager.utils.RedisManager;
import com.wind.service.service.interfaces.AdvertAreaService;
import com.wind.service.service.interfaces.AdvertCategService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务产品分类，映射id处理
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 14:45
 **/
@Slf4j
@Service
public class AdvertMappingCacheHandler {
    /**
     * 区域缓存key
     */
    private static final String ADVERT_AREA_NAME_CACHE_KEY = "advert_area_name_cache_table";

    /**
     * 服务分类缓存key
     */
    private static final String ADVERT_SEERV_CATEG_CACHE_KEY = "advert_serv_categ_cache_table";
    /**
     * 产品分类缓存key
     */
    private static final String ADVERT_PRODUCT_CATEG_CACHE_KEY = "advert_product_categ_cache_table";

    private static final String ADVERT_AREA_NAME_CACHE_LOCK = "advert_area_cache_lock";

    private static final String ADVERT_SEERV_CATEG_CACHE_LOCK = "advert_serv_categ_cache_lock";

    private static final String ADVERT_PRODUCT_CATEG_CACHE_LOCK = "advert_product_categ_cache_lock";

    /**
     * 缓存超时时间
     */
    private static final Integer CACHE_TIME_OUT_DATE = 1200;

    /**
     * splite
     */
    private static final String CITY_SPLITE = "-";


    public static final Integer ADVERT_SERV_CATEG_TYPE = 1;

    public static final Integer ADVERT_PRODUCT_CATEG_TYPE = 2;

    public static final String  DEFAULT_VALUE = "0";

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AdvertAreaService advertAreaService;

    @Autowired
    private AdvertCategService advertCategService;

    public String getAreaId(String areaName) {
        return getCache(areaName, ADVERT_AREA_NAME_CACHE_KEY, ADVERT_AREA_NAME_CACHE_LOCK);
    }

    /**
     * 通过名称获取服务分类id
     * @param servCategName
     * @return
     */
    public String getServCategId(String servCategName) {
        return getCache(servCategName, ADVERT_SEERV_CATEG_CACHE_KEY, ADVERT_SEERV_CATEG_CACHE_LOCK);
    }

    /**
     * 通过名称获取推广产品分类
     * @param productCategName
     * @return
     */
    public String getProductCategId(String productCategName) {
        return getCache(productCategName, ADVERT_PRODUCT_CATEG_CACHE_KEY, ADVERT_PRODUCT_CATEG_CACHE_LOCK);
    }

    /**
     * 获取服务分类中的所有数据
     * @return
     */
    public Map<Object, Object> getServCategMap() {
        boolean isExists = redisManager.exists(ADVERT_SEERV_CATEG_CACHE_KEY);
        if (!isExists) {
            doCacheData(ADVERT_SEERV_CATEG_CACHE_KEY, ADVERT_SEERV_CATEG_CACHE_LOCK);
        }

        return redisManager.hgetall(ADVERT_SEERV_CATEG_CACHE_KEY);
    }

    /**
     * 获取产品分类的所有数据
     * @return
     */
    public Map<Object, Object> getProductCategMap() {
        boolean isExists = redisManager.exists(ADVERT_PRODUCT_CATEG_CACHE_KEY);
        if (!isExists) {
            doCacheData(ADVERT_PRODUCT_CATEG_CACHE_KEY, ADVERT_PRODUCT_CATEG_CACHE_LOCK);
        }

        return redisManager.hgetall(ADVERT_PRODUCT_CATEG_CACHE_KEY);
    }

    /**
     * 获取缓存数据
     *
     * @param fieldName
     * @param cacheName
     * @param lockName
     * @return
     */
    private String getCache(String fieldName, String cacheName, String lockName) {
        try {
            return redisManager.hgetString(cacheName, fieldName);
        } catch (NullPointerException e) {
            boolean isExists = redisManager.exists(cacheName);
            if (!isExists) {
                //缓存
                doCacheData(cacheName, lockName);
                return redisManager.hgetString(cacheName, fieldName);
            }else{
                return DEFAULT_VALUE;
            }
        }
    }


    /**
     * 缓存数据
     */
    private void doCacheData(String cacheName, String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            boolean isExists = redisManager.exists(cacheName);
            if (isExists) {
                return;
            }
            if (ADVERT_AREA_NAME_CACHE_KEY.equals(cacheName)) {
                cacheAreaData();
            } else {
                cacheCategeData();
            }
        } catch (Exception e) {
            log.error("{} redis cache error !", cacheName);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将数据缓存到redis
     */
    private void cacheAreaData() {
        AdvertAreaQuery query = new AdvertAreaQuery();
        query.setLevel(2);
        List<AdvertArea> dataList = advertAreaService.listAll(query);
        if (dataList == null || dataList.isEmpty()) {
            log.info("area null data need cache!");
            return;
        }
        String cityIdStr;
        for (AdvertArea advertArea : dataList) {
            cityIdStr = advertArea.getAreaId() + CITY_SPLITE + advertArea.getParentId();
            redisManager.hset(ADVERT_AREA_NAME_CACHE_KEY, advertArea.getName(), cityIdStr);
        }
        redisManager.expire(ADVERT_AREA_NAME_CACHE_KEY, CACHE_TIME_OUT_DATE);
    }

    /**
     * 缓存分类数据
     */
    private void cacheCategeData() {
        List<AdvertCateg> dataList = advertCategService.listAll(new AdvertCateg());
        if (dataList == null || dataList.isEmpty()) {
            log.info("categ null data need cache!");
            return;
        }
        String categId;
        Integer type;
        for (AdvertCateg advertCateg : dataList) {
            type = advertCateg.getType();
            categId = advertCateg.getCategId().toString();
            if (ADVERT_SERV_CATEG_TYPE.equals(type)) {
                redisManager.hset(ADVERT_SEERV_CATEG_CACHE_KEY, advertCateg.getName(), categId);
            } else if (ADVERT_PRODUCT_CATEG_TYPE.equals(type)) {
                redisManager.hset(ADVERT_PRODUCT_CATEG_CACHE_KEY, advertCateg.getName(), categId);
            }
        }
        redisManager.expire(ADVERT_SEERV_CATEG_CACHE_KEY, CACHE_TIME_OUT_DATE);
        redisManager.expire(ADVERT_PRODUCT_CATEG_CACHE_KEY, CACHE_TIME_OUT_DATE);
    }

}
