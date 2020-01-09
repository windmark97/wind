package com.wind.dao.model;

import com.wind.common.entity.Pages;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * AdvertAccount查询条件
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 17:47
 **/
@Data
public class AdvertAccountQuery extends Pages implements Serializable {
    private static final long serialVersionUID = 3063637301249751669L;
    /**
     * 主键id
     */
    private Integer userId;

    private List<Integer> userIds;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 搜索推广平台类型
     */
    private Integer advertType;
    /**
     * 有效标志
     */
    private Integer activeMark;

    private String startDate;

    private String endDate;

}
