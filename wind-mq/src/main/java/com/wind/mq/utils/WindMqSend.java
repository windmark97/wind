package com.wind.mq.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/2/20 16:39
 **/
public class WindMqSend {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendTopicMessage1")
    public String sendTopicMessage1(){
        String messageId=String.valueOf(UUID.randomUUID());
        String messageData="message: M A N ";
        String createTime= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> manMap=new HashMap<>();
        manMap.put("messageId",messageId);
        manMap.put("messageData",messageData);
        manMap.put("createTime",createTime);
        rabbitTemplate.convertAndSend("topicExchange","topic.man",manMap);
        return"ok";
    }

    @GetMapping("/sendTopicMessage2")
    public String sendTopicMessage2(){
        String messageId=String.valueOf(UUID.randomUUID());
        String messageData="message: woman is all ";
        String createTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> womanMap=new HashMap<>();
        womanMap.put("messageId",messageId);
        womanMap.put("messageData",messageData);
        womanMap.put("createTime",createTime);
        rabbitTemplate.convertAndSend("topicExchange","topic.woman",womanMap);
        return"ok";
    }
}
