package com.wind.service.service;

import com.zmn.advert.model.entity.advertapi.AdvertCateg;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/27 14:22
 **/
public interface AdvertCategService {
    /**
     * 通过主键查找
     * @param pkId
     * @return
     */
    AdvertCateg findByKey(Integer pkId);

    /**
     * 查询所有
     * @param advertCateg
     * @return
     */
    List<AdvertCateg> listAll(AdvertCateg advertCateg);
}
