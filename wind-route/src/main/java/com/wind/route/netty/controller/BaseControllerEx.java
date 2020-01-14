package com.wind.route.netty.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * @ClassName: BaseControllerEx
 * @Description: <br>
 * @DATE: 2019/8/30 14:36
 * @Author: hyj
 * @Version: 1.0
 */
@Slf4j
public class BaseControllerEx extends  BaseController{

    private static String aesPassword = "voip-sldf!i23iKl";

    /**
     * Ip白名单
     *
     * @param origIp
     * @param jsonObject
     * @return
     */
    public Map<String, Object> privatePostJson(String origIp, JSONObject jsonObject) {
        return null;
    }


    /**
     * Oauth 2.0 验证
     *
     * @param origIp
     * @param accessToken
     * @param jsonObject
     * @return
     */
    public Map<String, Object> protectPostJson(String origIp, String accessToken, JSONObject jsonObject) {

        return null;
    }

}
