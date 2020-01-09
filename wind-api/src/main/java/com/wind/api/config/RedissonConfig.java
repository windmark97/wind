package com.wind.api.config;

import jodd.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Redisson config.
 *
 * @author xiangjunming
 * @date 2019 /04/18 14:59:08
 */
//@Configuration
public class RedissonConfig {

    /**
     * 主机地址
     */
    @Value("${spring.redis.single.host}")
    private String host;

    /**
     * 端口号
     */
    @Value("${spring.redis.single.port}")
    private int port;

    /**
     * 如果Redis设置有密码
     */
    @Value("${spring.redis.single.password}")
    private String password;

    /**
     * 分布式Java锁和同步器
     *
     * @return the redisson client
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(StringUtil.isBlank(password) ? null : password);
        return Redisson.create(config);
    }
}
