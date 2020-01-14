package com.wind.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 16:20
 **/
@EnableDubbo(scanBasePackages = {"com.wind.dubbo.impl"})
@Configuration
public class DubboConfig {

    @Value("${dubbo.registry.address}")
    private String dubboRegistryAddress;
    @Value("${dubbo.applicatiom.name}")
    private String dubboApplicationName;

    @Bean
    public ApplicationConfig getApplicationConfig() {
        System.out.println("=================================================");
        System.out.println("=================================================");
        System.out.println("=================================================");
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboApplicationName);
        return applicationConfig;
    }

    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        return providerConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress(dubboRegistryAddress);
        registryConfig.setPort(2181);
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        return protocolConfig;
    }
}
