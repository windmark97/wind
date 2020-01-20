package com.wind.service.handler.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.manager.exception.AdvertException;
import com.wind.service.handler.Advert360AccessTokenHandler;
import com.wind.service.handler.callback.AdvertDataHandler;
import com.wind.service.handler.strategy.Advert360ReportStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 360搜索推广，获取数据报告
 * 日报，粒度，城市。
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 10:42
 **/
@Slf4j
@Service("advert360ReportStrategy")
public class Advert360ReportStrategyImpl extends BaseAdvertStrategy implements Advert360ReportStrategy {


    @Autowired
    private Advert360AccessTokenHandler advert360AccessTokenHandler;

    @Override
    public void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler) {
        getAccessToken(info);
        info.setUrl(AdvertApiConsts.API_REPORT_TOTAL_360);
        String jsonStr = execute(info);
        JSONObject dataJson = JSON.parseObject(jsonStr);
        JSONArray failures = dataJson.getJSONArray("failures");
        if (failures != null) {
            log.info("360 http post failed!:{}", failures.toString());
            return;
        }
        Integer totalPage = dataJson.getInteger("totalPage");
        Integer totalNumber = dataJson.getInteger("totalNumber");
        if (totalPage == null || totalPage < 1) {
            totalPage = 1;
        }
        log.info("advert userName:{},totalPage:{},totalNumber:{}", info.getUserName(), totalPage, totalNumber);
        info.setUrl(AdvertApiConsts.API_REPORT_KEYWORD_360);
        for (int i = 1; i <= totalPage; i++) {
            info.setPageIndex(i);
            String resultJsonStr = execute(info);
            advertDataHandler.processResultJsonStr(resultJsonStr, info);
        }
    }

    protected void getAccessToken(AdvertBaseInfo info) {
        String accessToken = advert360AccessTokenHandler.getAccessToken(info);
        if (StringUtils.isEmpty(accessToken)) {
            log.info("failed get accessToken,userName:{} ", info.getUserName());
            throw new AdvertException("360 advert plat get accessToken failed!");
        }
        info.setAccessToken(accessToken);
    }

    @Override
    protected Map<String, Object> getHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", AdvertApiConsts.CONTENT_TYPE_FORM);
        headerMap.put("apiKey", info.getToken());
        headerMap.put("accessToken", info.getAccessToken());
        return headerMap;
    }

    @Override
    protected Map<String, Object> buildData(AdvertBaseInfo info) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("format", "JSON");
        dataMap.put("startDate", info.getStartDate());
        dataMap.put("endDate", info.getEndDate());
        dataMap.put("level", "account");
        if (AdvertApiConsts.API_REPORT_KEYWORD_360.equals(info.getUrl())) {
            dataMap.put("page", info.getPageIndex());
        }
        return dataMap;
    }
}
