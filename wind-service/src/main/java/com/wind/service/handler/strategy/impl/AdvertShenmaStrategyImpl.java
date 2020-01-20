package com.wind.service.handler.strategy.impl;


import com.google.common.collect.Maps;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.ApiCallParam;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.manager.exception.AdvertException;
import com.wind.service.handler.callback.AdvertDataHandler;
import com.wind.service.handler.strategy.AdvertShenmaStrategy;
import com.wind.service.util.ApiHttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 神马数据报告获取
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:39
 **/
@Slf4j
@Service("advertShenmaStrategy")
public class AdvertShenmaStrategyImpl extends BaseAdvertStrategy implements AdvertShenmaStrategy {

    /**
     * 循环获取报告重试次数
     */
    public static final int GET_FILE_NUM_MAX = 10;
    /**
     * 等待文件创建时间
     */
    public static final Integer FILE_WAIT_TIME = 1000;
    /**
     * 等待文件创建超时时间
     */
    public static final Long FILE_DOWNLOAD_TIMEOUT = 10000L;

    @Override
    public String execute(AdvertBaseInfo info) {
        log.info("start advert data sysn,userName:{}", info.getUserName());
        String resultStr = null;
        try {
            ApiCallParam apiCallParam = new ApiCallParam();
            buildApiCallParam(apiCallParam, info);
            apiCallParam.setUrl(info.getUrl());
            resultStr = ApiHttpClientUtils.sendHttpPost(apiCallParam);
            log.info("end advert data sysn,userName:{}", info.getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException(e.getMessage());
        }
        return resultStr;
    }

    @Override
    public void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler) {
        ByteArrayInputStream inputStream = null;
        try {
            info.setUrl(AdvertApiConsts.API_SHENMA_GET_REPORT);
            String jsonStr = execute(info);
            advertDataHandler.processResultJsonStr(jsonStr, info);
            sleep(FILE_WAIT_TIME);
            getTask(advertDataHandler, info);
            info.setUrl(AdvertApiConsts.API_SHENMA_DOWNLOAD);
            inputStream = downloadFile(info);
            if (advertDataHandler != null) {
                advertDataHandler.processResultFile(inputStream, info);
            }

        } catch (Exception e) {
            log.error("advert shenma http post failed!", e);
            throw new AdvertException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

    }

    /**
     * 获取创建文件任务
     *
     * @param advertDataHandler
     * @param info
     */
    public void getTask(AdvertDataHandler advertDataHandler, AdvertBaseInfo info) {
        //计数器控制递归获取次数
        int counter = info.getCounter();
        if (counter > GET_FILE_NUM_MAX) {
            throw new AdvertException("get report file time out");
        }
        counter++;
        info.setCounter(counter);
        info.setUrl(AdvertApiConsts.API_SHENMA_GET_TASKSTATE);
        String jsonStr = execute(info);
        advertDataHandler.processResultJsonStr(jsonStr, info);
        boolean isFinished = true;
        if (AdvertApiConsts.SHENMA_RESULT_STATUS_FINISHED.equals(info.getParamsMap().get(AdvertApiConsts.PARAM_STATUS_NAME))) {
            isFinished = true;
        } else {
            String createTime = (String) info.getParamsMap().get(AdvertApiConsts.SHENMA_PARAM_CREATETIME);
            if (StringUtils.isEmpty(createTime)) {
                throw new AdvertException("advert shema getTask result data error! ");
            }
            LocalDateTime beginDateTime = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime cuurentDate = LocalDateTime.now();
            Duration duration = Duration.between(cuurentDate, beginDateTime);
            //指定超时时间，避免死递归
            if (duration.toMillis() > FILE_DOWNLOAD_TIMEOUT) {
                throw new AdvertException("advert shema getTask timeOut ");
            }
            sleep(AdvertApiConsts.FILE_WAIT_TIME * counter);
            isFinished = false;
        }
        if (!isFinished) {
            getTask(advertDataHandler, info);
        }
    }


    /**
     * 执行下载文件
     *
     * @param info
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream downloadFile(AdvertBaseInfo info) {
        log.info("start advert download file,userName:{}", info.getUserName());
        ByteArrayInputStream inputStream = null;
        try {
            ApiCallParam apiCallParam = new ApiCallParam();
            buildApiCallParam(apiCallParam, info);
            apiCallParam.setUrl(info.getUrl());
            inputStream = ApiHttpClientUtils.httpPostDownload(apiCallParam);
            log.info("end advert download file,userName:{}", info.getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException("神马：下载报告文件失败！");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return inputStream;
    }


    /**
     * 组装ApiCallParam请求对象
     *
     * @param apiCallParam
     * @param info
     */
    @Override
    protected void buildApiCallParam(ApiCallParam apiCallParam, AdvertBaseInfo info) {
        apiCallParam.setUrl(info.getUrl());
        apiCallParam.setHeaderMap(getHeader(info));
        apiCallParam.setDataMap(buildData(info));
        apiCallParam.setEncodeFormat(AdvertApiConsts.ENCODING_FORMAT);
    }


    /**
     * 组装body参数
     *
     * @param info
     * @return
     */
    @Override
    protected Map<String, Object> buildBody(AdvertBaseInfo info) {
        String url = info.getUrl();
        Map<String, Object> bodyMap = Maps.newHashMap();
        if (AdvertApiConsts.API_SHENMA_GET_REPORT.equals(url)) {
            bodyMap.put("startDate", info.getStartDate());
            bodyMap.put("endDate", info.getEndDate());
            bodyMap.put("reportType", 11);
            bodyMap.put("unitOfTime", 5);
            bodyMap.put("performanceData", AdvertApiConsts.SHENMA_AND_SOUGO_PERFORMANCE_DATA);
        } else if (AdvertApiConsts.API_SHENMA_GET_TASKSTATE.equals(url)) {
            Map<String, Object> paramsMap = info.getParamsMap();
            bodyMap.put(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID, paramsMap.get(AdvertApiConsts.SHENMA_RESULT_COLUMN_TASKID));
        } else if (AdvertApiConsts.API_SHENMA_DOWNLOAD.equals(url)) {
            Map<String, Object> paramsMap = info.getParamsMap();
            bodyMap.put(AdvertApiConsts.SHENMA_RESULT_COLUMN_FILEID, paramsMap.get(AdvertApiConsts.SHENMA_RESULT_COLUMN_FILEID));
        }
        return bodyMap;
    }

}
