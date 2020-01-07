package com.wind.dao.buildsql.proxy;

import com.wind.dao.buildsql.config.BuildSqlConfiguration;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *  BuildSqlMapper代理工厂
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 10:24
 **/
public class BuildSqlMapperProxyFactory<T> {
    private final Class<T> mapperInterface;
    private final Map<Method, BuildSqlMapperMethod> methodCache = new ConcurrentHashMap<>();

    public BuildSqlMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, BuildSqlMapperMethod> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(BuildSqlMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public T newInstance(BuildSqlConfiguration configuration) {
        final BuildSqlMapperProxy<T> mapperProxy = new BuildSqlMapperProxy(mapperInterface, methodCache, configuration);
        return newInstance(mapperProxy);
    }
}
