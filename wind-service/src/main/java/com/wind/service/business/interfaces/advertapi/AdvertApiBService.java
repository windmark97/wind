package com.wind.service.business.interfaces.advertapi;


import com.wind.dao.model.AdvertJobParam;

/**
 * 搜索推广服务
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:25
 **/
public interface AdvertApiBService {
    /**
     * 同步获取推广数据
     * @param param
     * @return
     */
    String synAdvertApiData(AdvertJobParam param);

}
