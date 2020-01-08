package com.wind.service.business.impl.advertapi;

import com.wind.dao.model.AdvertAccount;
import com.wind.dao.model.AdvertAccountQuery;
import com.wind.dao.model.AdvertBaseInfo;
import com.wind.dao.model.AdvertJobParam;
import com.wind.manager.constant.AdvertApiConsts;
import com.wind.service.business.interfaces.advertapi.AdvertApiBService;
import com.wind.service.handler.impl.AdvertApiHandlerImpl;
import com.wind.service.service.interfaces.AdvertAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索推广数据同步服务
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 16:25
 **/
@Slf4j
@Service
public class AdvertApiBServiceImpl implements AdvertApiBService {

    @Autowired
    private AdvertAccountService advertAccountService;

    @Autowired
    private AdvertApiHandlerImpl advertApiHandler;

    /**
     * 同步搜索平台数据
     *
     * @return
     */
    @Override
    public String synAdvertApiData(final AdvertJobParam param) {
        AdvertAccountQuery query = new AdvertAccountQuery();
        query.setAdvertType(param.getApiType());
        query.setUserId(param.getUserId());
        query.setUserName(param.getUserName());
        //有效账号
        query.setActiveMark(AdvertApiConsts.ACCOUNT_ACTIVE_YES);
        List<AdvertAccount> advertAccountList = advertAccountService.queryAll(query);
        List<AdvertBaseInfo> advertBaseInfoList = advertAccountList.stream()
                .map(obj -> buildAdvertBaseInfo(obj, param))
                .collect(Collectors.toList());

        if (advertBaseInfoList == null || advertBaseInfoList.isEmpty()) {
            return "没有需要同步的数据！";
        }
        for (AdvertBaseInfo advertBaseInfo : advertBaseInfoList) {
            advertApiHandler.execute(advertBaseInfo);
        }
        return "同步数据成功！";
    }

    /**
     * 获取AdvertBaseInfo
     *
     * @param advertAccount
     * @param param
     * @return
     */
    private AdvertBaseInfo buildAdvertBaseInfo(AdvertAccount advertAccount, AdvertJobParam param) {
        AdvertBaseInfo baseInfo = new AdvertBaseInfo();
        BeanUtils.copyProperties(advertAccount, baseInfo);
        baseInfo.setStartDate(param.getStartDate());
        baseInfo.setEndDate(param.getEndDate());
        return baseInfo;
    }
}
