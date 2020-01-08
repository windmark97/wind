package com.wind.service.business.impl.advertapi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.SemReportDay;
import com.wind.service.business.interfaces.advertapi.AdvertDataBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索推广返回数据保存service
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/29 11:19
 **/
@Slf4j
@Service
public class AdvertDataBServiceImpl implements AdvertDataBService {

    @Reference(version = com.zmn.plat.dubbo.utils.DubboConsts.INTERFACE_VERSION, check = false)
    private DatasetModifyRemoteService datasetModifyRemoteService;

    private static final String INSERT_SQL = "insert into ods.ods_publicize_cost partition(day=#day,account_id=#userId) values";
    private static final String PARTITION_FIELD_DAY = "#day";
    private static final String PARTITION_FIELD_USERID = "#userId";
    private static final String SEGMENTATION_SYMBOL = ", ";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String CURRENT_TIME = "now()";
    private static final String CLEAR_SQL = "alter table ods.ods_publicize_cost drop partition(day=#day,account_id=#userId) ";


    @Override
    public void modifyAdvertDataByquery(List<SemReportDay> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            log.info("insertBatchSemReportDay: null data need insert!");
            return;
        }
        try {
            List<List<SemReportDay>> semReportDayList = Lists.partition(dataList, 1000);

            for (List<SemReportDay> rowList : semReportDayList) {
                String sql = buildSql(rowList, rowList.get(0).getReportDate(), rowList.get(0).getUserId());
                ResponseDTO<Integer> responseDTO = datasetModifyRemoteService.insert(sql);
                log.info("modifyAdvertDataByquery result:{} ", JSON.toJSONString(responseDTO));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 清理要同步的时间段内的数据
     *
     * @param userId
     * @param day
     */
    private void clearData(Integer userId, Integer day) {
        String sql = CLEAR_SQL.replace(PARTITION_FIELD_DAY, day.toString()).replace(PARTITION_FIELD_USERID, userId.toString());
        ResponseDTO<Integer> responseDTO = datasetModifyRemoteService.insert(sql);
        log.info("clearData result:{},sql:{} ", JSON.toJSONString(responseDTO), sql);
    }

    @Override
    public void clearAdvertData(AdvertBaseInfo info) {
        String startTimeStr = info.getStartDate().replaceAll("-", "");
        String endTimeStr = info.getEndDate().replaceAll("-", "");
        Integer startDate = Integer.valueOf(startTimeStr);
        Integer endDate = Integer.valueOf(endTimeStr);
        for (int i = startDate; i <= endDate; i++) {
            clearData(info.getUserId(), i);
        }

    }

    public String buildSql(List<SemReportDay> dataList, Integer day, Integer userId) {

        String sql = INSERT_SQL.replace(PARTITION_FIELD_DAY, day.toString()).replace(PARTITION_FIELD_USERID, userId.toString());
        StringBuffer buffer = new StringBuffer(sql);
        SemReportDay semReportDay;
        for (int i = 0; i < dataList.size(); i++) {
            semReportDay = dataList.get(i);
            buffer.append(LEFT_PARENTHESIS);
            buffer.append("'").append(semReportDay.getPublicizeDate() + " 00:00:00'");
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getChannelId());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getServCategId());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getCategOneId());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getCityId());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getImpression());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getClick());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append(semReportDay.getCost());
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append("'");
            buffer.append(semReportDay.getGroupName());
            buffer.append("'");
            buffer.append(SEGMENTATION_SYMBOL);
            buffer.append("'");
            buffer.append(semReportDay.getCreativeName());
            buffer.append("'");
            buffer.append(SEGMENTATION_SYMBOL);
            //创建时间
            buffer.append(CURRENT_TIME);
            buffer.append(RIGHT_PARENTHESIS);
            if (i != dataList.size() - 1) {
                buffer.append(SEGMENTATION_SYMBOL);
            }
        }
        return buffer.toString();
    }


}
