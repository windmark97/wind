package com.wind.service.service;

import com.zmn.advert.model.entity.advertapi.AdvertArea;
import com.zmn.advert.model.entity.advertapi.query.AdvertAreaQuery;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 14:12
 **/
public interface AdvertAreaService {
    /**
     * 根据主键查询
     * @param areaId
     * @return
     */
    AdvertArea findAdvertAreaByKey(Integer areaId);

    /**
     * 分页查询
     * @param query
     * @return
     */
    List<AdvertArea> listPage(AdvertAreaQuery query);

    /**
     * 根据条件查询所有
     * @param query
     * @return
     */
    List<AdvertArea> listAll(AdvertAreaQuery query);
}
