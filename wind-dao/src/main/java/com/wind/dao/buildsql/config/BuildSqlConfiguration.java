package com.wind.dao.buildsql.config;

import com.wind.dao.buildsql.proxy.BuildSqlMapperRegistry;
import org.apache.ibatis.session.Configuration;

/**
 * buildsql配置类
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 11:10
 **/
public class BuildSqlConfiguration extends Configuration {

    protected final BuildSqlMapperRegistry buildSqlMapperRegistry = new BuildSqlMapperRegistry(this);

    public <T> T getMapper(Class<T> type) {
        return buildSqlMapperRegistry.getMapper(type);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return buildSqlMapperRegistry.hasMapper(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        buildSqlMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        buildSqlMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        buildSqlMapperRegistry.addMappers(packageName);
    }


}
