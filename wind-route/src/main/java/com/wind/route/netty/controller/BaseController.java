package com.wind.route.netty.controller;

import com.alibaba.fastjson.JSONObject;
import com.wind.route.netty.controller.bean.ParameterOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * @ClassName: BaseController
 * @Description: <br>
 * @DATE: 2019/8/28 14:51
 * @Author: hyj
 * @Version: 1.0
 */
@Slf4j
public class BaseController {

    /**
     * 根据验证权限结果组装业务参数
     *
     * @param checkPermissionResultObjectMap
     * @param parameters
     * @param utype
     * @return
     */
    public static JSONObject assemblyParameters(Map<String, Object> checkPermissionResultObjectMap, JSONObject parameters, String utype) {

        if (parameters != null && StringUtils.isNotBlank(utype) && "1".equals(utype)) {
            String mUid = (String) checkPermissionResultObjectMap.get("uid");
            String mUserName = (String) checkPermissionResultObjectMap.get("userName");
            parameters.put("uid", mUid);
            parameters.put("OpenId", mUid);
            parameters.put("userName", mUserName);
        }
        return parameters;
    }


    public static Map<String, Object> invokeMethod(ParameterOrder parameterOrder) {
         return null;

    }


    /**
     * 获取服务名
     *
     * @param jsonObject
     * @return
     */
    public static String getServiceName(JSONObject jsonObject) {
         return null;
    }

    /**
     * 获取方法名
     *
     * @param jsonObject
     * @return
     */
    public static String getMethodName(JSONObject jsonObject) {
         return null;
    }


    /**
     * 获取接口参数
     *
     * @param jsonObject
     * @return
     */
    public static JSONObject getParameters(JSONObject jsonObject) {
        if (jsonObject.containsKey("parameters")) {
            return jsonObject.getJSONObject("parameters");
        } else if (jsonObject.containsKey("CmdType")) {
            return jsonObject;
        } else {
            return null;
        }
    }
}
