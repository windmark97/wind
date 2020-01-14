package com.wind.api.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis的mapper扫描路径
 * @author quanjic
 * @version v1.0
 * @since 2018/12/8 10:24
 **/
@Configuration
@MapperScan(basePackages = {"com.wind.dao.mapper"})
public class MybatisConfig {

}
