package com.wind.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages={"com.wind"})
public class WindRouteApplication {

    public static void main(String[] args) {
      try{
          SpringApplication.run(WindRouteApplication.class, args);
      }catch (Exception e){
          log.error(e.getMessage(),e);
      }
    }

}
