package com.wind.service.service.buildsql;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.zmn.common.dto2.ResponseDTO;
import com.zmn.data.dubbo.interfaces.dataset.DatasetRemoteService;
import com.zmn.performance.persistence.buildsql.handler.BuildSqlExecuteHandler;
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
    @Reference(version = com.zmn.plat.dubbo.utils.DubboConsts.INTERFACE_VERSION, check = false)
    private DatasetRemoteService datasetRemoteService;

    @Override
    public Object selectList(String sql) {
        ResponseDTO<List> responseDTO = datasetRemoteService.list(sql);
        log.debug("datasetRemoteService list，status:{}", responseDTO.getStatus());
        Object dataList = responseDTO.getData();
        if (dataList == null) {
            dataList = Lists.newArrayList();
        }
        return dataList;
    }

    @Override
    public Integer count(String sql) {
        ResponseDTO<Integer> responseDTO = datasetRemoteService.count(sql);
        log.debug("datasetRemoteService list，status:{}", responseDTO.getStatus());
        Integer data = responseDTO.getData();
        return data;
    }
}
