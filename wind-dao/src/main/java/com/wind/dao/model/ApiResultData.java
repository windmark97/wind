package com.wind.dao.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 搜索推广数据返回通用结果
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:46
 **/
@Data
public class ApiResultData {

    private String advertAccount;

    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 展现
     */
    private Integer views;

    /**
     * 点击
     */
    private Integer clicks;

    /**
     * 消费
     */
    private BigDecimal totalCost;


    private Integer advertType;

    /**
     * 渠道
     */
    private Integer channal;

    /**
     * 服务
     */
    private String serveName;

    /**
     * 产品
     */
    private String productName;

    /**
     * 日期
     */
    private String date;

}
