package com.ccbuluo.business.platform.maintaincar.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintaincar;
import com.ccbuluo.business.platform.maintaincar.dao.BizServiceMaintaincarDao;
import com.ccbuluo.business.platform.maintaincar.dto.SearchBizServiceMaintaincarDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 车辆基本信息service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BizServiceMaintaincarServiceImpl implements BizServiceMaintaincarService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BizServiceMaintaincarDao bizServiceMaintaincarDao;
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
     * 该北斗设备编号已经存在！
     */
    private static final String CAR_BEIDOU_NUMBER_VERIFY = "该北斗设备编号已经存在！";


    /**
     * 车辆注册新增
     * @param bizServiceMaintaincar 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto saveServiceMaintaincar(BizServiceMaintaincar bizServiceMaintaincar) {
        try {
            // 1.1 车辆基本信息验证唯一性
            StatusDto statusDto = findServiceMaintaincarVerification(bizServiceMaintaincar);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
            // 1.保存车辆基本信息
            buildServiceMaintaincar(bizServiceMaintaincar);
            long carcoreInfoId = bizServiceMaintaincarDao.saveServiceMaintaincar(bizServiceMaintaincar);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("维修车辆新增失败！", e);
            throw e;
        }
    }
    /**
     * 编辑车辆
     * @param bizServiceMaintaincar 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto editServiceMaintaincar(BizServiceMaintaincar bizServiceMaintaincar){
        try {
            // 1.1 车辆基本信息验证唯一性
            StatusDto statusDto = findServiceMaintaincarVerification(bizServiceMaintaincar);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
            // 1.保存车辆基本信息
            buildServiceMaintaincar(bizServiceMaintaincar);
            long carcoreInfoId = bizServiceMaintaincarDao.updateServiceMaintaincar(bizServiceMaintaincar);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("车辆更新失败！", e);
            throw e;
        }
    }

    /**
     * 构建维修车辆信息实体
     * @param bizServiceMaintaincar
     * @return void
     * @exception
     * @author wuyibo
     * @date 2018-05-10 18:38:06
     */
    private void buildServiceMaintaincar(BizServiceMaintaincar bizServiceMaintaincar) {
        bizServiceMaintaincar.setCarStatus((long)Constants.STATUS_FLAG_ZERO);
        bizServiceMaintaincar.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        // 3.通用字段
        bizServiceMaintaincar.preInsert(userHolder.getLoggedUserId());
    }

    /**
     * 车辆基本信息验证唯一性
     * @param bizServiceMaintaincar 车辆基本信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto findServiceMaintaincarVerification(BizServiceMaintaincar bizServiceMaintaincar) {
        int vinNumberCount = bizServiceMaintaincarDao.countVinNumber(bizServiceMaintaincar);
        int beidouNumberCount = bizServiceMaintaincarDao.countBeidouNumber(bizServiceMaintaincar);
        StringBuilder result = new StringBuilder();
        if (vinNumberCount > 0) {
            result.append(CAR_VIN_NUMBER_VERIFY);
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
    public BizServiceMaintaincar queryServiceMaintaincarByCarId(Long carId){
        return bizServiceMaintaincarDao.queryServiceMaintaincarByCarId(carId);
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
    public int deleteServiceMaintaincarByCarId(Long carId){
        return bizServiceMaintaincarDao.deleteCarcoreInfoByCarId(carId);
    }
    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    @Override
    public Page<SearchBizServiceMaintaincarDTO> queryServiceMaintaincarList(Long carbrandId, Long carseriesId, Integer carStatus, String keyword, Integer offset, Integer pageSize){
        return bizServiceMaintaincarDao.queryCarcoreInfoList(carbrandId, carseriesId, carStatus, keyword, offset, pageSize);
    }


}
