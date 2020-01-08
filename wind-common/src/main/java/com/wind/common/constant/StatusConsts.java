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

    /**
     * 需要GET请求
     */
    public final static int ERROR_NOTGET = 40011;
    /**
     * 需要POST请求
     */
    public final static int ERROR_NOPOST = 40012;
    /**
     * 需要HTTPS请求
     */
    public final static int ERROR_NOTHTTPS = 40013;
    /**
     * 不合法的URL
     */
    public final static int ERROR_URL_ILLEGAL = 40014;

    /**
     * 图片验证码错误
     */
    public final static int ERROR_ICAPTCHA = 40021;
    /**
     * 短信验证码错误
     */
    public final static int ERROR_VALIDCODE = 40022;

    /**
     * 输入格式不符合要求(通用)
     */
    public final static int ERROR_PATTERN = 40100;
    /**
     * 不是数字类型
     */
    public final static int ERROR_PATTERN_NUMBER = 40101;
    /**
     * 手机格式错误
     */
    public final static int ERROR_PATTERN_MOBILE = 40102;
    /**
     * 邮箱格式错误
     */
    public final static int ERROR_PATTERN_MAIL = 40103;
    /**
     * 用户名不符合要求
     */
    public final static int ERROR_PATTERN_USERNAME = 40104;
    /**
     * 密码不符合要求
     */
    public final static int ERROR_PATTERN_PASSWORD = 40105;
    /**
     * 图片校验码不符合要求
     */
    public final static int ERROR_PATTERN_CAPTCHA = 40106;
    /**
     * 短信验证码不符合要求
     */
    public final static int ERROR_PATTERN_VALIDCODE = 40107;

    /**
     * 用户不存在
     */
    public final static int ERROR_NOTUSER = 40201;
    /**
     * 手机号未验证
     */
    public final static int ERROR_MOBILE_NOTAUTH = 40202;
    /**
     * 邮箱未验证
     */
    public final static int ERROR_MAIL_NOTAUTH = 40203;
    /**
     * 当前密码错误
     */
    public final static int ERROR_OLDPASSWORD = 40204;
    /**
     * 未开通权限
     */
    public final static int ERROR_ADMIN_LOGIN_NO = 40205;

    /**
     * 用户名已存在
     */
    public final static int ERROR_EXIST_USERNAME = 40301;
    /**
     * 手机号已存在
     */
    public final static int ERROR_EXIST_MOBILE = 40302;
    /**
     * 邮箱已存在
     */
    public final static int ERROR_EXIST_MAIL = 40303;
    /**
     * 新密码不能与当前密码相同
     */
    public final static int ERROR_UPDATED_USERNAME = 40304;

    /**
     * 登录，用户或密码为空
     */
    public final static int FAIL_LOGIN_USERPWD_EMPTY = 41001;
    /**
     * 登录，手机号未验证
     */
    public final static int FAIL_LOGIN_NOTAUTHMOBILE = 41002;
    /**
     * 登录，邮箱地址未验证
     */
    public final static int FAIL_LOGIN_NOTAUTHMAIL = 41003;
    /**
     * 登录，密码错误
     */
    public final static int FAIL_LOGIN_PASSWORD = 41004;
    /**
     * 登录，用户已经被禁用或锁定
     */
    public final static int FAIL_LOGIN_DISABLE = 41005;
    /**
     * 登录，密码错误超过5次,需要半个小时
     */
    public final static int FAIL_LOGIN_TOO_MANY_TIMES = 41006;
    /**
     * 登录，微信登录失败
     */
    public final static int FAIL_LOGIN_WEIXIN = 41007;
    /**
     * 登录，Token验证失败
     */
    public final static int FAIL_LOGINTOKEN = 41008;

    /**
     * 用户注册失败/新建用户失败
     */
    public final static int FAIL_REGISTER = 41100;

    /**
     * 未登录
     */
    public final static int FAIL_NOTLOGIN = 41200;
    /**
     * 需要关注subscribe(微信端)
     */
    public final static int FAIL_NOTSUBSCRIBE = 41201;

    /**
     * 不在服务范围
     */
    public final static int FAIL_SERVICE_POINT = 42101;

    /**
     * 描述: 师傅状态异常
     */
    public final static int FAIL_INVALID_MASTER = 50002;
    /**
     * 描述: 师傅会话状态异常
     */
    public final static int FAIL_INVALID_SESSION = 50003;


}
