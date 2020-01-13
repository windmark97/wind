package com.wind.dao.model;

import com.wind.common.entity.Pages;
import lombok.Data;
import org.omg.CORBA.INTERNAL;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  SemReportDay查询条件
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 13:39
 **/
@Data
public class SemReportDayQuery extends Pages implements Serializable {

    private static final long serialVersionUID = 1797659908633963560L;

    /**
     * 报告Id
     */
    private Long reportId;

    private Integer userId;

    /**
     * 报告日期，格式yyyyMMdd
     */
    private Integer date;

    /**
     * 广告平台，1百度，2,360，3神马，4,搜狗5五八，6美团
     */
    private Short advertPlat;

    /**
     * 服务分类Id
     */
    private Integer servCategId;

    /**
     * 1级产品分类Id
     */
    private Integer categOneId;


    /**
     * 省份Id
     */
    private Integer provinceId;

    /**
     * 城市Id
     */
    private Integer cityId;

    /**
     * 城市
     */
    private String city;


    /**
     * 创建/更新日期
     */
    private Date updateTime;

    private Integer channelId;
    private Integer reportDate;

    private List<Integer> reportIds;
    private Integer startDate;
    private Integer endDate;
}
