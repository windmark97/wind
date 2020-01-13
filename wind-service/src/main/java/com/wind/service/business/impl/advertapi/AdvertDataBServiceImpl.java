package com.wind.service.business.impl.advertapi;

import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.SemReportDay;
import com.wind.dao.model.SemReportDayQuery;
import com.wind.service.business.interfaces.advertapi.AdvertDataBService;
import com.wind.service.service.interfaces.SemReportDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/13 10:03
 **/
@Slf4j
@Service
public class AdvertDataBServiceImpl implements AdvertDataBService {

    @Autowired
    private SemReportDayService semReportDayService;

    @Override
    public void modifyAdvertDataByquery(List<SemReportDay> dataList) {
        semReportDayService.insertBatchSemReportDay(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clearAdvertData(AdvertBaseInfo info) {
        String startTimeStr = info.getStartDate().replaceAll("-", "");
        String endTimeStr = info.getEndDate().replaceAll("-", "");
        Integer startDate = Integer.valueOf(startTimeStr);
        Integer endDate = Integer.valueOf(endTimeStr);
        for (int i = startDate; i <= endDate; i++) {
            SemReportDayQuery semReportDay = new SemReportDayQuery();
            semReportDay.setUserId(info.getUserId());
            semReportDay.setReportDate(i);
            semReportDayService.clearAdvertData(semReportDay);
        }
    }
}
