package com.ccbuluo.business.platform.carmanage.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarseriesManageDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.business.platform.carconfiguration.utils.RegularCodeProductor;
import com.ccbuluo.business.platform.carmanage.dao.BasicCarcoreInfoDao;
import com.ccbuluo.business.platform.carmanage.dto.*;
import com.ccbuluo.business.platform.maintaincar.dto.ListServiceMaintaincarDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    BasicCarseriesManageDao basicCarseriesManageDao;
    @Resource
    private InnerUserInfoService innerUserInfoService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;

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
     * 该车架号(VIN)已经存在！
     */
    private static final String CAR_STATUS_VERIFY = "此车辆已被分配无法删除！";


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
        try {
            // 1.1 车辆基本信息验证唯一性
            StatusDto statusDto = findCarcoreInfoVerification(carcoreInfo);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
            // 1.车辆编码(新增）
            StatusDto<String> codeDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FJ);
            // 获取code失败
            if(!codeDto.isSuccess()){
                return statusDto;
            }
            carcoreInfo.setCarNumber(codeDto.getData());
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
        try {
            // 1.1 车辆基本信息验证唯一性
            StatusDto statusDto = findCarcoreInfoVerification(carcoreInfo);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
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

        // 新增默认未分配：0
        carcoreInfo.setCarStatus(Constants.STATUS_FLAG_ZERO);
        carcoreInfo.setStoreAssigned(Constants.STATUS_FLAG_ZERO);
        // 3.通用字段
//        carcoreInfo.preInsert(userHolder.getLoggedUserId());
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
    public StatusDto deleteCarcoreInfoByCarId(Long carId){
        try {
            StatusDto statusDto = checkCarcoreInfoDelete(carId);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
            basicCarcoreInfoDao.deleteCarcoreInfoByCarId(carId);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("删除车辆失败！",e);
            throw e;
        }
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
    public Page<SearchCarcoreInfoDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, Integer storeAssigned, String custmanagerUuid, String Keyword, Integer offset, Integer pageSize){
        Page<SearchCarcoreInfoDTO> searchCarcoreInfoDTOPage =  basicCarcoreInfoDao.queryCarcoreInfoList(carbrandId, carseriesId, storeAssigned, custmanagerUuid, Keyword, offset, pageSize);
        // 拼装车系
        buildCarseriesManage(searchCarcoreInfoDTOPage);
        return searchCarcoreInfoDTOPage;
    }
    // 组装车系的名称
    private void buildCarseriesManage(Page<SearchCarcoreInfoDTO> searchCarcoreInfoDTOPage){
        List<CarseriesManage> list = basicCarseriesManageDao.queryAllCarseriesManageList();
        for(SearchCarcoreInfoDTO sd : searchCarcoreInfoDTOPage.getRows()){
            for(CarseriesManage cm : list){//获取车系的名称
                if(sd.getCarseriesId().intValue() == cm.getId().intValue()){
                    sd.setCarseriesName(cm.getCarseriesName());
                }
            }
        }
    }
    /**
     * 车辆删除验证
     * @param carId 车辆id
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    public StatusDto checkCarcoreInfoDelete(Long carId) {
        CarcoreInfo carcoreInfo = basicCarcoreInfoDao.queryCarDetailByCarId(carId);
        StringBuilder result = new StringBuilder();
        if (null != carcoreInfo && carcoreInfo.getCarStatus() != 0) {
            result.append(CAR_STATUS_VERIFY);
        }
        if (StringUtils.isNotBlank(result.toString())) {
            return StatusDto.buildFailureStatusDto(result.toString());
        }
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询未分配的车辆列表
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    @Override
    public List<ListCarcoreInfoDTO> queryuUndistributedList(String vinNumber){
        List<ListCarcoreInfoDTO> queryundistributedlist = basicCarcoreInfoDao.queryuUndistributedList(vinNumber);
        List<Long> carModelIds = queryundistributedlist.stream().map(a -> a.getCarmodelId()).distinct().collect(Collectors.toList());
        List<ListCarcoreInfoDTO> listCarcoreInfoDTOS = basicCarcoreInfoDao.queryCarMobelNameByIds(carModelIds);
        Map<Long, String> collect = listCarcoreInfoDTOS.stream().collect(Collectors.toMap(a -> a.getCarmodelId(), b -> b.getCarmodelName()));
        queryundistributedlist.forEach(item -> {
            item.setCarmodelName(collect.get(item.getCarmodelId()));
        });
        return queryundistributedlist;
    }

    /**
     * 根据车辆code更新车辆状态
     * @param list
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    @Override
    public int updateStatusByCode(List<UpdateCarcoreInfoDTO> list){
        return basicCarcoreInfoDao.updateStatusByCode(list);
    }
    /**
     * 根据车架号查询车辆信息
     * @param vinNumber 车辆vin
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @Override
    public StatusDto getCarInfoByVin(String vinNumber,String appId,String secretId){
        StatusDto<String> statusDto = innerUserInfoService.checkAppIdAndSecretId( appId, secretId);
        // 权限校验
        if(Constants.ERROR_CODE.equals(statusDto.getCode())){
            return statusDto;
        }
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoDao.getCarInfoByVin(vinNumber));
    }

    @Override
    public int release(String carNumber) {
        //  根据车辆编号更新客户经理名称和门店名称
        basicCarcoreInfoDao.updateCarcoreInfoManager(carNumber);
        return basicCarcoreInfoDao.release(carNumber);
    }

    /**
     * 根据车辆vin更新车辆的门店信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-08-01 15:55:14
     */
    @Override
    public StatusDto batchUpdateCarcoreInfoByVin(UpdateCarcoreInfoByVinDTO updateCarcoreInfoByVinDTO){
        StatusDto<String> statusDto = innerUserInfoService.checkAppIdAndSecretId(updateCarcoreInfoByVinDTO.getAppId(), updateCarcoreInfoByVinDTO.getSecretId());
        // 权限校验
        if(Constants.ERROR_CODE.equals(statusDto.getCode())){
            return statusDto;
        }
        int flag =  basicCarcoreInfoDao.batchUpdateCarcoreInfoByVin(updateCarcoreInfoByVinDTO.getCarcoreInfoList());
        if (flag != Constants.STATUS_FLAG_ZERO) {
            return StatusDto.buildSuccessStatusDto("操作成功！");
        }
        return StatusDto.buildFailureStatusDto("操作失败！");
    }

    /**
     * 根据客户经理uuids查询名下的车辆数
     * @param cusmanagerUuids 客户经理uuids
     * @return 客户经理名下的车辆数
     * @author liuduo
     * @date 2018-08-02 10:09:30
     */
    @Override
    public List<CusmanagerCarCountDTO> queryCarNumByCusmanagerUuid(List<String> cusmanagerUuids) {
        if (cusmanagerUuids.isEmpty()) {
            return Collections.emptyList();
        }
        return basicCarcoreInfoDao.queryCarNumByCusmanagerUuid(cusmanagerUuids);
    }


}
