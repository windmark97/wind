package com.wind.service.service.buildsql;

import com.google.common.collect.Lists;
import com.wind.common.dto.ResponseDTO;
import com.wind.dao.buildsql.handler.BuildSqlExecuteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * buildsql调用dubbo接口执行sql语句
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/19 14:01
 **/
@Slf4j
@Service
public class BuildSqlExecuteHandlerImpl implements BuildSqlExecuteHandler {
    //@Reference( check = false)
    //private DatasetRemoteService datasetRemoteService;

    @Override
    public Object selectList(String sql) {
        ResponseDTO<List> responseDTO = null;//datasetRemoteService.list(sql);
        log.debug("datasetRemoteService list，status:{}", responseDTO.getStatus());
        Object dataList = responseDTO.getData();
        if (dataList == null) {
            dataList = Lists.newArrayList();
        }
        return dataList;
    }

    @Override
    public Integer count(String sql) {
        ResponseDTO<Integer> responseDTO = null;//datasetRemoteService.count(sql);
        log.debug("datasetRemoteService list，status:{}", responseDTO.getStatus());
        Integer data = responseDTO.getData();
        return data;
    }
}
