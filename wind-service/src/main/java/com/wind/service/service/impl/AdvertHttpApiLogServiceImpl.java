package com.wind.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.wind.dao.mapper.AdvertHttpApiLogDao;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.AdvertHttpApiLog;
import com.wind.dao.model.ApiCallParam;
import com.wind.service.service.interfaces.AdvertHttpApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 搜索推广任务日志
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/28 14:13
 **/
@Slf4j
@Service
public class AdvertHttpApiLogServiceImpl implements AdvertHttpApiLogService {

    @Autowired(required = false)
    private AdvertHttpApiLogDao advertHttpApiLogDao;

    /**
     * 错误信息最大字符个数
     */
    private static final Integer MESSAGE_MAX_SIZE= 1000;

    @Override
    public void insertAdvertHttpApiLog(AdvertBaseInfo info, String message) {
        AdvertHttpApiLog advertHttpApiLog = buildAdvertHttpApiLog(info, message);
        advertHttpApiLogDao.insert(advertHttpApiLog);
    }

    /**
     * 构建错误日志对象
     * @param info
     * @param message
     * @return
     */
    private AdvertHttpApiLog buildAdvertHttpApiLog(AdvertBaseInfo info, String message) {
        AdvertHttpApiLog buildData = new AdvertHttpApiLog();
        buildData.setUserId(info.getUserId());
        buildData.setPlatType(info.getAdvertType());
        buildData.setStartDate(info.getStartDate());
        buildData.setEndDate(info.getEndDate());
        ApiCallParam apiCallParam = info.getApiCallParam();
        if(!Objects.isNull(apiCallParam)){
            buildData.setHttpParam(JSON.toJSONString(apiCallParam.getDataMap()));
        }else{
            buildData.setHttpParam("-1");
        }
        int endIndex = message.length();
        if (endIndex > MESSAGE_MAX_SIZE) {
            endIndex = MESSAGE_MAX_SIZE;
        }
        buildData.setMessage(message.substring(0, endIndex));
        String dataStr = info.getStartDate().replaceAll("-", "");
        buildData.setDate(Integer.valueOf(dataStr));
        return buildData;
    }
}
