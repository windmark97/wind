package com.wind.route;

import com.wind.route.netty.HttpServer;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 14:09
 **/
public class NettyRouteApplication {
    public static void main(String[] args) {
        System.out.println("启动..........");
        GenericApplicationContext context = new GenericApplicationContext();
        new ClassPathBeanDefinitionScanner(context).scan("com.wind");
        context.refresh();
        Integer port = 8082;
        System.out.println("启动netty..........");
        HttpServer httpServer = new HttpServer(port);
        httpServer.start();
    }
}
