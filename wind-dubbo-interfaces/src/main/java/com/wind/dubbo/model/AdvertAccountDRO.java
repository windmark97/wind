package com.wind.dubbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 16:11
 **/
public class AdvertAccountDRO implements Serializable {

    /**
     * 主键id
     */
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
     * 平台类型：{1：百度，2:360, 3:神马，4:搜狗}
     */
    private Integer advertType;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 权限token，360的是apikey
     */
    private String token;

    /**
     * 360密码加密盐
     */
    private String apiSecret;

    /**
     * 有效标志
     */
    private Integer activeMark;

    /**
     * 返点率
     */
    private BigDecimal returnRate;

    /**
     * 创建时间
     */
    private Date creatTime;
}
