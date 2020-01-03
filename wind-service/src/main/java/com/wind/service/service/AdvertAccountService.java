package com.wind.service.service;

import com.zmn.advert.model.entity.advertapi.AdvertAccount;
import com.zmn.advert.model.entity.advertapi.query.AdvertAccountQuery;

import java.util.List;

/**
 * 搜索推广账号service
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 17:54
 **/
public interface AdvertAccountService {
    /**
     * 查询所有满足条件的数据
     * @param query
     * @return
     */
    List<AdvertAccount> queryAll(AdvertAccountQuery query);
}
