package com.wind.service.handler;


import com.wind.dao.model.AdvertBaseInfo;

/**
 * 搜索推广执行策略接口
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:55
 **/
public interface AdvertStrategy {

    /**
     * 执行并有回调处理类
     * @param info
     * @param advertDataHandler
     */
    void execute(AdvertBaseInfo info, AdvertDataHandler advertDataHandler);

    /**
     * 执行，不需要回调
     * @param info
     * @return
     */
    String execute(AdvertBaseInfo info);

}
