package com.wind.service.handler.callback.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wind.service.handler.AdvertMappingCacheHandler;
import com.wind.service.handler.callback.Advert360DataHandler;
import com.wind.service.service.interfaces.AdvertHttpApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 360搜索推广返回数据处理类
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:08
 **/
@Slf4j
@Service("advert360DataHandler")
public class Advert360DataHandlerImpl extends BaseAdvertDataHandler implements Advert360DataHandler {

    @Autowired
    private AdvertMappingCacheHandler advertMappingCacheHandler;

    @Autowired
    private AdvertDataBService advertDataBService;

    @Autowired
    private AdvertHttpApiLogService advertHttpApiLogService;

    @Override
    protected void checkStatus(JSONObject jsonObject) {
        JSONArray failures = jsonObject.getJSONArray("failures");
        if (failures != null) {
            processFailedResult(failures.toString());
        }
    }

    @Override
    protected void processResult(JSONObject resultJson, AdvertBaseInfo info) {
        JSONArray dataArr = resultJson.getJSONArray("groupList");
        if (dataArr == null || dataArr.size() < 1) {
            log.info("userName:{}:data result is null", info.getUserName());
            return;
        }
        List<SemReportDay> dataList = Lists.newArrayList();

        Map<Object, Object> servCategMap = advertMappingCacheHandler.getServCategMap();
        Map<Object, Object> productCategMap = advertMappingCacheHandler.getProductCategMap();
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject rowData = dataArr.getJSONObject(i);
            SemReportDay resultData = buildAdvert(rowData, info);
            if (resultData != null) {
                resultData.setUserId(info.getUserId());
                resultData.setChannelId(info.getChannelId());
                String groupName = rowData.getString("groupName");
                processCategDate(resultData, groupName, servCategMap, productCategMap);
                dataList.add(resultData);
            }
        }
        info.setSaveData(true);
        advertDataBService.modifyAdvertDataByquery(dataList);
    }

    private SemReportDay buildAdvert(JSONObject rowData, AdvertBaseInfo info) {
        try {
            SemReportDay resultData = new SemReportDay();
            resultData.setImpression(rowData.getInteger("views"));
            resultData.setClick(rowData.getInteger("clicks"));
            resultData.setCost(formatMoney(rowData.getBigDecimal("totalCost"),info.getReturnRate()));
            String dateStr = rowData.getString("date");
            resultData.setReportDate(parseDate(dateStr));
            resultData.setPublicizeDate(dateStr);
            String campaignName = rowData.getString("campaignName");
            processCityData(advertMappingCacheHandler, resultData, campaignName);
            resultData.setCpc(formatMoney(BigDecimal.valueOf(0)));
            resultData.setCpm(formatMoney(BigDecimal.valueOf(0)));
            resultData.setCtr(BigDecimal.valueOf(0));
            return resultData;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            advertHttpApiLogService.insertAdvertHttpApiLog(info, e.getMessage() + rowData.toJSONString());
        }
        return null;
    }


}
