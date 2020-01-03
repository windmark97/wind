package com.wind.dao.model;

import com.zmn.common.utils.pager.Pages;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
     * 2级产品分类Id
     */
    private Integer categTwoId;

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
     * 子公司
     */
    private Integer companyId;

    /**
     * 创建/更新日期
     */
    private Date updateTime;
}
