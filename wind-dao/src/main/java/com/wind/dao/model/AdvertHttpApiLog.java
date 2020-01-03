package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索推广数据集获取日志类
 * advert_http_api_log
 * @author
 */
@Data
public class AdvertHttpApiLog implements Serializable {

    private static final long serialVersionUID = 7842298718061038083L;

    /**
     * 主键id
     */
    private Long logId;

    /**
     * 账号
     */
    private Integer userId;

    /**
     * 数据日期
     */
    private Integer date;

    /**
     * 平台类型
     */
    private Integer platType;
    /**
     * 结束时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 请求参数
     */
    private String httpParam;
    /**
     * 信息
     */
    private String message;

    /**
     * 创建日期
     */
    private Date createTime;

 }
