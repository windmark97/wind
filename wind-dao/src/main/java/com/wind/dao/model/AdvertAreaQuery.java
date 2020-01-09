package com.wind.dao.model;


import com.wind.common.entity.Pages;
import lombok.Data;

import java.io.Serializable;

/**
 * AdvertArea查询条件
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 13:37
 **/
@Data
public class AdvertAreaQuery  extends Pages implements Serializable {
    private static final long serialVersionUID = -866414500360469221L;
    /**
     * 区域id
     */
    private Integer areaId;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 区域代码
     */
    private String code;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 城市级次：1：省，2：市
     */
    private Integer level;

}
