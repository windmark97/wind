package com.wind.dao.buildsql.proxy;

import com.zmn.performance.persistence.buildsql.config.BuildSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.ErrorContext;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.dao.support.DaoSupport;

import static org.springframework.util.Assert.notNull;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 13:27
 **/
@Slf4j
public class BuildSqlMapperFactoryBean<T> extends DaoSupport implements FactoryBean<T> {
    private Class<T> mapperInterface;

    private BuildSqlConfiguration buildSqlConfiguration;

    public BuildSqlMapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    private boolean addToConfig = true;

    @Override
    public T getObject() throws Exception {
        log.debug("---------------------------获取对象:{}---------------------------",this.mapperInterface.getName());
        return buildSqlConfiguration.getMapper(this.mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        notNull(this.buildSqlConfiguration, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        log.debug("---------------------------checkDaoConfig:{}---------------------------",this.mapperInterface.getName());
        if (this.addToConfig && !buildSqlConfiguration.hasMapper(this.mapperInterface)) {
            try {
                buildSqlConfiguration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }


    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public BuildSqlConfiguration getBuildSqlConfiguration() {
        return buildSqlConfiguration;
    }

    public void setBuildSqlConfiguration(BuildSqlConfiguration buildSqlConfiguration) {
        this.buildSqlConfiguration = buildSqlConfiguration;
    }

    public boolean isAddToConfig() {
        return addToConfig;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }
}
