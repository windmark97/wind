package com.wind.service.service.interfaces;


import com.wind.dao.model.SemReportDay;

import java.util.List;

/**
 * SemReportDay数据服务
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 14:12
 **/
public interface SemReportDayService {
    /**
     * 新增单条数据
     *
     * @param semReportDay
     */
    void insertSemReportDay(SemReportDay semReportDay);

    /**
     * 批量新增
     *
     * @param semReportDayList
     */
    void insertBatchSemReportDay(List<SemReportDay> semReportDayList);


    void clearAdvertData(SemReportDay semReportDay);
}
