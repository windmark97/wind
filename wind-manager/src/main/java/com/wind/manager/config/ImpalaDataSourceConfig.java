package com.wind.manager.config;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author heguanghua
 * Date: 2019/06/24 17:16
 */
//@Configuration
public class ImpalaDataSourceConfig {

    //@Bean(name = "impalaDataSource")
    //@ConfigurationProperties(prefix = "spring.datasource.impala")
    public DataSource impalaDataSource() {
        //return new ComboPooledDataSource();
        return new HikariDataSource();
    }

}
