package com.wind.service.handler.strategy.impl;

import com.google.common.collect.Maps;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.ApiCallParam;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.manager.exception.AdvertException;
import com.wind.service.handler.AdvertDataHandler;
import com.wind.service.handler.AdvertStrategy;
import com.wind.service.util.ApiHttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * api的restful风格的调用通用策略，
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 19:05
 **/
@Slf4j
@Service
public abstract class BaseAdvertStrategy implements AdvertStrategy {
    private final static String EXECUTE_START_LOG = "start advert data query,userName:{}";
    private final static String EXECUTE_END_LOG = "end advert data query,userName:{}";
    /**
     * 执行
     * @param info
     * @param advertDataHandler 回调处理类
     */
    @Override
    public void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler) {
        log.info(EXECUTE_START_LOG, info.getUserName());
        try {
            ApiCallParam apiCallParam = new ApiCallParam();
            beforeEnhance(apiCallParam, info);
            buildApiCallParam(apiCallParam, info);
            info.setApiCallParam(apiCallParam);
            String resultStr = ApiHttpClientUtils.sendHttpPost(apiCallParam);
            doCallBack(advertDataHandler, info, resultStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException(e.getMessage());
        }
        log.info(EXECUTE_END_LOG, info.getUserName());
    }

    /**
     * 有返回的执行
     * @param info
     * @return
     */
    @Override
    public String execute(AdvertBaseInfo info) {
        log.info(EXECUTE_START_LOG, info.getUserName());
        String resultStr = null;
        try {
            ApiCallParam apiCallParam = new ApiCallParam();
            beforeEnhance(apiCallParam, info);
            buildApiCallParam(apiCallParam, info);
            info.setApiCallParam(apiCallParam);
            resultStr = ApiHttpClientUtils.sendHttpPost(apiCallParam);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException(e.getMessage());
        }
        log.info(EXECUTE_END_LOG, info.getUserName());
        return resultStr;
    }
    /**
     * 组装请求header
     *
     * @param info
     * @return
     */
    protected Map<String, Object> getHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", AdvertApiConsts.CONTENT_TYPE_JSON);
        return headerMap;
    }


    /**
     * 组装ApiCallParam请求对象
     *
     * @param apiCallParam
     * @param info
     */
    protected void buildApiCallParam(ApiCallParam apiCallParam, AdvertBaseInfo info) {
        apiCallParam.setUrl(info.getUrl());
        apiCallParam.setHeaderMap(getHeader(info));
        apiCallParam.setDataMap(buildData(info));
        apiCallParam.setEncodeFormat(AdvertApiConsts.ENCODING_FORMAT);
    }
    /**
     * 组装请求体
     * @param info
     * @return
     */
    protected Map<String, Object> buildData(AdvertBaseInfo info){
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("header", buildHeader(info));
        dataMap.put("body", buildBody(info));
        log.debug("advert api,userName:{},http body:{}", info.getUserName(), dataMap.toString());
        return dataMap;
    }


    /**
     * 请求体中的header数据
     *
     * @param info
     * @return
     */
    protected Map<String, Object> buildHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("username", info.getUserName());
        headerMap.put("password", info.getPassword());
        headerMap.put("token", info.getToken());
        return headerMap;
    }

    /**
     * 请求体中的body数据
     * @param info
     * @return
     */
    protected Map<String, Object> buildBody(AdvertBaseInfo info){
        return Maps.newHashMap();
    }


    /**
     * 执行回调方法
     *
     * @param advertDataHandler
     * @param resultStr
     */
    protected void doCallBack(AdvertDataHandler advertDataHandler, AdvertBaseInfo info, String resultStr) {
        if (advertDataHandler != null) {
            advertDataHandler.processResultJsonStr(resultStr, info);
        }
    }

    /**
     * 前置逻辑，子类增强处理
     * 如：设置url
     * @param apiCallParam
     * @param info
     */
    protected void beforeEnhance(ApiCallParam apiCallParam, AdvertBaseInfo info){
        log.debug("start advert api post,userName:{}",info.getUserName());
    }


    /**
     * 等待报告生成
     * @param time
     */
    protected void sleep(Integer time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
