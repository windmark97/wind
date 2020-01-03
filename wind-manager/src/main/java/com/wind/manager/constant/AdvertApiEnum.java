package com.wind.manager.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索推广平台枚举类型
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:19
 **/
@AllArgsConstructor
@Getter
public enum AdvertApiEnum {
    //百度
    API_BAIDU_STRATEGY(1, "advertBaiduStrategy", "advertBaiduDataHandler", "百度"),
    //360
    API_360_STRATEGY(2, "advert360ReportStrategy", "advert360DataHandler", "360"),
    //神马
    API_SHENMA_STRATEGY(3, "advertShenmaStrategy", "advertShenmaDataHandler", "神马"),
    //搜狗
    API_SOUGO_STRATEGY(4, "advertSougoStrategy", "advertSougoDataHandler", "搜狗");
    /**
     *搜索平台
     */
    private Integer apiPlat;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 处理类名称
     */
    private String handlerName;
    /**
     * 描述
     */
    private String describe;

    /**
     * 通过apiPlat 遍历获取枚举对象
     * @param apiPlat api渠道
     * @return
     */
    public static AdvertApiEnum ergodicOf(Integer apiPlat) {
        for (AdvertApiEnum paymentTypeEnum : values()) {
            if (paymentTypeEnum.getApiPlat().equals(apiPlat)) {
                return paymentTypeEnum;
            }
        }
        return null;
    }
}
