package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 搜索推广账号
 * advert_account
 * @author HuangYongJie
 */
@Data
public class AdvertAccount implements Serializable {

    private static final long serialVersionUID = 8812035515004629272L;
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
