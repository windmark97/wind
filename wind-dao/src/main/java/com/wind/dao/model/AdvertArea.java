package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  广告区域对象
 * @author HuangYongJie
 */
@Data
public class AdvertArea implements Serializable {

    private static final long serialVersionUID = 8521697288273275204L;

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
    private Short level;

    /**
     * 创建/更新日期
     */
    private Date updateTime;

}
