package com.wind.service.service.impl;

import com.wind.dao.mapper.AdvertCategDao;
import com.wind.dao.model.AdvertCateg;
import com.wind.service.service.interfaces.AdvertCategService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索推广服务产品分类数据
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/27 14:22
 **/
@Service
public class AdvertCategServiceImpl implements AdvertCategService {
    @Autowired(required = false)
    private AdvertCategDao advertCategDao;

    @Override
    public AdvertCateg findByKey(Integer pkId) {
        return advertCategDao.findByKey(pkId);
    }

    @Override
    public List<AdvertCateg> listAll(AdvertCateg advertCateg) {
        return advertCategDao.listAll(advertCateg);
    }
}
