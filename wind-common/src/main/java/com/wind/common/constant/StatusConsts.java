package com.wind.common.constant;

/**
 * 状态
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 13:26
 **/
public class StatusConsts {

    /**
     * 失败
     */
    public final static int STATUS_ERROR = 100;
    /**
     * 系统繁忙/系统错误
     */
    public final static int STATU_SERROR_SYSTEM = 101;
    /**
     * 成功
     */
    public final static int STATUS_SUCCESS = 200;
    /**
     * 成功且进入下一步
     */
    public final static int STATUS_SUCCESS_NEXT = 201;

    /** 限流：流量限流异常（资源访问量） */
    public final static int FLOW_QPS_STATUS = 301;

    /** 限流：线程限流异常（资源执行的最大线程数） */
    public final static int FLOW_TPS_STATUS = 311;

    /** 熔断：资源访问RT超过限制（1秒内5次超过RT值） */
    public final static int DEGRADE_GRADE_RT_STATUS = 321;
    /** 熔断：资源访问异常比例超过限制 */
    public final static int DEGRADE_GRADE_EXCEPTION_RATIO_STATUS = 322;
    /** 熔断：资源访问异常数超过限制 */
    public final static int DEGRADE_GRADE_EXCEPTION_COUNT_STATUS = 322;

    /**
     * 未知错误
     */
    public final static int ERROR_UNKNOWN = 40000;
    /**
     * 非法请求
     */
    public final static int ERROR_ILLEGAL = 40001;
    /**
     * 非法字符
     */
    public final static int ERROR_CHARS = 40002;
    /**
     * 参数错误
     */
    public final static int ERROR_PARAMS = 40003;
    /**
     * 状态错误
     */
    public final static int ERROR_STATUS = 40004;
    /**
     * 没有数据
     */
    public final static int ERROR_EMPTY = 40005;
    /**
     * 数据一致性错误
     */
    public final static int ERROR_DATA = 40006;
    /**
     * 没有权限
     */
    public final static int ERROR_PERMIT = 40007;
}
