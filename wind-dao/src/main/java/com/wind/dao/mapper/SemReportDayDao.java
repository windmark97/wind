package com.wind.dao.mapper;

import com.wind.dao.model.SemReportDay;
import com.wind.dao.model.SemReportDayQuery;

import java.util.List;

/**
 * 搜索推广数据Dao
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 13:57
 **/
public interface SemReportDayDao {
    /**
     * 根据主键查询
     *
     * @param reportId
     * @return
     */
    SemReportDay findByKey(Integer reportId);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    List<SemReportDay> listPage(SemReportDayQuery query);

    /**
     * 查询总量
     *
     * @param query
     * @return
     */
    Integer countByQuery(SemReportDayQuery query);

    /**
     * 插入
     *
     * @param semReportDay
     * @return
     */
    Integer insert(SemReportDay semReportDay);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    Integer insertBatch(List<SemReportDay> list);

}
