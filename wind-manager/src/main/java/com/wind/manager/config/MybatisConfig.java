package com.wind.manager.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author quanjic
 * @version v1.0
 * @since 2018/12/8 10:24
 **/
@Configuration
@MapperScan(basePackages = {"com.zmn.advert.persistence"})
public class MybatisConfig {

}
