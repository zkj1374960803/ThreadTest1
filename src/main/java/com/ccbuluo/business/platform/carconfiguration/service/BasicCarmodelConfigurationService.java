package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.dao.CarmodelManageDTO;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;

/**
 * 车型具体配置service
 * @author chaoshuai
 * @date 2018-05-08 10:52:22
 */
public interface BasicCarmodelConfigurationService {
    /**
     * 查询车型配置详情
     * @param id 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 18:21:10
     */
    CarmodelManageDTO queryCarModelConfigurationByCarModelId(Long id);

    /**
     * 根据车型id和参数名称查询车型参数
     * @param carmodelId 车型id
     * @param parameterName 参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 15:50:15
     */
    CarmodelConfiguration getByCarModelIdAndName(Long carmodelId, String parameterName);
}
