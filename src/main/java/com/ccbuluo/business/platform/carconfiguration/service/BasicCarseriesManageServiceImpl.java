package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarbrandManageDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarseriesManageDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarbrandManage;
import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.business.platform.carconfiguration.utils.RegularCodeProductor;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 车系管理service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarseriesManageServiceImpl implements BasicCarseriesManageService{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicCarseriesManageDao basicCarseriesManageDao;
    @Autowired
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @Autowired
    private BasicCarbrandManageDao basicCarbrandManageDao;
    @Autowired
    private RegularCodeProductor regularCodeProductor;
    @Autowired
    private UserHolder userHolder;

    /**
     * 存储redis时当前模块的名字
     */
    private static final String CAR_SERIES_NUMBER = "car_series_number";

    /**
     * 该车系已经存在！
     */
    private static final String CARSERIES_NUME_VERIFY = "该车系已经存在！";
    /**
     * 该车系下已有车型数据，无法进行删除！
     */
    private static final String CARMODEL_MANAGE_VERIFY = "该车系下已有车型数据，无法进行删除！";

    /**
     * 车系新增
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveCarseriesManage(CarseriesManage carseriesManage) {

        // 1.判断车系是否存在
        if (findCarseriesRenameExist(carseriesManage)) {
            return StatusDto.buildFailureStatusDto(CARSERIES_NUME_VERIFY);
        }
        // 2.车系编码
        carseriesManage.setCarseriesNumber(findCarseriesNumber());
        // 3.公共字段
        carseriesManage.preInsert(userHolder.getLoggedUserId());

        try {
            // 4.保存车系
            basicCarseriesManageDao.saveCarseriesManage(carseriesManage);
            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车系新增失败！", e);
            throw e;
        }
    }

    /**
     * 获取车系编码
     * @param
     * @return java.lang.String
     * @exception
     * @author wuyibo
     * @date 2018-05-16 10:56:20
     */
    private String findCarseriesNumber() {
        // 获取认证中心的appId 构建redis存储的key
        String appId = SystemPropertyHolder.getBaseAppid();
        StringBuilder key = new StringBuilder();
        key.append(appId).append(Constants.CAR_COLON).append(Constants.CAR_CONFIGURATION).append(Constants.CAR_COLON).append(CAR_SERIES_NUMBER);
        return regularCodeProductor.getNextCode(key.toString(), Constants.CAR_SERIES_CODING, Constants.CAR_SERIES_LENGTH);
    }

    /**
     * 判断车系是否存在
     * @param carseriesManage 车系
     * @return boolean
     * @exception
     * @author wuyibo
     * @date 2018-05-16 10:51:36
     */
    private boolean findCarseriesRenameExist(CarseriesManage carseriesManage) {
        int count = basicCarseriesManageDao.countCarseriesRename(carseriesManage.getId(), carseriesManage.getCarseriesName(), Constants.DELETE_FLAG_NORMAL);
        return count > 0;
    }

    /**
     * 车系编辑
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto updateCarseriesManage(CarseriesManage carseriesManage) {

        // 1.判断车系是否存在
        if (findCarseriesRenameExist(carseriesManage)) {
            return StatusDto.buildFailureStatusDto(CARSERIES_NUME_VERIFY);
        }

        try {
            // 2.保存车系
            carseriesManage.preUpdate(userHolder.getLoggedUserId());
            basicCarseriesManageDao.updateCarseriesManage(carseriesManage);
            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车系编辑失败！", e);
            throw e;
        }
    }

    /**
     * 车系删除
     * @param id 车系id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto deleteCarseriesManage(Long id) {

        // 1.判断车系下是否有车型数据
        int count = basicCarmodelManageDao.countCarmodelManage(id);
        if (count > 0) {
            return StatusDto.buildFailureStatusDto(CARMODEL_MANAGE_VERIFY);
        }

        try {
            // 2.删除品牌
            basicCarseriesManageDao.deleteCarseriesManage(id);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("车系删除失败！", e);
            throw e;
        }
    }

    /**
     * 车系详情
     * @param id 车系id
     * @return com.ccbuluo.business.entity.CarseriesManage
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    @Override
    public CarseriesManage findCarseriesManageDetail(Long id) {
        return basicCarseriesManageDao.findCarseriesManageDetail(id);
    }


    /**
     * 分页查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.db.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @Override
    public Page<Map<String, Object>> queryCarseriesManagePage(Long carbrandId, String carseriesName, int offset, int limit) {
        CarbrandManage carbrandManage = basicCarbrandManageDao.findCarbrandManageDetail(carbrandId);
        return basicCarseriesManageDao.queryCarseriesManagePage(carbrandManage.getCarbrandName(), carbrandId, carseriesName, offset, limit);
    }

    /**
     * 查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @Override
    public List<CarseriesManage> queryCarseriesManageList(Long carbrandId, String carseriesName) {
        return basicCarseriesManageDao.queryCarseriesManageList(carbrandId, carseriesName);
    }

    /**
     * 查询所有车系 下拉框
     * @param
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @Override
    public List<CarseriesManage> queryAllCarseriesManageList() {
        return basicCarseriesManageDao.queryAllCarseriesManageList();
    }
}
