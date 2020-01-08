package com.wind.service.service.interfaces;


import com.wind.dao.model.AdvertBaseInfo;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/28 14:13
 **/
public interface AdvertHttpApiLogService {
    /**
     * 新增
     * @param info
     * @param message
     */
    void insertAdvertHttpApiLog(AdvertBaseInfo info, String message);


}
