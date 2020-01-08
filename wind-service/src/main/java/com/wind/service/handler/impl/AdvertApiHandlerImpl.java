package com.wind.service.handler.impl;

import com.wind.dao.model.AdvertBaseInfo;
import com.wind.manager.constant.AdvertApiEnum;
import com.wind.manager.exception.AdvertException;
import com.wind.manager.utils.SpringContextUtils;
import com.wind.service.business.interfaces.advertapi.AdvertDataBService;
import com.wind.service.handler.AdvertApiHandler;
import com.wind.service.handler.AdvertDataHandler;
import com.wind.service.handler.AdvertStrategy;
import com.wind.service.service.interfaces.AdvertHttpApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 搜索推广执行器
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:52
 **/
@Slf4j
@Service
public class AdvertApiHandlerImpl implements AdvertApiHandler {

    @Autowired
    private AdvertHttpApiLogService advertHttpApiLogService;

    @Autowired
    private AdvertDataBService advertDataBService;

    /**
     * 根据参数选择执行的策略
     *
     * @param info
     * @return
     */
    @Async
    @Override
    public void execute(AdvertBaseInfo info) {
        AdvertStrategy strategy = null;
        AdvertDataHandler handler = null;
        try {
            Integer platType = info.getAdvertType();
            AdvertApiEnum apiEnum = AdvertApiEnum.ergodicOf(platType);
            if (apiEnum == null) {
                throw new AdvertException("not find " + platType + " enum");
            }
            handler = (AdvertDataHandler) SpringContextUtils.getBean(apiEnum.getHandlerName());
            strategy = (AdvertStrategy) SpringContextUtils.getBean(apiEnum.getServiceName());
            advertDataBService.clearAdvertData(info);
            strategy.execute(info, handler);
        } catch (Exception e) {
            log.error("userName:{},platType:{}", info.getUserName(), info.getAdvertType(), e);
            advertHttpApiLogService.insertAdvertHttpApiLog(info, e.getMessage());
            //保存过数据才清理数据
            if(info.getSaveData()){
                advertDataBService.clearAdvertData(info);
            }
        }
    }




}
