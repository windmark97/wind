package com.wind.route.netty.controller.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @ClassName: ParameterOrder
 * @Description: <br>
 * @DATE: 2019/8/30 14:38
 * @Author: hyj
 * @Version: 1.0
 */
public class ParameterOrder implements Serializable {
    private static final long serialVersionUID = 2823604289137689725L;
    private String gid;
    private String origIp;
    private String serviceName;
    private String methodName;
    private JSONObject jsonObject;

    public ParameterOrder() {
    }

    public ParameterOrder(String gid, String origIp, String serviceName, String methodName, JSONObject jsonObject) {
        this.gid = gid;
        this.origIp = origIp;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.jsonObject = jsonObject;
    }


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getOrigIp() {
        return origIp;
    }

    public void setOrigIp(String origIp) {
        this.origIp = origIp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParameterOrder{");
        sb.append("gid='").append(gid).append('\'');
        sb.append(", origIp='").append(origIp).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", jsonObject=").append(jsonObject);
        sb.append('}');
        return sb.toString();
    }
}
