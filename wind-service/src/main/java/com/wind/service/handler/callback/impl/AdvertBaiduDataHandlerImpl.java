package com.wind.service.handler.callback.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zmn.advert.business.interfaces.advertapi.AdvertDataBService;
import com.zmn.advert.handler.AdvertMappingCacheHandler;
import com.zmn.advert.handler.callback.AdvertBaiduDataHandler;
import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;
import com.zmn.advert.model.entity.advertapi.SemReportDay;
import com.zmn.advert.services.interfaces.advertapi.AdvertHttpApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 搜索推广，百度返回结果处理
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:08
 **/
@Slf4j
@Service("advertBaiduDataHandler")
public class AdvertBaiduDataHandlerImpl extends BaseAdvertDataHandler implements AdvertBaiduDataHandler {

    @Autowired
    private AdvertDataBService advertDataBService;

    @Autowired
    private AdvertMappingCacheHandler advertMappingCacheHandler;

    @Autowired
    private AdvertHttpApiLogService advertHttpApiLogService;


    @Override
    protected void checkStatus(JSONObject resultJson) {
        JSONObject header = resultJson.getJSONObject("header");
        Integer status = header.getInteger("status");
        if (!status.equals(0)) {
            processFailedResult(header.toJSONString());
        }
    }

    @Override
    protected void processResult(JSONObject resultJson, AdvertBaseInfo info) {
        try {
            JSONObject body = resultJson.getJSONObject("body");
            JSONArray dataArr = body.getJSONArray("data");
            if (dataArr == null||dataArr.size() < 1) {
                log.info("userId:{}:data result is null",info.getUserId());
                return;
            }
            Map<Object, Object> servCategMap = advertMappingCacheHandler.getServCategMap();
            Map<Object, Object> productCategMap = advertMappingCacheHandler.getProductCategMap();
            List<SemReportDay> dataList = Lists.newArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject rowData = dataArr.getJSONObject(i);
                SemReportDay resultData = buildAdvert(rowData, info, servCategMap, productCategMap);
                if (!Objects.isNull(resultData)) {
                    dataList.add(resultData);
                }
            }
            resultParams(dataArr.getJSONObject(0), info);
            info.setSaveData(true);
            advertDataBService.modifyAdvertDataByquery(dataList);
        } catch (Exception e) {
            log.error("baidu api result failed,result:{}", resultJson.toJSONString());
            processFailedResult(e.getMessage());
        }
    }

    /**
     * 组装SemReportDay数据
     *
     * @param rowData
     * @param info
     * @param servCategMap
     * @param productCategMap
     * @return
     */
    private SemReportDay buildAdvert(JSONObject rowData, AdvertBaseInfo info, Map<Object, Object> servCategMap, Map<Object, Object> productCategMap) {
        try {
            if (rowData == null || rowData.isEmpty()) {
                return null;
            }
            SemReportDay resultData = new SemReportDay();
            JSONArray kpis = rowData.getJSONArray("kpis");
            JSONArray nameJsonData = rowData.getJSONArray("name");
            String dataStr = rowData.getString("date");
            resultData.setReportDate(parseDate(dataStr));
            resultData.setPublicizeDate(dataStr);
            resultData.setImpression(kpis.getInteger(0));
            resultData.setClick(kpis.getInteger(1));
            resultData.setCost(formatMoney(kpis.getBigDecimal(2),info.getReturnRate()));
            resultData.setCpc(formatMoney(kpis.getBigDecimal(3)));
            resultData.setCpm(formatMoney(kpis.getBigDecimal(4)));
            resultData.setCtr(kpis.getBigDecimal(5));
            resultData.setUserId(info.getUserId());
            resultData.setChannelId(info.getChannelId());
            if (nameJsonData != null) {
                processName(resultData,nameJsonData, servCategMap, productCategMap);
            }
            return resultData;
        } catch (Exception e) {
            log.error("error data:{}", rowData.toJSONString());
            log.error(e.getMessage(), e);
            advertHttpApiLogService.insertAdvertHttpApiLog(info, rowData.toJSONString());
        }
        return null;
    }

    /**
     * 处理名称
     *
     * @param nameJsonData
     * @param resultData
     * @param servCategMap
     * @param productCategMap
     */
    private void processName(SemReportDay resultData, JSONArray nameJsonData, Map<Object, Object> servCategMap, Map<Object, Object> productCategMap) {
        String proviceStr = nameJsonData.getString(1);
        processCityData(advertMappingCacheHandler,resultData, proviceStr);
        processCategDate(resultData, nameJsonData.getString(2), servCategMap, productCategMap);
    }





    /**
     * 返回参数
     *
     * @param rowData
     * @param info
     */
    private void resultParams(JSONObject rowData, AdvertBaseInfo info) {
        Integer totalRowNumber = rowData.getInteger("totalRowNumber");
        Integer pageIndex = rowData.getInteger("pageIndex");
        Map<String, Object> paramsMap;
        if (info.getParamsMap() == null) {
            paramsMap = new HashMap<>(16);
        } else {
            paramsMap = info.getParamsMap();
        }
        paramsMap.put("totalRowNumber", totalRowNumber);
        paramsMap.put("pageIndex", pageIndex);
        info.setParamsMap(paramsMap);
    }

}
