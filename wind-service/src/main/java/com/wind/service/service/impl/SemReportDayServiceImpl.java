package com.wind.service.service.impl;

import com.google.common.collect.Lists;
import com.zmn.advert.model.entity.advertapi.SemReportDay;
import com.zmn.advert.persistence.advertapi.SemReportDayDao;
import com.zmn.advert.services.interfaces.advertapi.SemReportDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/26 14:12
 **/
@Slf4j
@Service
public class SemReportDayServiceImpl implements SemReportDayService {

    @Autowired(required = false)
    private SemReportDayDao semReportDayDao;

    @Override
    public void insertSemReportDay(SemReportDay semReportDay) {
        Integer num = semReportDayDao.insert(semReportDay);
        log.info("insertSemReportDay success num:{}", num);
    }

    @Override
    public void insertBatchSemReportDay(List<SemReportDay> semReportDayList) {
        if (semReportDayList == null || semReportDayList.isEmpty()) {
            log.info("insertBatchSemReportDay: null data need insert!");
            return;
        }
        List<List<SemReportDay>> dataList = Lists.partition(semReportDayList, 500);
        for (List<SemReportDay> list : dataList) {
            Integer num = semReportDayDao.insertBatch(list);
            log.info("insertBatchSemReportDay success num:{}", num);
        }
    }


}