package com.wind.dubbo.model;

import lombok.Data;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 16:13
 **/
@Data
public class AdvertAccountDIO {
    /**
     * 主键id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 平台类型：{1：百度，2:360, 3:神马，4:搜狗}
     */
    private Integer advertType;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 有效标志
     */
    private Integer activeMark;
}
