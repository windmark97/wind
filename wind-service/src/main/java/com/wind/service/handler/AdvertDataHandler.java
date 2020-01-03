package com.wind.service.handler;


import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;

import java.io.ByteArrayInputStream;

/**
 * 搜索推广平台，数据处理接口
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:05
 **/
public interface AdvertDataHandler {

    /**
     * 数据处理入口
     * @param jsonStr api返回的json字符串
     * @param info
     */
    void processResultJsonStr(String jsonStr, AdvertBaseInfo info);

    /**
     * 二进制流数据处理
     * @param inputStream 二进制流
     * @param info
     */
    void processResultFile(ByteArrayInputStream inputStream, AdvertBaseInfo info);
}
