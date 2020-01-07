package com.wind.dao.buildsql.config;

import com.wind.dao.buildsql.utils.BuildSqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileNotFoundException;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 11:13
 **/
@Slf4j
@Configuration
public class BuildSqlConfig {


    private static final String classPath = "classpath:impala/**.xml";

    private BuildSqlConfiguration buildSqlConfiguration;

    @Bean(name = "buildSqlConfiguration")
    public BuildSqlConfiguration initBuildSql() {
        buildSqlConfiguration = new BuildSqlConfiguration();
        try {
            Resource[] resourceArr = new PathMatchingResourcePatternResolver().getResources(classPath);
            for (Resource resource1 : resourceArr) {
                BuildSqlUtils.xmlMapperBuild(resource1,buildSqlConfiguration);
            }
        } catch (FileNotFoundException fe){
            log.info("not find builsql xml!");
        } catch(Exception e) {
            log.error("init build sql error:", e);
        }
        return buildSqlConfiguration;
    }



}
