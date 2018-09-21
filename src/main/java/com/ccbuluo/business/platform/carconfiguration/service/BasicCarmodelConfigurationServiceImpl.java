package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelConfigurationDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.carconfiguration.dao.CarmodelManageDTO;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 车型具体配置service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarmodelConfigurationServiceImpl implements BasicCarmodelConfigurationService{
    @Autowired
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @Autowired
    private BasicCarmodelConfigurationDao basicCarmodelConfigurationDao;

    /**
     * 根据车型id查询车型配置详情
     * @param id 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 18:21:10
     */
    @Override
    public CarmodelManageDTO queryCarModelConfigurationByCarModelId(Long id) {
        CarmodelManageDTO carmodelManageDTO = this.basicCarmodelManageDao.getCarmodelManageDtoById(id);
        List<CarmodelConfiguration> carmodelConfigurations = this.basicCarmodelConfigurationDao.getByCarModelId(id);
        carmodelManageDTO.setConfigurations(carmodelConfigurations);
        return carmodelManageDTO;
    }

    /**
     * 根据车型id和参数名称查询车型参数
     * @param carmodelId 车型id
     * @param parameterName 参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 15:50:15
     */
    @Override
    public CarmodelConfiguration getByCarModelIdAndName(Long carmodelId, String parameterName) {
        CarmodelConfiguration carmodelConfiguration = this.basicCarmodelConfigurationDao.getByCarModelIdAndName(carmodelId, parameterName);
        return carmodelConfiguration;
    }
}
