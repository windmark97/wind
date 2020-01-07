package com.wind.manager.constant;

/**
 * 线程池类型
 *
 * Created by hyj97 on 2018/12/14.
 */
public enum ThreadPoolTypeEnum {
    EXECUTE_COMMON_TASK(1,"定时任务工具"),
    EXECUTE_ANALYSIS_TASK(2,"analysis excel");
    private Integer type;
    private String message;
    ThreadPoolTypeEnum(Integer type, String message){
        this.type = type;
        this.message = message;
    }
    public Integer getType(){
        return type;
    }

    public void setType(Integer type){
        this.type = type;
    }


    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message  = message;
    }
}
