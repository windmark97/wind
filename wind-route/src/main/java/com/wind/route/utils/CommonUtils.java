package com.wind.route.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: CommonUtils
 * @Description: TODO
 * @DATE: 2019/6/4 16:27
 * @Author:hyj97 on 2019/6/4
 * @Version: 1.0
 */
public class CommonUtils {

    public static Map<String,Object> buildResultMap(int errorCode,String errorMsg){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",errorCode);
        resultMap.put("message",errorMsg);
        resultMap.put("data",null);
        return resultMap;
    }
    /**
     * 获取访问者IP地址
     * <p>在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。</p>
     * <p>本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)。</p>
     * <p>如果还不存在则调用Request.getRemoteAddr()。</p>
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip)&& !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
    public static String stampToDate(Integer s){
        String res = "";
        if(s==null){
            return res;
        }
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s+"000");
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }catch (Exception e){
            LogMgr.error(CommonUtils.class,e.getMessage(),e);
        }
        return res;
    }
    public static Integer dateTostamp(String dataStr){
        Integer res = 0;
        if(dataStr==null){
            return res;
        }
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dataStr);
            res = (int)(date.getTime()/1000);
        }catch (Exception e){
            LogMgr.error(CommonUtils.class,e.getMessage(),e);
        }
        return res;
    }
    public static Long getLongValue(Object obj){
        if(obj==null){
            return null;
        }
        if(obj instanceof Integer){
            return (long)((Integer)obj).intValue();
        }else if(obj instanceof Long){
            return (Long)obj;
        }else{
            return Long.getLong(obj.toString());
        }

    }
}
