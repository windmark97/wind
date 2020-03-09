package com.wind.dao.model;

import lombok.Data;

import java.util.Map;

/**
 * 搜索推广基本配置
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:04
 **/
@Data
public class ApiCallParam {
    /**
     * url地址
     */
    private String url;

    /**
     * 消息头
     */
    private Map<String, Object> headerMap;

    /**
     * 数据body
     */
    private Map<String, Object> dataMap;

    /**
     * 编码
     */
    private String encodeFormat;

}
