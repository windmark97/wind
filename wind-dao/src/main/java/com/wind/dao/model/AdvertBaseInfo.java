package com.wind.dao.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 *  搜索推广操作基础信息
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:20
 **/
@Data
public class AdvertBaseInfo {

    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 子账号用户名
     */
    private String subUserName;

    /**
     * 平台类型
     */
    private Integer advertType;

    /**
     *  渠道id
     */
    private Integer channelId;

    /**
     * token权限，360的是apikey
     */
    private String token;

    /**
     * 返点率
     */
    private BigDecimal returnRate;

    /**
     * api密盐
     */
    private String apiSecret;


    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 360的临时token
     */
    private String accessToken;

    /**
     * http请求url
     */
    private String url;

    /**
     * 页码
     */
    private Integer pageIndex;
    /**
     * 保存过数据
     */
    private Boolean saveData = false;

    /**
     * 计数器
     */
    private Integer counter = 0;

    private Map<String,Object> paramsMap;

    private ApiCallParam apiCallParam;

}
