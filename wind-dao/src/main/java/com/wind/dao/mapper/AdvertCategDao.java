package com.wind.dao.mapper;

import com.wind.dao.model.AdvertCateg; 

import java.util.List;

/**
 * 服务产品分类dao
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/27 14:21
 **/
public interface AdvertCategDao {

    AdvertCateg findByKey(Integer areaId);

    List<AdvertCateg> listAll(AdvertCateg advertCateg);

    Integer countByQuery(AdvertCateg advertCateg);

}
