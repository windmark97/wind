package com.wind.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广服务分类
 * advert_categ
 * @author
 */
@Data
public class AdvertCateg implements Serializable {

    private static final long serialVersionUID = 4916414655362674769L;

    /**
     * 主键id
     */
    private Integer pkId;

    /**
     * 分类id
     */
    private Integer categId;

    /**
     * 分类类型：{1：服务分类，2：产品分类}
     */
    private Integer type;

    /**
     * 产品名称
     */
    private String name;


    private Date updateTime;
}
