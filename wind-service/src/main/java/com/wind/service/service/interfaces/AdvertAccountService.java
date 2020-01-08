package com.wind.service.service.interfaces;


import com.wind.dao.model.AdvertAccount;
import com.wind.dao.model.AdvertAccountQuery;

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
