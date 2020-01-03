package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告数据对象
 * sem_report_day
 *
 * @author HuangYongJie
 */
@Data
public class SemReportDay implements Serializable {

    private static final long serialVersionUID = 3203386686614868666L;

    /**
     * 报告Id
     */
    private Long reportId;

    /**
     * 报告日期，格式yyyyMMdd
     */
    private Integer reportDate;
    /**
     * 推广日期
     */
    private String publicizeDate;

    /**
     * 用户 id
     */
    private Integer userId;

    /**
     * 渠道
     */
    private Integer channelId;
    /**
     * 服务分类Id
     */
    private Integer servCategId;

    /**
     * 1级产品分类Id
     */
    private Integer categOneId;

    /**
     * 城市Id
     */
    private Integer cityId;


    /**
     * 展现次数
     */
    private Integer impression;

    /**
     * 点击次数
     */
    private Integer click;

    /**
     * 消费金额
     */
    private Integer cost;

    /**
     * 平均点击价格
     */
    private Integer cpc;

    /**
     * 千次展现成本
     */
    private Integer cpm;

    /**
     * 点击率
     */
    private BigDecimal ctr;


    /**
     * 创建/更新日期
     */
    private Date createTime;

    /**
     * 推广单元
     */
    private String groupName;

    /**
     * 推广计划
     */
    private String creativeName;
    /**
     * 城市
     */
    private String city;
    /**
     * 省份Id
     */
    private Integer provinceId;

}
