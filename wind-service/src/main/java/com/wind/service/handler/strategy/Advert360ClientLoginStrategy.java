package com.wind.service.handler.strategy;


import com.wind.dao.model.AdvertBaseInfo;
import com.wind.service.handler.AdvertStrategy;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:32
 **/
public interface Advert360ClientLoginStrategy extends AdvertStrategy {
    /**
     * 360执行登录接口
     * @param info
     * @return
     */
    String clientLogin(AdvertBaseInfo info);
}
