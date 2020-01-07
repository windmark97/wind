package com.wind.dao.buildsql.proxy;

import com.wind.dao.buildsql.config.BuildSqlConfiguration; 
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;

import java.util.*;

/**
 *
 * buildsql的mapper接口注册类
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 13:33
 **/
public class BuildSqlMapperRegistry {

    private final BuildSqlConfiguration config;

    private final Map<Class<?>, BuildSqlMapperProxyFactory<?>> knownMappers = new HashMap();

    public BuildSqlMapperRegistry(BuildSqlConfiguration config) {
        this.config = config;
    }

    public <T> T getMapper(Class<T> type) {
        BuildSqlMapperProxyFactory<T> mapperProxyFactory = (BuildSqlMapperProxyFactory) this.knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        } else {
            try {
                return mapperProxyFactory.newInstance(config);
            } catch (Exception var5) {
                throw new BindingException("Error getting mapper instance. Cause: " + var5, var5);
            }
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return this.knownMappers.containsKey(type);
    }

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (this.hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }

            boolean loadCompleted = false;

            try {
                this.knownMappers.put(type, new BuildSqlMapperProxyFactory(type));
                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(this.config, type);
                parser.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    this.knownMappers.remove(type);
                }
            }
        }

    }

    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(this.knownMappers.keySet());
    }

    public void addMappers(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        Iterator var5 = mapperSet.iterator();
        while (var5.hasNext()) {
            Class<?> mapperClass = (Class) var5.next();
            this.addMapper(mapperClass);
        }

    }

    public void addMappers(String packageName) {
        this.addMappers(packageName, Object.class);
    }
}
