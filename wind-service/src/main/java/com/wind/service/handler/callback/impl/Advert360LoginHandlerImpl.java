package com.wind.service.handler.callback.impl;

import com.wind.service.handler.callback.Advert360LoginHandler;
import com.wind.service.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 360登录返回结果处理
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 14:33
 **/
@Slf4j
@Service("advert360LoginHandler")
public class Advert360LoginHandlerImpl extends BaseAdvertDataHandler implements Advert360LoginHandler {

    @Override
    public void processResultJsonStr(String jsonStr, AdvertBaseInfo info) {
        try {
            String accessToken = XmlUtils.get360AccessToken(jsonStr);
            log.info("360 success get accessToken:[{}],userName:{}", accessToken, info.getUserName());
        } catch (Exception e) {
            log.error("360 failed get accessToken: ,userName:{" + info.getUserName() + "}", e);
            processFailedResult(e.getMessage());
        }
    }
}
