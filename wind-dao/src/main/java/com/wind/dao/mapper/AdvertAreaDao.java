package com.wind.dao.mapper;

import com.zmn.advert.model.entity.advertapi.AdvertArea;
import com.zmn.advert.model.entity.advertapi.query.AdvertAreaQuery;

import java.util.List;

/**
 * 搜索推广区域数据
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 13:41
 **/
public interface AdvertAreaDao {

    AdvertArea findByKey(Integer areaId);

    List<AdvertArea> listPage(AdvertAreaQuery query);

    Integer countByQuery(AdvertAreaQuery query);

}
