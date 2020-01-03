package com.wind.service.handler.strategy.impl;

import com.google.common.collect.Maps;
import com.zmn.advert.handler.AdvertDataHandler;
import com.zmn.advert.handler.strategy.AdvertBaiduStrategy;
import com.zmn.advert.manager.constant.AdvertApiConsts;
import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;
import com.zmn.advert.model.entity.advertapi.ApiCallParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Baidu 数据报告获取
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:55
 **/
@Slf4j
@Service("advertBaiduStrategy")
public class AdvertBaiduStrategyImpl extends BaseAdvertStrategy implements AdvertBaiduStrategy {


    public static final Integer PAGE_SIZE = 1000;

    @Override
    public void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler) {
        super.execute(info, advertDataHandler);
        Map<String, Object> paramsMap = info.getParamsMap();
        if (paramsMap == null) {
            return;
        }
        int totalRowNumber = (Integer) paramsMap.get("totalRowNumber");
        int pageIndex = (Integer) paramsMap.get("pageIndex");
        log.info("userName:{},totalRowNumber:{}", info.getUserName(), totalRowNumber);
        if (totalRowNumber <= PAGE_SIZE) {
            return;
        }
        int totalPage = totalRowNumber / PAGE_SIZE;
        if (totalRowNumber % PAGE_SIZE != 0) {
            totalPage++;
        }
        for (int i = pageIndex; i < totalPage; i++) {
            pageIndex++;
            log.info("start query baidu advert data, username:{},pageIndex:{}", info.getUserName(), pageIndex);
            info.setPageIndex(pageIndex);
            String dataJson = execute(info);
            advertDataHandler.processResultJsonStr(dataJson, info);
        }
    }

    @Override
    protected void beforeEnhance(ApiCallParam apiCallParam, AdvertBaseInfo info) {
        info.setUrl(AdvertApiConsts.API_REPORT_BAIDU);
    }

    /***
     * http的header
     * @param info
     * @return
     */
    @Override
    protected Map<String, Object> getHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", "application/json;charset=utf8");
        return headerMap;
    }

    /**
     * 组装body参数
     *
     * @param info
     * @return
     */
    @Override
    protected Map<String, Object> buildBody(AdvertBaseInfo info) {
        Map<String, Object> bodyMap = Maps.newHashMap();
        bodyMap.put("realTimeRequestType", buildRealTimeRequestType(info));
        return bodyMap;
    }

    @Override
    protected Map<String, Object> buildHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = super.buildHeader(info);
        //如果有子账号，则注入子账号
        if (!StringUtils.isEmpty(info.getSubUserName())) {
            headerMap.put("target", info.getSubUserName());
        }
        return headerMap;
    }

    /**
     * 组装realTimeRequestType
     *
     * @param info
     * @return
     */
    protected Map<String, Object> buildRealTimeRequestType(AdvertBaseInfo info) {
        Map<String, Object> realTimeRequestType = Maps.newHashMap();
        //返回结果列
        realTimeRequestType.put("performanceData", AdvertApiConsts.BAIDU_PERFORMANCE_DATA);
        //数据粒度，单元
        realTimeRequestType.put("reportType", 11);
        //时间粒度，天
        realTimeRequestType.put("unitOfTime", 5);
        //单元粒度
        realTimeRequestType.put("levelOfDetails", 5);

        realTimeRequestType.put("number", PAGE_SIZE);
        Integer pageIndex = info.getPageIndex();
        if (pageIndex == null) {
            pageIndex = 1;
        }
        realTimeRequestType.put("pageIndex", pageIndex);

        //开始时间
        realTimeRequestType.put("startDate", info.getStartDate());
        //结束时间
        realTimeRequestType.put("endDate", info.getEndDate());
        return realTimeRequestType;
    }


}
