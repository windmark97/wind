package com.wind.service.handler;

import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;

/**
 * 搜索推广api处理类
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:10
 **/
public interface AdvertApiHandler {

    /**
     * 执行api
     * @param info
     */
    void execute(AdvertBaseInfo info);
}
