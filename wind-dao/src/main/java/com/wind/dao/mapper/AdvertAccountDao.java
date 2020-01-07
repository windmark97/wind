package com.wind.dao.mapper;

import com.wind.dao.model.AdvertAccount;
import com.wind.dao.model.AdvertAccountQuery; 

import java.util.List;

/**
 * 搜索推广账号dao
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/20 18:42
 **/
public interface AdvertAccountDao {

    /**
     * 根据主键查询
     * @param userId
     * @return
     */
    AdvertAccount findByKey(Integer userId);

    /**
     * 分页查询
     * @param query
     * @return
     */
    List<AdvertAccount> listPage(AdvertAccountQuery query);

    /**
     * 查询总数
     * @param query
     * @return
     */
    Integer countByQuery(AdvertAccountQuery query);


}
