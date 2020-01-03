package com.wind.dao.model;

import com.zmn.common.utils.pager.Pages;
import lombok.Data;

import java.io.Serializable;

/**
 * AdvertHttpApiLog查询条件
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/28 13:59
 **/
@Data
public class AdvertHttpApiLogQuery extends Pages implements Serializable {

    private static final long serialVersionUID = 8061552689802138679L;

    /**
     * 主键id
     */
    private Long logId;

    /**
     * 账号
     */
    private String userId;

    /**
     * 数据日期
     */
    private Integer date;

    /**
     * 平台类型
     */
    private Integer platType;


    private String startDate;

    private String endDate;


}
