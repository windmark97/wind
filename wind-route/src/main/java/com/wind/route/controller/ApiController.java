package com.wind.route.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wind.common.dto.ResponseDTO;
import com.wind.common.utils.SpringContextUtils;
import com.wind.dubbo.interfaces.AdvertDataListService;
import com.wind.dubbo.model.AdvertAccountDIO;
import com.wind.dubbo.model.AdvertAccountDRO;
import com.wind.route.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseController
 * @Description:  前端请求统一入口
 * @DATE: 2019/6/4 14:59
 * @Author:hyj97 on 2019/6/4
 * @Version: 1.0
 */
@Slf4j
@Controller
public class ApiController {
    @ResponseBody
    @RequestMapping(value = "/api/{serviceName}/{methodName}", method = RequestMethod.POST)
    public Map<String, Object> controller(@PathVariable("serviceName") String serviceName,
                                          @PathVariable("methodName") String methodName,
                                          @RequestBody JSONObject jsonObject,
                                          HttpServletRequest request) {

        String ip = CommonUtils.getIp(request);
        Map<String,Object> resultMap =  invokeServiceMethod(serviceName,methodName,jsonObject,ip);
        return resultMap;
    }
    @Reference
    private AdvertDataListService advertDataListService;


    protected Map<String,Object> invokeServiceMethod(String serviceName,String methodName,JSONObject jsonObject,String ip){
        AdvertAccountDIO advertAccountDIO = new AdvertAccountDIO();
        advertAccountDIO.setUserId(10);
        ResponseDTO<List<AdvertAccountDRO>> resultList = advertDataListService.listAdvertAccount(advertAccountDIO);
        System.out.println(JSON.toJSONString(resultList.getData()));
        log.info("start deal with :{}:{}",serviceName,methodName);
        Map<String,Object> resultMap;
        Object service = SpringContextUtils.getBean(serviceName);
        if (service == null) {
            log.error("服务" + serviceName + "不存在");
            resultMap = CommonUtils.buildResultMap(7006, "服务" + serviceName + "不存在");
            return resultMap;
        }
        if (jsonObject == null) {
            Method method = ReflectionUtils.findMethod(service.getClass(), methodName);
            if(method==null){
                log.error("无参方法【" + serviceName+"." + methodName + "】不存在");
                resultMap= CommonUtils.buildResultMap(7007, "无参方法" + serviceName+"." + methodName + "不存在");

            }else{
                log.info("消费者：【{}】调用接口：{}.{}",ip,serviceName,methodName);
                resultMap = (Map<String, Object>) ReflectionUtils.invokeMethod(method, service);
            }

        } else {
            Method method = ReflectionUtils.findMethod(service.getClass(), methodName, Map.class);
            if(method==null){
                log.error("有参方法【" + serviceName+"." + methodName + "】不存在");
                resultMap = CommonUtils.buildResultMap(7007, "有参方法" + serviceName+"." + methodName + "不存在");
            }else{
                log.info("消费者：【{}】调用接口：{}.{}",ip,serviceName,methodName);
                resultMap = (Map<String, Object>) ReflectionUtils.invokeMethod(method, service, jsonObject);
            }
        }
        return resultMap;
    }
}
