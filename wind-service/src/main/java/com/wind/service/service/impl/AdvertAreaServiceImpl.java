package com.wind.service.service.impl;

import com.zmn.advert.model.entity.advertapi.AdvertArea;
import com.zmn.advert.model.entity.advertapi.query.AdvertAreaQuery;
import com.zmn.advert.persistence.advertapi.AdvertAreaDao;
import com.zmn.advert.services.interfaces.advertapi.AdvertAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索推广区域数据service
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 14:12
 **/
@Slf4j
@Service
public class AdvertAreaServiceImpl implements AdvertAreaService {

    @Autowired(required = false)
    private AdvertAreaDao advertAreaDao;

    @Override
    public AdvertArea findAdvertAreaByKey(Integer areaId) {
        return advertAreaDao.findByKey(areaId);
    }

    @Override
    public List<AdvertArea> listPage(AdvertAreaQuery query) {
        return advertAreaDao.listPage(query);
    }

    @Override
    public List<AdvertArea> listAll(AdvertAreaQuery query) {
        Integer count = advertAreaDao.countByQuery(query);
        query.setPageSize(count);
        return advertAreaDao.listPage(query);
    }
}
