package com.wind.service.handler.callback.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.SemReportDay;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.manager.exception.AdvertException;
import com.wind.service.business.interfaces.advertapi.AdvertDataBService;
import com.wind.service.handler.AdvertMappingCacheHandler;
import com.wind.service.handler.callback.AdvertShenmaDataHandler;
import com.wind.service.service.interfaces.AdvertHttpApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 神马搜索推广api返回数据处理类
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:42
 **/
@Slf4j
@Service("advertShenmaDataHandler")
public class AdvertShenmaDataHandlerImpl extends BaseAdvertDataHandler implements AdvertShenmaDataHandler {

    @Autowired
    private AdvertMappingCacheHandler advertMappingCacheHandler;

    @Autowired
    private AdvertDataBService advertDataBService;

    @Autowired
    private AdvertHttpApiLogService advertHttpApiLogService;
    @Override
    public void processResultJsonStr(String jsonStr, AdvertBaseInfo info) {
        JSONObject resultJson = JSON.parseObject(jsonStr);
        JSONObject header = resultJson.getJSONObject("header");
        if (Objects.isNull(header)) {
            throw new AdvertException("shenma http post result is null");
        }
        Integer status = header.getInteger(AdvertApiConsts.PARAM_STATUS_NAME);
        //请求失败
        if (!AdvertApiConsts.API_HTTP_SUCCESS_STATUS.equals(status)) {
            throw new AdvertException(resultJson.toJSONString());
        }
        JSONObject body = resultJson.getJSONObject("body");
        if (Objects.isNull(body)) {
            throw new AdvertException("shenma http post result data is error");
        }
        String url = info.getUrl();
        Map<String, Object> paramsMap = Maps.newHashMap();
        if (AdvertApiConsts.API_SHENMA_GET_REPORT.equals(url)) {
            paramsMap.put(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID, body.getString(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID));
        } else if (AdvertApiConsts.API_SHENMA_GET_TASKSTATE.equals(url)) {
            paramsMap.put(AdvertApiConsts.SHENMA_RESULT_COLUMN_FILEID, body.getString(AdvertApiConsts.SHENMA_RESULT_COLUMN_FILEID));
            paramsMap.put(AdvertApiConsts.PARAM_STATUS_NAME, body.getString(AdvertApiConsts.PARAM_STATUS_NAME));
            paramsMap.put(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID, body.getString(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID));
            paramsMap.put(AdvertApiConsts.SHENMA_PARAM_CREATETIME, body.getString(AdvertApiConsts.SHENMA_PARAM_CREATETIME));
        }
        info.setParamsMap(paramsMap);
    }

    @Override
    public void processResultFile(ByteArrayInputStream inputStream, AdvertBaseInfo info) {
        try (InputStreamReader input = new InputStreamReader(inputStream); BufferedReader bf = new BufferedReader(input)) {
            String str;
            int rowNum = 0;
            Map<Object, Object> servCategMap = advertMappingCacheHandler.getServCategMap();
            Map<Object, Object> productCategMap = advertMappingCacheHandler.getProductCategMap();
            List<SemReportDay> dataList = Lists.newArrayList();
            while ((str = bf.readLine()) != null) {
                rowNum++;
                if (rowNum == 1) {
                    continue;
                }
                SemReportDay resultData = buildAdvert(str, info, servCategMap, productCategMap);
                if (resultData != null) {
                    dataList.add(resultData);
                }
            }
            info.setSaveData(true);
            advertDataBService.modifyAdvertDataByquery(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            processFailedResult(e.getMessage());
        }
    }

    /**
     * 组装SemReportDay数据
     *
     * @param str
     * @param info
     * @param servCategMap
     * @param productCategMap
     * @return
     */
    private SemReportDay buildAdvert(String str, AdvertBaseInfo info, Map<Object, Object> servCategMap, Map<Object, Object> productCategMap) {
        SemReportDay resultData = new SemReportDay();
        try {
            String[] dataArr = str.split(",");
            resultData.setUserId(info.getUserId());
            resultData.setChannelId(info.getChannelId());
            resultData.setReportDate(parseDate(dataArr[0]));
            resultData.setPublicizeDate(dataArr[0]);
            resultData.setImpression(Integer.valueOf(dataArr[7]));
            resultData.setClick(Integer.valueOf(dataArr[8]));
            resultData.setCost(formatMoney(BigDecimal.valueOf(Double.valueOf(dataArr[9])),info.getReturnRate()));
            resultData.setCtr(getCtr(dataArr[10]));
            resultData.setCpc(formatMoney(BigDecimal.valueOf(Double.valueOf(dataArr[11]))));
            processCityData(advertMappingCacheHandler,resultData, dataArr[4]);
            processCategDate(resultData, dataArr[6], servCategMap, productCategMap);
            resultData.setCpm(formatMoney(BigDecimal.valueOf(0)));
        } catch (Exception e) {
            log.error("data:{} error:{}", str, e.getMessage(), e);
            advertHttpApiLogService.insertAdvertHttpApiLog(info, e.getMessage());
            return null;
        }
        return resultData;
    }

    /**
     * 计算ctr
     *
     * @param str
     * @return
     */
    private BigDecimal getCtr(String str) {
        if (StringUtils.isEmpty(str)) {
            return BigDecimal.valueOf(0);
        }
        return BigDecimal.valueOf(Double.valueOf(str.replace("%", "")));
    }



}
