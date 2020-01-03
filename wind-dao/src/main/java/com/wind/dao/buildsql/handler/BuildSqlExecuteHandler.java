package com.wind.dao.buildsql.handler;


/**
 * buildsql执行sql语句
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/19 13:58
 **/
public interface BuildSqlExecuteHandler<T> {

    T selectList(String sql);

    Integer count(String sql);

}
