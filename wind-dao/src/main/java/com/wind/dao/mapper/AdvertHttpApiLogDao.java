package com.wind.dao.mapper;


import com.wind.dao.model.AdvertHttpApiLog;
import com.wind.dao.model.AdvertHttpApiLogQuery;

import java.util.List;

/**
 * 搜索推广获取数据任务日志
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/28 13:51
 **/
public interface AdvertHttpApiLogDao {

    /**
     * 根据主键查询
     *
     * @param logId
     * @return
     */
    AdvertHttpApiLog findByKey(Integer logId);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    List<AdvertHttpApiLog> listPage(AdvertHttpApiLogQuery query);

    /**
     * 查询总量
     *
     * @param query
     * @return
     */
    Integer countByQuery(AdvertHttpApiLogQuery query);

    /**
     * 插入
     *
     * @param advertHttpApiLog
     * @return
     */
    Integer insert(AdvertHttpApiLog advertHttpApiLog);

    /**
     * 通过主键删除
     * @param logId
     * @return
     */
    Integer deleteByKey(Integer logId);

    /**
     * 根据条件删除
     * @param query
     * @return
     */
    Integer deleteByWhere(AdvertHttpApiLogQuery query);

}
