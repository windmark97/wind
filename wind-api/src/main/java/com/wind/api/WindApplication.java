package com.wind.api;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 17:15
 **/
//@EnableScheduling
@EnableDubbo
@SpringBootApplication(scanBasePackages={"com.wind"})
public class WindApplication {

    public static void main(String[] args) {
       try{
           SpringApplication.run(WindApplication.class, args);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
