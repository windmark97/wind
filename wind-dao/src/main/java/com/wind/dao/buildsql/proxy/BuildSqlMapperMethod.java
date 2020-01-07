package com.wind.dao.buildsql.proxy;


import com.wind.dao.buildsql.config.BuildSqlConfiguration;
import com.wind.dao.buildsql.handler.BuildSqlExecuteHandler;
import com.wind.dao.buildsql.handler.BuildSqlResultHandler;
import com.wind.dao.buildsql.utils.BuildSqlUtils;
import com.wind.dao.buildsql.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * BuildSqlMapper代理的具体逻辑
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/16 19:57
 **/
@Slf4j
public class BuildSqlMapperMethod {
    private final MapperMethod.SqlCommand command;
    private final MapperMethod.MethodSignature method;
    private final BuildSqlConfiguration buildSqlConfiguration;

    public BuildSqlMapperMethod(Class<?> mapperInterface, Method method, BuildSqlConfiguration buildSqlConfiguration) {
        this.buildSqlConfiguration = buildSqlConfiguration;
        this.command = new MapperMethod.SqlCommand(buildSqlConfiguration, mapperInterface, method);
        this.method = new MapperMethod.MethodSignature(buildSqlConfiguration, mapperInterface, method);
    }

    public Object execute(Object[] args) {
        String mappedStatementId = this.command.getName();
        BuildSqlResultHandler handler = new BuildSqlResultHandler(command, method, buildSqlConfiguration);
        BoundSql boundSql = handler.getBoundSql(args);
        String sql = BuildSqlUtils.formatSql(boundSql.getSql(), buildSqlConfiguration, boundSql);
        BuildSqlExecuteHandler buildSqlExecuteHandler = SpringContextUtils.getBean(BuildSqlExecuteHandler.class);
        Object dataObject = null;
        log.debug("buildSql start: mappedStatementId:{},sql:{}", mappedStatementId, sql);
        if(this.method.getReturnType().equals(Integer.class)){
            dataObject = buildSqlExecuteHandler.count(sql);
            return dataObject;
        }else{
            dataObject = buildSqlExecuteHandler.selectList(sql);
        }
        Object result = null;
        if (SqlCommandType.SELECT.equals(this.command.getType())) {
            if (this.method.returnsVoid() && this.method.hasResultHandler()) {
                //无返回值
                log.debug("buildSql returnsVoid,mappedStatementId:{}", mappedStatementId);
                result = null;
            } else if (this.method.returnsMany()) {
                RowBounds rowBounds = method.extractRowBounds(args);
                log.debug("buildSql returnsMany,mappedStatementId:{}", mappedStatementId);
                if (this.method.hasRowBounds()) {
                    log.debug("buildSql execute,returnsMany2:{}");
                }
                List<Object> resultList = (List<Object>) dataObject;
                result = handler.executeForMany(resultList);
            } else if (this.method.returnsMap()) {
                List<Object> resultList = (List<Object>) dataObject;
                if(resultList==null||resultList.isEmpty()){
                    result = Collections.emptyMap();
                }else{
                    result = resultList.get(0);
                }
            } else if (this.method.returnsCursor()) {
                result = handler.executeForCursor((List<Object>) dataObject);
            } else {
                result = handler.executeForCursor((List<Object>) dataObject);
            }
        }
        log.debug("execute end,mappedStatementId:{}", mappedStatementId);
        return result;
    }


}
