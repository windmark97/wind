package com.wind.dao.buildsql.handler;

import com.google.common.collect.Maps;
import com.zmn.performance.persistence.buildsql.config.BuildSqlConfiguration;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.result.DefaultMapResultHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * sql 返回数据结果处理
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/19 13:24
 **/
public class BuildSqlResultHandler {
    private final MapperMethod.SqlCommand command;
    private final MapperMethod.MethodSignature method;
    private final MappedStatement mappedStatement;
    private final BuildSqlConfiguration buildSqlConfiguration;


    public BuildSqlResultHandler(MapperMethod.SqlCommand command, MapperMethod.MethodSignature method, BuildSqlConfiguration buildSqlConfiguration) {
        this.command = command;
        this.method = method;
        this.buildSqlConfiguration = buildSqlConfiguration;
        String mappedStatementId = this.command.getName();
        this.mappedStatement = buildSqlConfiguration.getMappedStatement(mappedStatementId);
    }

    public BoundSql getBoundSql(Object[] args) {
        Object param = this.method.convertArgsToSqlCommandParam(args);
        BoundSql boundSql = this.mappedStatement.getBoundSql(param);
        return boundSql;
    }

    public <E> Object executeForMany(List<E> result) {
        if (!method.getReturnType().isAssignableFrom(result.getClass())) {
            if (method.getReturnType().isArray()) {
                return convertToArray(result);
            } else {
                //转成Map
                return convertToDeclaredCollection(buildSqlConfiguration, result);
            }
        }
        return processMapResult(result);
    }

    public <K, V> Map<K, V> selectMap(List<? extends V> list) {
        final DefaultMapResultHandler<K, V> mapResultHandler = new DefaultMapResultHandler<K, V>(method.getMapKey(),
                buildSqlConfiguration.getObjectFactory(), buildSqlConfiguration.getObjectWrapperFactory(), buildSqlConfiguration.getReflectorFactory());
        final DefaultResultContext<V> context = new DefaultResultContext<V>();
        for (V o : list) {
            context.nextResultObject(o);
            mapResultHandler.handleResult(context);
        }
        return mapResultHandler.getMappedResults();
    }

    public <E> Object convertToArray(List<E> list) {
        Class<?> arrayComponentType = method.getReturnType().getComponentType();
        Object array = Array.newInstance(arrayComponentType, list.size());
        if (arrayComponentType.isPrimitive()) {
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }
            return array;
        } else {
            return list.toArray((E[]) array);
        }
    }

    public <E> Object convertToDeclaredCollection(BuildSqlConfiguration config, List<E> list) {

        Object collection = config.getObjectFactory().create(method.getReturnType());
        MetaObject metaObject = config.newMetaObject(collection);
        metaObject.addAll(list);
        return collection;
    }

    /**
     * 处理返回结果
     *
     * @param list
     * @param <E>
     * @return
     */
    public <E> Object processMapResult(List<E> list) {
        List<ResultMap> resultMapList = mappedStatement.getResultMaps();
        if (resultMapList == null || resultMapList.isEmpty()) {
            return list;
        }
        ResultMap resultMap = resultMapList.get(0);
        List<ResultMapping> resultMappingList = resultMap.getResultMappings();
        Class resultType = resultMap.getType();
        List resultList = new ArrayList(list.size());
        try {
            for (E o : list) {
                Object dto = buildSqlConfiguration.getObjectFactory().create(resultType);
                if (o instanceof Map) {
                    Map<String, Object> dataMap = proccessResultMapping((Map<String, Object>) o, resultMappingList);
                    BeanUtils.populate(dto, dataMap);
                }
                resultList.add(dto);
            }
        } catch (Exception e) {

            return list;
        }
        return resultList;
    }

    /**
     * 处理返回结果的映射关系
     *
     * @param dataMap
     * @param resultMappingList
     * @return
     */
    public Map<String, Object> proccessResultMapping(Map<String, Object> dataMap, List<ResultMapping> resultMappingList) {
        Map<String, Object> map = Maps.newHashMap();
        for (ResultMapping resultMapping : resultMappingList) {
            resultMapping.getColumn();
            resultMapping.getProperty();
            map.put(resultMapping.getProperty(), dataMap.get(resultMapping.getColumn()));
        }
        return map;
    }


    public Object wrapCollection(final Object object) {
        if (object instanceof Collection) {
            DefaultSqlSession.StrictMap<Object> map = new DefaultSqlSession.StrictMap<Object>();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        } else if (object != null && object.getClass().isArray()) {
            DefaultSqlSession.StrictMap<Object> map = new DefaultSqlSession.StrictMap<Object>();
            map.put("array", object);
            return map;
        }
        return object;
    }

    public <E> Object executeForCursor(List<E> result) {
        if (result == null || result.isEmpty()) {
            return null;
        }
        if(result.size()>1){
            throw new RuntimeException("Too many rows returned");
        }
        Map<String, Object> objectMap =(Map<String, Object>)result.get(0);
        return processMapResult(objectMap);
    }

    public <E> Object processMapResult(Map<String, Object> objectMap) {
        List<ResultMap> resultMapList = mappedStatement.getResultMaps();
        if (resultMapList == null) {
            return objectMap;
        }
        if (resultMapList.isEmpty()) {
            return null;
        }
        ResultMap resultMap = resultMapList.get(0);
        List<ResultMapping> resultMappingList = resultMap.getResultMappings();
        Class resultType = resultMap.getType();
        Object dto = buildSqlConfiguration.getObjectFactory().create(resultType);
        try {
            Map<String, Object> dataMap = proccessResultMapping(objectMap, resultMappingList);
            BeanUtils.populate(dto, dataMap);
        } catch (Exception e) {

            return objectMap;
        }
        return dto;
    }
}
