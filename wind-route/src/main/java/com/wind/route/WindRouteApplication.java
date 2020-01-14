package com.wind.route;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Slf4j
@EnableDubbo
@SpringBootApplication(scanBasePackages={"com.wind"})
public class WindRouteApplication {

    public static void main(String[] args) {
      try{
          SpringApplication.run(WindRouteApplication.class, args);
      }catch (Exception e){
           e.printStackTrace();
      }
    }

}
