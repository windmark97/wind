package com.wind.service.handler.strategy.impl;

import com.google.common.collect.Maps;
import com.zmn.advert.handler.strategy.Advert360ClientLoginStrategy;
import com.zmn.advert.manager.constant.AdvertApiConsts;
import com.zmn.advert.manager.exception.AdvertException;
import com.zmn.advert.model.entity.advertapi.AdvertBaseInfo;
import com.zmn.advert.model.entity.advertapi.ApiCallParam;
import com.zmn.advert.util.ApiHttpClientUtils;
import com.zmn.advert.util.EncryptUtils;
import com.zmn.advert.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 搜索推广，360执行策略
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:55
 **/
@Slf4j
@Service("advert360ClientLoginStrategy")
public class Advert360ClientLoginStrategyImpl extends BaseAdvertStrategy implements Advert360ClientLoginStrategy {


    @Override
    public String clientLogin(AdvertBaseInfo info) {
        log.info("start 360 advert data clientLogin,userName:{}", info.getUserName());
        try {
            ApiCallParam apiCallParam = new ApiCallParam();
            beforeEnhance(apiCallParam, info);
            buildApiCallParam(apiCallParam, info);
            String resultStr = ApiHttpClientUtils.sendHttpPost(apiCallParam);
            log.info("end 360 advert data clientLogin,data{}", info.getUserName());
            return formatResult(resultStr, info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException("360 登录失败！" + e.getMessage());
        }
    }

    @Override
    protected void beforeEnhance(ApiCallParam apiCallParam, AdvertBaseInfo info) {
        info.setUrl(AdvertApiConsts.API_LOGIN_360);
    }

    /**
     * @param info
     * @return
     */
    @Override
    protected Map<String, Object> getHeader(AdvertBaseInfo info) {
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("apiKey", info.getToken());
        headerMap.put("Content-Type", AdvertApiConsts.CONTENT_TYPE_FORM);
        return headerMap;
    }

    @Override
    protected Map<String, Object> buildData(AdvertBaseInfo info) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("username", info.getUserName());
        dataMap.put("passwd", getEncryptPassword(info));
        return dataMap;
    }

    /**
     * 加密密码
     *
     * @param info
     * @return
     */
    private String getEncryptPassword(AdvertBaseInfo info) {
        try {
            String passwordMd5 = EncryptUtils.encryptByMd5(info.getPassword());
            String passwordHexAse = EncryptUtils.encrypt(passwordMd5, info.getApiSecret());
            return passwordHexAse.substring(0, 64);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdvertException("密码加密异常!" + e.getMessage());
        }
    }

    public String formatResult(String jsonStr, AdvertBaseInfo info) {
        String accessToken = XmlUtils.get360AccessToken(jsonStr);
        log.info("360 success get accessToken:[{}],userName:{}", accessToken, info.getUserName());
        return accessToken;
    }


}
