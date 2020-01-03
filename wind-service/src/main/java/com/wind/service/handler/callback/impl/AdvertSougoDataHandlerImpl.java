package com.wind.service.handler.callback.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zmn.advert.business.interfaces.advertapi.AdvertDataBService;
import com.zmn.advert.handler.AdvertMappingCacheHandler;
import com.zmn.advert.handler.callback.AdvertSougoDataHandler;
import com.zmn.advert.manager.constant.AdvertApiConsts;
import com.zmn.advert.manager.exception.AdvertException;
import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;
import com.zmn.advert.model.entity.advertapi.SemReportDay;
import com.zmn.advert.services.interfaces.advertapi.AdvertHttpApiLogService;
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
import java.util.zip.GZIPInputStream;

/**
 * 搜狗搜索推广数据处理
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 16:15
 **/
@Slf4j
@Service("advertSougoDataHandler")
public class AdvertSougoDataHandlerImpl extends BaseAdvertDataHandler implements AdvertSougoDataHandler {
    @Autowired
    private AdvertMappingCacheHandler advertMappingCacheHandler;

    @Autowired
    private AdvertDataBService advertDataBService;

    @Autowired
    private AdvertHttpApiLogService advertHttpApiLogService;

    /**
     * 处理json数据
     *
     * @param jsonStr api返回的json字符串
     * @param info
     */
    @Override
    public void processResultJsonStr(String jsonStr, AdvertBaseInfo info) {
        JSONObject resultJson = JSON.parseObject(jsonStr);
        Integer status = resultJson.getInteger(AdvertApiConsts.PARAM_STATUS_NAME);
        String url = info.getUrl();
        if (!AdvertApiConsts.API_HTTP_SUCCESS_STATUS.equals(status)) {
            throw new AdvertException("advert http post is error:" + resultJson.toJSONString());
        }
        JSONObject dataJson = resultJson.getJSONObject(AdvertApiConsts.PARAM_DATA_NAME);
        Map<String, Object> paramsMap = Maps.newHashMap();
        if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTID.equals(url)) {
            String reportId = dataJson.getString(AdvertApiConsts.SOUGO_PARAM_REPORTID);
            paramsMap.put(AdvertApiConsts.SOUGO_PARAM_REPORTID, reportId);
        } else if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTSTATE.equals(url)) {
            paramsMap.put(AdvertApiConsts.SOUGO_PARAM_REPORTID, info.getParamsMap().get(AdvertApiConsts.SOUGO_PARAM_REPORTID));
            Integer isGenerated = dataJson.getInteger(AdvertApiConsts.SOUGO_PARAM_IS_GENERATED);
            paramsMap.put(AdvertApiConsts.SOUGO_PARAM_IS_GENERATED, isGenerated);
            if (isGenerated.equals(-1)) {
                throw new AdvertException("advert sogou getTask error ");
            }
        } else if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTPATH.equals(url)) {
            paramsMap.put(AdvertApiConsts.SOUGO_PARAM_REPORTPATH, dataJson.getString(AdvertApiConsts.SOUGO_PARAM_REPORTPATH));
        }
        info.setParamsMap(paramsMap);
    }

    /**
     * 处理返回的文件
     *
     * @param inputStream 二进制流
     * @param info
     */
    @Override
    public void processResultFile(ByteArrayInputStream inputStream, AdvertBaseInfo info) {
        log.info("start sougo processResultFile,url:{}", info.getUserName());

        try {
            GZIPInputStream gzs = new GZIPInputStream(inputStream);
            InputStreamReader input = new InputStreamReader(gzs, "GBK");
            BufferedReader bf = new BufferedReader(input);
            String str;
            int rowNum = 0;
            Map<Object, Object> servCategMap = advertMappingCacheHandler.getServCategMap();
            Map<Object, Object> productCategMap = advertMappingCacheHandler.getProductCategMap();
            List<SemReportDay> dataList = Lists.newArrayList();
            while ((str = bf.readLine()) != null) {
                rowNum++;
                if (rowNum <= 2) {
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
            resultData.setReportDate(parseDate(dataArr[1]));
            resultData.setPublicizeDate(dataArr[1]);
            resultData.setImpression(Integer.valueOf(dataArr[10]));
            resultData.setClick(Integer.valueOf(dataArr[9]));
            resultData.setCost(formatMoney(BigDecimal.valueOf(Double.valueOf(dataArr[7])),info.getReturnRate()));
            resultData.setCtr(getCtr(dataArr[11]));
            resultData.setCpc(formatMoney(BigDecimal.valueOf(Double.valueOf(dataArr[8]))));
            processCityData(advertMappingCacheHandler,resultData, dataArr[4]);
            processCategDate(resultData, dataArr[6], servCategMap, productCategMap);
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
