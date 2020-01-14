package com.wind.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wind.common.dto.ResponseDTO;
import com.wind.dao.model.AdvertAccount;
import com.wind.dao.model.AdvertAccountQuery;
import com.wind.dubbo.interfaces.AdvertDataListService;
import com.wind.dubbo.model.AdvertAccountDIO;
import com.wind.dubbo.model.AdvertAccountDRO;
import com.wind.service.service.interfaces.AdvertAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 16:21
 **/
@Service
public class AdvertDataListServiceImpl implements AdvertDataListService {
    @Autowired
    private AdvertAccountService advertAccountService;

    @Override
    public ResponseDTO<List<AdvertAccountDRO>> listAdvertAccount(AdvertAccountDIO dio) {

        AdvertAccountQuery advertAccountQuery = new AdvertAccountQuery();
        BeanUtils.copyProperties(dio,advertAccountQuery);

        List<AdvertAccount> advertAccountList =  advertAccountService.queryAll(advertAccountQuery);
        List<AdvertAccountDRO> advertAccountDROList= advertAccountList.stream().map(obj->{
            AdvertAccountDRO dro = new AdvertAccountDRO();
            BeanUtils.copyProperties(obj,dro);
            return dro;
        }).collect(Collectors.toList());
        return ResponseDTO.success(advertAccountDROList);
    }
}
