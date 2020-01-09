package com.wind.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 17:15
 **/
@EnableScheduling
@SpringBootApplication
public class WindApplication {

    public static void main(String[] args) {
       try{
           SpringApplication.run(WindApplication.class, args);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
