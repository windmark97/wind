package com.wind.service.handler.strategy.impl;

import com.google.common.collect.Maps;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.manager.exception.AdvertException;
import com.wind.service.handler.AdvertDataHandler;
import com.wind.service.handler.strategy.AdvertSougoStrategy;
import com.wind.service.util.ApiHttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * 搜狗数据报告获取
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 16:12
 **/
@Slf4j
@Service("advertSougoStrategy")
public class AdvertSougoStrategyImpl extends BaseAdvertStrategy implements AdvertSougoStrategy {

    /**
     * 循环获取报告重试次数
     */
    public static final int GET_FILE_NUM_MAX = 3;
    /**
     * 报告生成成功标志
     */
    public static final int IS_GENERATE_SUCCESS = 1;
    /**
     * 数据粒度：推广组
     */
    public static final int SOUGO_REPORT_TYPE = 3;
    /**
     * 时间粒度：天
     */
    public static final int SOUGO_UNIT_OF_TIME_DAY = 1;
    /**
     * 统计粒度：整体
     */
    public static final int SOUGO_LEVEL_OF_DETAILS = 1;
    /**
     * 统计范围：账户
     */
    public static final int SOUGO_STAT_RANGE = 1;

    @Override
    public void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler) {
        info.setUrl(AdvertApiConsts.API_SOUGO_REPORT_GETREPORTID);
        String jsonStr = execute(info);
        advertDataHandler.processResultJsonStr(jsonStr, info);
        checkStatus(advertDataHandler, info);
        info.setUrl(AdvertApiConsts.API_SOUGO_REPORT_GETREPORTPATH);
        String pathJson = execute(info);
        advertDataHandler.processResultJsonStr(pathJson, info);
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = downloadFile(info);
            if (advertDataHandler != null) {
                advertDataHandler.processResultFile(inputStream, info);
            }
        } catch (Exception e) {
            log.error("advert sougo http post failed!", e);
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
     * 检查返回状态
     *
     * @param advertDataHandler
     * @param info
     */
    private void checkStatus(AdvertDataHandler advertDataHandler, AdvertBaseInfo info) {
        //计数器控制递归获取次数
        int counter = info.getCounter();
        if (counter > GET_FILE_NUM_MAX) {
            throw new AdvertException("get report file time out");
        }
        counter++;
        info.setCounter(counter);
        info.setUrl(AdvertApiConsts.API_SOUGO_REPORT_GETREPORTSTATE);
        //每过一次，等待时间延长
        sleep(AdvertApiConsts.FILE_WAIT_TIME * counter);
        String jsonStr = execute(info);
        advertDataHandler.processResultJsonStr(jsonStr, info);
        if (info.getParamsMap().get(AdvertApiConsts.SOUGO_PARAM_IS_GENERATED).equals(IS_GENERATE_SUCCESS)) {
            return;
        } else {
            checkStatus(advertDataHandler, info);
        }
    }


    /**
     * 请求体中的header数据
     *
     * @param info
     * @return
     */
    @Override
    protected Map<String, Object> buildHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = super.buildHeader(info);
        //平台类型，区分操作物料平台；1:网页搜索推广，2:输入与搜索生态推广
        headerMap.put("adType", 1);
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
        String url = info.getUrl();
        Map<String, Object> bodyMap = Maps.newHashMap();
        if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTID.equals(url)) {
            bodyMap.put("startDate", info.getStartDate() + AdvertApiConsts.START_DATE_HSM);
            bodyMap.put("endDate", info.getEndDate() + AdvertApiConsts.END_DATE_HSM);
            bodyMap.put("reportType", SOUGO_REPORT_TYPE);
            bodyMap.put("unitOfTime", SOUGO_UNIT_OF_TIME_DAY);
            bodyMap.put("levelOfDetails", SOUGO_LEVEL_OF_DETAILS);
            bodyMap.put("statRange", SOUGO_STAT_RANGE);
            bodyMap.put("performanceData", AdvertApiConsts.SHENMA_AND_SOUGO_PERFORMANCE_DATA);
        } else if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTSTATE.equals(url)) {
            Map<String, Object> paramsMap = info.getParamsMap();
            bodyMap.put(AdvertApiConsts.SOUGO_PARAM_REPORTID, paramsMap.get(AdvertApiConsts.SOUGO_PARAM_REPORTID));
        } else if (AdvertApiConsts.API_SOUGO_REPORT_GETREPORTPATH.equals(url)) {
            Map<String, Object> paramsMap = info.getParamsMap();
            bodyMap.put(AdvertApiConsts.SOUGO_PARAM_REPORTID, paramsMap.get(AdvertApiConsts.SOUGO_PARAM_REPORTID));
        }
        return bodyMap;
    }

    /**
     * 执行下载文件
     *
     * @param info
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream downloadFile(AdvertBaseInfo info) {
        log.info("start download file,userName:{}", info.getUserName());
        ByteArrayInputStream inputStream = null;
        try {
            String downloadUrl = (String) info.getParamsMap().get(AdvertApiConsts.SOUGO_PARAM_REPORTPATH);
            log.info("send get http for download report file,url:{}", downloadUrl);
            inputStream = ApiHttpClientUtils.httpGetDownload(downloadUrl);
            log.info("end download file,userName:{}", info.getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new AdvertException("error,download file！");
        }
        return inputStream;
    }

}
