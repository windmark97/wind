package com.wind.service.handler.callback.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.SemReportDay;
import com.wind.manager.exception.AdvertException;
import com.wind.service.handler.AdvertDataHandler;
import com.wind.service.handler.AdvertMappingCacheHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理搜索推广api返回结果基础类
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/22 15:44
 **/
@Slf4j
public abstract class BaseAdvertDataHandler implements AdvertDataHandler {

    /**
     * 100
     */
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    /**
     * 服务产品正则表达式
     */

    private static final String CATEG_SERV_REGEX = "\\[(.*?)\\]";
    /**
     * 服务产品默认值
     */
    private static final String CATEG_SERV_NAME_DEFAULT = "其他家电-其它";
    /**
     * 分隔符
     */
    private static final String STR_SPLITE_SYMBOL = "-";
    /**
     * 整型默认值0
     */
    protected static final Integer INT_VALUE_DEFAULT = 0;
    /**
     * 数组最小长度
     */
    private static final Integer ARR_MIN_SIZE = 1;
    /**
     * 服务分类的下标
     */
    public static final int SERV_CATEG_NAME_INDEX = 1;
    /**
     * 分类的数组的最小size
     */
    public static final int CATEG_NAME_MIN_SIZE = 1;
    /**
     * 产品分类下标
     */
    public static final int PRODUCT_CATEG_NAME_INDEX = 0;

    @Override
    public void processResultJsonStr(String jsonStr, AdvertBaseInfo info) {
        try {
            JSONObject resultJson = JSON.parseObject(jsonStr);
            checkStatus(resultJson);
            processResult(resultJson, info);
        } catch (Exception e) {
            log.error(JSON.toJSONString(info.getApiCallParam()));
            log.error("userId:{},error:{}", info.getUserId(), e.getMessage(), e);
            throw new AdvertException(e.getMessage());
        }
    }

    @Override
    public void processResultFile(ByteArrayInputStream inputStream, AdvertBaseInfo info) {
        log.info("start process inputStream data,userName:{}", info.getUserName());
    }

    /**
     * 判断返回结果是否失败
     *
     * @param object
     * @return
     */
    protected void checkStatus(JSONObject object) {
        log.info("result status is success!");
    }

    /**
     * 处理失败的情况下
     */
    protected void processFailedResult(String message) {
        throw new AdvertException(message);
    }

    /**
     * 处理成功的返回数据
     *
     * @param resultJson
     */
    protected void processResult(JSONObject resultJson, AdvertBaseInfo info) {
        log.info("url:{} result failed,userName:{},result:{}", info.getUrl(), info.getUserName(), resultJson.toJSONString());
    }

    protected String[] spliteStr(String str) {
        return str.split(STR_SPLITE_SYMBOL);
    }

    protected Integer parseDate(String dataStr) {
        if (StringUtils.isEmpty(dataStr)) {
            return 0;
        }
        String str = dataStr.replaceAll(STR_SPLITE_SYMBOL, "");
        return Integer.valueOf(str);
    }


    /**
     * 处理城市数据
     *
     * @param resultData
     * @param cityIdStr
     * @param cityName
     */
    protected void setCityId(SemReportDay resultData, String cityIdStr, String cityName) {
        if (cityIdStr == null) {
            log.info("campaignName:{} is not find ", cityName);
            setDefaultCity(resultData, cityName);
        } else {
            String[] cityIds = cityIdStr.split("-");
            if (cityIds == null || cityIds.length < ARR_MIN_SIZE) {
                setDefaultCity(resultData, cityName);
                log.info("not find area：cityName:{},cityIdStr:{}", cityName, cityIdStr);
            } else {
                if (cityIds.length == 1) {
                    resultData.setProvinceId(Integer.valueOf(cityIds[0]));
                } else {
                    resultData.setProvinceId(Integer.valueOf(cityIds[1]));
                }
                resultData.setCityId(Integer.valueOf(cityIds[0]));
                resultData.setCity(cityName);
            }
        }
    }

    protected void setDefaultCity(SemReportDay resultData, String cityName) {
        resultData.setProvinceId(INT_VALUE_DEFAULT);
        resultData.setCityId(INT_VALUE_DEFAULT);
        resultData.setCity(cityName);
        resultData.setCreativeName(String.valueOf(INT_VALUE_DEFAULT));
    }

    protected void setDefaultCategData(SemReportDay resultData) {
        resultData.setCategOneId(INT_VALUE_DEFAULT);
        resultData.setServCategId(INT_VALUE_DEFAULT);
    }

    /**
     * 处理服务产品分类
     *
     * @param resultData
     * @param groupName
     */
    protected void processCategDate(SemReportDay resultData, String groupName, Map<Object, Object> servCategMap, Map<Object, Object> productCategMap) {
        resultData.setGroupName(groupName);
        if (StringUtils.isEmpty(groupName)) {
            setDefaultCategData(resultData);
        } else {
            String categServName = matchCategServ(groupName);
            String[] categArr = spliteStr(categServName);
            if (categArr.length < CATEG_NAME_MIN_SIZE) {
                setDefaultCategData(resultData);
                return;
            }
            String productCategName = categArr[PRODUCT_CATEG_NAME_INDEX];
            String servCategName;
            //只有一个分类，则该分类既是产品分类，也是服务分类
            if (categArr.length == CATEG_NAME_MIN_SIZE) {
                servCategName = productCategName;
            } else {
                servCategName = categArr[SERV_CATEG_NAME_INDEX];
            }
            String productCategIdStr = (String) productCategMap.get(productCategName);
            String servCategIdStr = (String) servCategMap.get(servCategName);
            Integer productCategId;
            Integer servCategId;
            if (productCategIdStr == null) {
                productCategId = processCategByMap(productCategName, productCategMap);
            } else {
                productCategId = Integer.valueOf(productCategIdStr);
            }
            if (servCategIdStr == null) {
                servCategId = processCategByMap(servCategName, servCategMap);
            } else {
                servCategId = Integer.valueOf(servCategIdStr);
            }
            resultData.setServCategId(servCategId);
            resultData.setCategOneId(productCategId);
        }
    }

    /**
     * 匹配服务分类
     *
     * @param groupName
     * @return
     */
    protected String matchCategServ(String groupName) {
        Pattern pattern = Pattern.compile(CATEG_SERV_REGEX);
        Matcher matcher = pattern.matcher(groupName);
        String categServName;
        if (matcher.find()) {
            categServName = matcher.group();
        } else {
            categServName = CATEG_SERV_NAME_DEFAULT;
        }
        return categServName;
    }

    /**
     * 处理服务分类
     *
     * @param name
     * @param categMap
     * @return
     */
    private Integer processCategByMap(String name, Map<Object, Object> categMap) {
        String value = null;
        String keyName;
        for (Map.Entry<Object, Object> entry : categMap.entrySet()) {
            keyName = (String) entry.getKey();
            if (name.indexOf(keyName) != -1) {
                value = (String) entry.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    /**
     * 金钱元转分
     *
     * @param value
     * @return
     */
    protected Integer formatMoney(BigDecimal value) {
        return value.multiply(ONE_HUNDRED).intValue();
    }
    protected Integer formatMoney(BigDecimal value,BigDecimal returnRate) {
        value = value.multiply(ONE_HUNDRED);
        BigDecimal realCost = value.divide(returnRate, INT_VALUE_DEFAULT, RoundingMode.HALF_UP);
        return realCost.intValue();
    }

    /**
     * 处理城市数据
     *
     * @param resultData
     * @param campaignName
     */
    protected void processCityData(AdvertMappingCacheHandler advertMappingCacheHandler, SemReportDay resultData, String campaignName) {
        if (!StringUtils.isEmpty(campaignName)) {
            resultData.setCreativeName(campaignName);
            String[] cityArr = spliteStr(campaignName);
            String cityIdStr = advertMappingCacheHandler.getAreaId(cityArr[1]);
            setCityId(resultData, cityIdStr, cityArr[1]);
        } else {
            setDefaultCity(resultData, campaignName);
        }
    }

}
