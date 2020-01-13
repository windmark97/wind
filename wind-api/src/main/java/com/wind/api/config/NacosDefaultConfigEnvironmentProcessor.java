package com.wind.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/13 12:58
 **/
@Slf4j
public class NacosDefaultConfigEnvironmentProcessor  implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String clientClasspath = "/config/nacos-client.properties";
        try (InputStream input = NacosDefaultConfigEnvironmentProcessor.class.getResourceAsStream(clientClasspath)) {
            Properties properties = new Properties();
            properties.load(input);
            environment.getPropertySources().addLast(new PropertiesPropertySource("classpath:" + clientClasspath, properties));
        } catch (IOException e) {
            log.error("#nacos#config load {} fail", clientClasspath, e);
        }

        //密钥访问
        String keyPath = "/a/config-repo/acm-access-key.properties";
        try {
            File keyPathFile = new File(keyPath);
            if (keyPathFile.isFile()) {
                Properties accessKeyProperties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(keyPathFile));
                environment.getPropertySources().addLast(new PropertiesPropertySource(keyPath, accessKeyProperties));
            } else {
                log.warn("#nacos#config FILE NOT FOUND {}", keyPath);
            }
        } catch (IOException e) {
            log.error("#nacos#config load {} fail", keyPath, e);
        }
    }
}
