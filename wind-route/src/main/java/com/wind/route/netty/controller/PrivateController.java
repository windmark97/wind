package com.wind.route.netty.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName: PrivateController
 * @Description: <br>
 * @DATE: 2019/8/30 14:40
 * @Author: hyj
 * @Version: 1.0
 */
@Controller
public class PrivateController extends BaseControllerEx{
    @ResponseBody
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public Map<String, Object> postJson(@RequestHeader(name = "Orig-Ip") String origIp,
                                        @RequestBody JSONObject jsonObject) {
        return privatePostJson(origIp, jsonObject);
    }
}
