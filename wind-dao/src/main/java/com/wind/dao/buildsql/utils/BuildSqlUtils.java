package com.wind.dao.buildsql.utils;

import com.wind.dao.buildsql.config.BuildSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 14:09
 **/
@Slf4j
public class BuildSqlUtils {

    public static String getBuildSql(BuildSqlConfiguration buildSqlConfiguration, String mappedStatementId, Object parameterObject) {
        MappedStatement mappedStatement = buildSqlConfiguration.getMappedStatement(mappedStatementId);
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        return BuildSqlUtils.formatSql(boundSql.getSql(), buildSqlConfiguration, boundSql);
    }

    public static void xmlMapperBuild(Resource resource1, BuildSqlConfiguration buildSqlConfiguration) {
        try {
            XMLMapperBuilder builder = new XMLMapperBuilder(resource1.getInputStream(), buildSqlConfiguration, resource1.toString(), buildSqlConfiguration.getSqlFragments());
            builder.parse();
        } catch (Exception e) {
            log.error("buildsql xmlMapperBuiler,Resource:{},  error:", resource1.toString(), e);
        }
    }

    /**
     * 将占位符替换成参数值
     *
     * @param sql
     * @param configuration
     * @param boundSql
     * @return
     */
    public static String formatSql(String sql, Configuration configuration, BoundSql boundSql) {

        //美化sql
        sql = beautifySql(sql);

        //填充占位符, 目前基本不用mybatis存储过程调用,故此处不做考虑
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<String> parameters = new ArrayList<>();
        if (parameterMappings != null) {
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    //  参数值
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    //  获取参数名称
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 获取参数值
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        // 如果是单个值则直接赋值
                        value = parameterObject;
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }

                    if (value instanceof Number) {
                        parameters.add(String.valueOf(value));
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("'");
                        if (value instanceof Date) {
                            builder.append(dateTimeFormatter.get().format((Date) value));
                        } else if (value instanceof String) {
                            builder.append(value);
                        }
                        builder.append("'");
                        parameters.add(builder.toString());
                    }
                }
            }
        }

        for (String value : parameters) {
            sql = sql.replaceFirst("\\?", value);
        }
        return sql;
    }

    public static void main(String[] args) {

    }

    /**
     * 格式化sql日志
     *
     * @param sqlId
     * @param sql
     * @param costTime
     * @return
     */
    private void formatSqlLog(String sqlId, String sql, long costTime, Object obj) {
        String sqlLog = "==> " + sql;
        StringBuffer result = new StringBuffer();
        if (obj instanceof List) {
            List list = (List) obj;
            int count = list.size();
            result.append("<==      Total: " + count);
        } else if (obj instanceof Integer) {
            result.append("<==      Total: " + obj);
        }
        result.append("      Spend Time ==> " + costTime + " ms");
    }

    public static String beautifySql(String sql) {
        sql = sql.replaceAll("[\\s\n ]+", " ");
        return sql;
    }

    private static ThreadLocal<SimpleDateFormat> dateTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

}
