package com.wind.service.business.interfaces.advertapi;


import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.SemReportDay;

import java.util.List;


/**
 * 搜索推广数据报告保存
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/29 11:19
 **/
public interface AdvertDataBService {

    /**
     * 数据保存
     * @param dataList
     */
    void modifyAdvertDataByquery(List<SemReportDay> dataList);

    /**
     * 清楚账号当天的数据
     * @param info
     */
    void clearAdvertData(AdvertBaseInfo info);

}
