package com.wind.dao.mapper;

import com.wind.dao.model.AdvertArea;
import com.wind.dao.model.AdvertAreaQuery;

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
