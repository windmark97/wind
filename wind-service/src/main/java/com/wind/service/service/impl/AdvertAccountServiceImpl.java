package com.wind.service.service.impl;

import com.google.common.collect.Lists;
import com.wind.dao.mapper.AdvertAccountDao;
import com.wind.dao.model.AdvertAccount;
import com.wind.dao.model.AdvertAccountQuery;
import com.wind.service.service.interfaces.AdvertAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 17:56
 **/
@Slf4j
@Service
public class AdvertAccountServiceImpl implements AdvertAccountService {
    private static final int DEFAULT_PAGE_SIZE = 200;

    @Autowired(required = false)
    private AdvertAccountDao advertAccountDao;

    /**
     * 根据查询条件查询所有数据
     * @param query
     * @return
     */
    @Override
    public List<AdvertAccount> queryAll(AdvertAccountQuery query) {
        List<AdvertAccount> dataList = Lists.newArrayList();
        Integer count = advertAccountDao.countByQuery(query);
        if(count==null||count==0){
            return dataList;
        }
        int pageSize = DEFAULT_PAGE_SIZE;
        if (count <= pageSize) {
            pageSize = count;
        }
        int totalPage = count / pageSize;
        if (count % pageSize != 0) {
            totalPage++;
        }
        query.setPageSize(pageSize);
        for (int i = 1; i <= totalPage; i++) {
            query.setPagesNumber(i);
            List<AdvertAccount> pageList = advertAccountDao.listPage(query);
            dataList.addAll(pageList);
        }
        return dataList;
    }
}
