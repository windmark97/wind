package com.wind.manager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * Created by huangyongjie on 2018/7/25.
 */
public class LogMgr {

    /**
     * 获取logger对象
     *
     * @param c
     * @return
     */
    public static Logger getLog(Class c) {
        return LoggerFactory.getLogger(c);
    }

    public static void debug(Class c, String s) {
        getLog(c).debug(s);
    }

    public static void error(Class c, String s) {
        getLog(c).error(s);
    }

    public static void error(Class c, String s, Throwable e) {
        getLog(c).error(s, e);
    }

    public static void info(Class c, String s) {
        getLog(c).info(s);
    }

    public static void warn(Class c, String s) {
        getLog(c).warn(s);
    }

}
