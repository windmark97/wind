package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;

/**
 * advert的job参数
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 18:17
 **/
@Data
public class AdvertJobParam implements Serializable {
    private static final long serialVersionUID = 979902466188656895L;

    /**
     * 搜索推广平台类型
     */
    private Integer apiType;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;


}
