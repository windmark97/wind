package com.wind.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wind.common.constant.StatusConsts;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 13:24
 **/

@Data
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = -1222354456575757L;

    /**
     * 数值结果,如200,404
     */
    private Integer status;

    /**
     * 文本信息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    public static ResponseDTO success(){
        return new ResponseDTO();
    }

    public static <T> ResponseDTO<T> success(T data){
        return new ResponseDTO(data);
    }

    public static <T> ResponseDTO<T> success(T data, String message){
        return new ResponseDTO(StatusConsts.STATUS_SUCCESS, data, message);
    }

    public static ResponseDTO fail(int status, String message){
        return new ResponseDTO(status, null, message);
    }

    public static ResponseDTO fail(String message){
        return new ResponseDTO(StatusConsts.STATUS_ERROR, null, message);
    }

    public static ResponseDTO fail(){
        return new ResponseDTO(StatusConsts.STATUS_ERROR, null, null);
    }

    public ResponseDTO() {
        this.status = StatusConsts.STATUS_SUCCESS;
    }

    public ResponseDTO(T data) {
        this.status = StatusConsts.STATUS_SUCCESS;
        this.data = data;
    }


    public ResponseDTO(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return 如果成功返回true，否则返回 false。
     */
    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == StatusConsts.STATUS_SUCCESS;
    }


}
