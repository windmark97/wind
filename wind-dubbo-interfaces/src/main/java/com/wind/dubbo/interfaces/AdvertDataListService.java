package com.wind.dubbo.interfaces;

import com.wind.common.dto.ResponseDTO;
import com.wind.dubbo.model.AdvertAccountDIO;
import com.wind.dubbo.model.AdvertAccountDRO;

import java.util.List;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/14 16:07
 **/
public interface AdvertDataListService {

    ResponseDTO<List<AdvertAccountDRO>> listAdvertAccount(AdvertAccountDIO dio);

}
