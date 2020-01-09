package com.wind.common.entity;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/9 13:50
 **/

import lombok.Data;

import java.io.Serializable;

@Data
public class Operator implements Serializable {

    private String operatorIP;
    private int operatorType;
    private int operatorId;
    private String operator;
    private String operatorRemark;
    private boolean isAuto = false;
    private boolean isSuper = false;
    private String token;
    private String sign;
    private int fromType;
}
