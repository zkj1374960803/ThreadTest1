package com.ccbuluo.business.platform.carmanage.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carconfiguration.utils.RegularCodeProductor;
import com.ccbuluo.business.platform.carmanage.dao.BasicCarcoreInfoDao;
import com.ccbuluo.business.platform.carmanage.dto.SearchCarcoreInfoDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 车辆基本信息service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarcoreInfoServiceImpl  implements BasicCarcoreInfoService{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicCarcoreInfoDao basicCarcoreInfoDao;
    @Autowired
    private RegularCodeProductor regularCodeProductor;
    @Autowired
    private UserHolder userHolder;
    @Autowired

    /**
     * 存储redis时当前模块的名字
     */
    private static final String CAR_NUMBER = "car_number";
    /**
     * 该车架号(VIN)已经存在！
     */
    private static final String CAR_VIN_NUMBER_VERIFY = "该车架号(VIN)已经存在！";
    /**
     * 该发动机号已经存在！
     */
    private static final String CAR_ENGINE_NUMBER_VERIFY = "该发动机号已经存在！";
    /**
     * 该北斗设备编号已经存在！
     */
    private static final String CAR_BEIDOU_NUMBER_VERIFY = "该北斗设备编号已经存在！";


    /**
     * 身份证号码不正确！CODE值
     */
    private static final String IDENTITY_CARD_VERIFY_CODE = "50002";

    /**
     * 身份证号码不正确！
     */
    private static final String IDENTITY_CARD_VERIFY = "身份证号码不正确！";

    /**
     *
     * 车牌号不存在
     */
    private static final String PLAT_NUMBER_VERIFY_CODE = "50003";

    /**
     * 车牌号不存在！
     */
    private static final String PLAT_NUMBER_VERIFY = "车牌号不存在！";

    /**
     * 车辆注册新增
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto saveCarcoreInfo(CarcoreInfo carcoreInfo) {
        // 1.1 车辆基本信息验证唯一性
        StatusDto statusDto = findCarcoreInfoVerification(carcoreInfo);
        if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
            return statusDto;
        }

        try {

            // 1.保存车辆基本信息
            buildCarcoreInfo(carcoreInfo);
            long carcoreInfoId = basicCarcoreInfoDao.saveCarcoreInfo(carcoreInfo);

            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车辆新增失败！", e);
            throw e;
        }
    }
    /**
     * 编辑车辆
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto editCarcoreInfo(CarcoreInfo carcoreInfo){
        // 1.1 车辆基本信息验证唯一性
        StatusDto statusDto = findCarcoreInfoVerification(carcoreInfo);
        if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
            return statusDto;
        }

        try {

            // 1.保存车辆基本信息
            buildCarcoreInfo(carcoreInfo);
            long carcoreInfoId = basicCarcoreInfoDao.updateCarcoreInfo(carcoreInfo);

            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车辆更新失败！", e);
            throw e;
        }
    }

    /**
     * 构建车辆信息实体
     * @param carcoreInfo
     * @return void
     * @exception
     * @author wuyibo
     * @date 2018-05-10 18:38:06
     */
    private void buildCarcoreInfo(CarcoreInfo carcoreInfo) {

        // 1.车辆编码(新增）
        if (null == carcoreInfo.getId()) {
            carcoreInfo.setCarNumber(findCarNumber());
        }
        carcoreInfo.setCarStatus(Constants.DELETE_FLAG_NORMAL);
        // 3.通用字段
        carcoreInfo.preInsert(userHolder.getLoggedUserId());
    }

    /**
     * 获取车辆编码
     * @param
     * @return java.lang.String
     * @exception
     * @author wuyibo
     * @date 2018-05-16 11:29:21
     */
    private String findCarNumber() {
        // 获取认证中心的appId 构建redis存储的key
        String appId = SystemPropertyHolder.getBaseAppid();
        StringBuilder key = new StringBuilder();
        key.append(appId).append(Constants.CAR_COLON).append(Constants.CAR_MANAGE).append(Constants.CAR_COLON).append(CAR_NUMBER);
        return regularCodeProductor.getCarNumberNextCode(key.toString(), Constants.CAR_CODING, Constants.CAR_CODING_LENGTH);
    }

    /**
     * 车辆基本信息验证唯一性
     * @param carcoreInfo 车辆基本信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto findCarcoreInfoVerification(CarcoreInfo carcoreInfo) {
        carcoreInfo.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        int vinNumberCount = basicCarcoreInfoDao.countVinNumber(carcoreInfo);
        int engineNumberCount = basicCarcoreInfoDao.countEngineNumber(carcoreInfo);
        int beidouNumberCount = basicCarcoreInfoDao.countBeidouNumber(carcoreInfo);
        StringBuilder result = new StringBuilder();
        if (vinNumberCount > 0) {
            result.append(CAR_VIN_NUMBER_VERIFY);
        }
        if (engineNumberCount > 0) {
            result.append(CAR_ENGINE_NUMBER_VERIFY);
        }
        if (beidouNumberCount > 0) {
            result.append(CAR_BEIDOU_NUMBER_VERIFY);
        }
        if (StringUtils.isNotBlank(result.toString())) {
            return StatusDto.buildFailureStatusDto(result.toString());
        }
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     * 根据车辆id查询车辆详情
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @Override
    public CarcoreInfo queryCarDetailByCarId(Long carId){
        return basicCarcoreInfoDao.queryCarDetailByCarId(carId);
    }

    /**
     * 根据车辆id删除车辆
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @Override
    public int deleteCarcoreInfoByCarId(Long carId){
        return basicCarcoreInfoDao.deleteCarcoreInfoByCarId(carId);
    }
    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param Keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    @Override
    public Page<SearchCarcoreInfoDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, Integer carStatus, String Keyword, Integer offset, Integer pageSize){
        return basicCarcoreInfoDao.queryCarcoreInfoList(carbrandId, carseriesId, carStatus, Keyword, offset, pageSize);
    }


}
