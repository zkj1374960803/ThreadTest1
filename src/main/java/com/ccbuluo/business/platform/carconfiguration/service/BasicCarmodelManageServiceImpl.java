package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.dao.*;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelManage;
import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.business.platform.carconfiguration.utils.RegularCodeProductor;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 车型管理service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarmodelManageServiceImpl implements BasicCarmodelManageService{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @Autowired
    private BasicCarmodelConfigurationDao basicCarmodelConfigurationDao;
    @Autowired
    private BasicCarseriesManageDao basicCarseriesManageDao;
    @Autowired
    private RegularCodeProductor product;
    @Autowired
    private UserHolder userHolder;

    /**
     * 存储redis时当前模块的名字
     */
    private static final String CAR_MODEL_NUMBER = "car_model_number";

    /**
     * 分页查询车型列表
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param status 状态
     * @param offset
     * @param limit
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 19:37:29
     */
    @Override
    public Page<CarmodelManageDTO> queryPageForCarModelManage(Long carbrandId, Long carseriesId, Integer status, String carmodelName, int offset, int limit) {
        Page<CarmodelManageDTO> carmodelManageDTOPage = this.basicCarmodelManageDao.queryPageForCarModelManage(carbrandId, carseriesId, status,carmodelName, offset, limit);
        return carmodelManageDTOPage;
    }

    /**
     * 查询所有的车型
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 11:20:13
     */
    @Override
    public List<CarmodelManage> queryAllModel() {
        List<CarmodelManage> carmodelManages = this.basicCarmodelManageDao.queryAllModel();
        return carmodelManages;
    }

    /**
     * 根据车系id查询车型
     * @param id 车系id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-10 09:37:13
     */
    @Override
    public List<CarmodelManage> getByCarSeriesId(Long id) {
        List<CarmodelManage> carmodelManages = this.basicCarmodelManageDao.getByCarSeriesId(id);
        return carmodelManages;
    }

    /**
     * 新增车型
     * @param carmodelManageDTO 车型扩展实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto createCarModel(CarmodelManageDTO carmodelManageDTO) {
        try {
            CarmodelManage carmodelManage = buildCarModelManage(carmodelManageDTO);
            //保存车型主体信息
            long carmodelId = this.basicCarmodelManageDao.create(carmodelManage);
            List<CarmodelConfiguration> configurations = carmodelManageDTO.getConfigurations();
            for (CarmodelConfiguration configuration : configurations){
                configuration.setCarmodelId(carmodelId);
            }
            //车型具体配置表（车身颜色变速箱、排量等）包括车型配置id，以及选择的某些部分配置参数parameter_value
            this.basicCarmodelConfigurationDao.batchCreate(carmodelManageDTO.getConfigurations());

            return StatusDto.buildSuccessStatusDto("车型新增成功！！！");
        }catch (Exception e){
            logger.error("车型新增失败！！！",e);
            throw e;
        }

    }

    /**
     * 车型编辑
     * @param carmodelManageDTO 车型扩展实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 11:22:56
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editCarModel(CarmodelManageDTO carmodelManageDTO) {
        try {
            CarmodelManage carmodelManage = buildCarModelManage(carmodelManageDTO);
            if(null != carmodelManageDTO.getId()){
                this.basicCarmodelConfigurationDao.deleteByModelId(carmodelManageDTO.getId());
                this.basicCarmodelConfigurationDao.batchCreate(carmodelManageDTO.getConfigurations());
            }
            this.basicCarmodelManageDao.edit(carmodelManage);

            return StatusDto.buildSuccessStatusDto("车型修改成功！！！");
        }catch (Exception e){
            logger.error("车型修改失败！！！",e);
            throw e;
        }
    }

    /**
     * 车型停用启用
     * @param id 车型id
     * @param status 车型状态
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:19:31
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto stopOperationCarModel(Long id, int status) {
        CarmodelManage carmodelManage = this.basicCarmodelManageDao.getById(id);
        try {
            carmodelManage.setCarmodelStatus(status);
            carmodelManage.preUpdate(userHolder.getLoggedUserId());
            this.basicCarmodelManageDao.stopOperationCarModel(carmodelManage);
            return StatusDto.buildSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            logger.error("操作失败！！！",e);
            throw e;
        }
    }

    /**
     * 根据车型id 获取车型名称 和 logo
     * @param id 车型id
     * @return Map<String,Object> eg：{"carmodelName":"","carbrandLogo":""}
     * @exception
     * @author lizhao
     * @date 2018-05-29 17:36:27
     */
    @Override
    public Map<String, Object> getCarmodelAndLogo(Long id) {
        return basicCarmodelManageDao.getCarmodelAndLogo(id);
    }
    /**
     * 获取全部车型 下拉用
     * @return 结果集
     * @author Ryze
     * @date 2018-06-12 15:31:17
     */
    @Override
    public List<Map<String, Object>> queryAll() {
        return basicCarmodelManageDao.queryAll();
    }

    /**
     * 构建车型实体参数
     * @param carmodelManageDTO 车型扩展
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 11:19:07
     */
    private CarmodelManage buildCarModelManage(CarmodelManageDTO carmodelManageDTO){
        CarmodelManage carmodelManage = new CarmodelManage();
        CarseriesManage carseriesManageDetail = this.basicCarseriesManageDao.findCarseriesManageDetail(carmodelManageDTO.getCarseriesId());

        // 新增or编辑
        if(null != carseriesManageDetail){
            StringBuilder carModelName = new StringBuilder();
            carModelName.append(carseriesManageDetail.getCarseriesName())
                        .append(" ")
                        .append(carmodelManageDTO.getDisplacementGearbox());
            carmodelManage.setCarmodelName(carModelName.toString());
            // 基础字段的维护
            if(null != carmodelManageDTO.getId()){
                carmodelManage.preUpdate(userHolder.getLoggedUserId());
            }else {
                carmodelManage.preInsert(userHolder.getLoggedUserId());
            }
        }

        // 获取认证中心的appId
        String appId = SystemPropertyHolder.getBaseAppid();
        StringBuilder key = new StringBuilder();
        // 构建redis存储的key
        key.append(appId)
           .append(Constants.CAR_COLON)
           .append(Constants.CAR_CONFIGURATION)
           .append(Constants.CAR_COLON)
           .append(CAR_MODEL_NUMBER);

        if(null == carmodelManageDTO.getId()){
            String carModelNumber = product.getNextCode(key.toString(), Constants.CAR_CONFIGURATION_CODING, Constants.CAR_CONFIGURATION_CODING_LENGTH);
            carmodelManage.setCarmodelNumber(carModelNumber);
        }

        if( null != carmodelManageDTO.getId()){
            carmodelManage.setId(carmodelManageDTO.getId());
        }
        carmodelManage.setCarbrandId(carmodelManageDTO.getCarbrandId());
        carmodelManage.setCarseriesId(carmodelManageDTO.getCarseriesId());
        carmodelManage.setModelTitle(carmodelManageDTO.getModelTitle());
        carmodelManage.setModelImage(carmodelManageDTO.getModelImage());
        carmodelManage.setModelMasterImage(carmodelManageDTO.getModelMasterImage());
        carmodelManage.preUpdate(userHolder.getLoggedUserId());
        return carmodelManage;
    }
}
