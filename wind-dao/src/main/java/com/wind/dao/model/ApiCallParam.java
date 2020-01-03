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

    private String url;

    private Map<String, Object> headerMap;

    private Map<String, Object> dataMap;

    private String encodeFormat;

}
