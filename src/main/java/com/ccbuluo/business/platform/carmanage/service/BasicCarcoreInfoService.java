package com.ccbuluo.business.platform.carmanage.service;

import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carmanage.dto.ListCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.dto.SearchCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.dto.UpdateCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.dto.VinCarcoreInfoDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 车辆基本信息service
 * @author chaoshuai
 * @date 2018-05-08 10:55:54
 */
public interface BasicCarcoreInfoService {

    /**
     * 新增车辆
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    StatusDto saveCarcoreInfo(CarcoreInfo carcoreInfo);
    /**
     * 编辑车辆
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    StatusDto editCarcoreInfo(CarcoreInfo carcoreInfo);

    /**
     * 车辆基本信息验证唯一性
     * @param carcoreInfo 车辆基本信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    StatusDto findCarcoreInfoVerification(CarcoreInfo carcoreInfo);


    /**
     * 根据车辆id查询车辆详情
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    CarcoreInfo queryCarDetailByCarId(Long carId);

    /**
     * 根据车辆id删除车辆
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    StatusDto deleteCarcoreInfoByCarId(Long carId);
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
    Page<SearchCarcoreInfoDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, Integer carStatus, String Keyword, Integer offset, Integer pageSize);

    /**
     * 查询未分配的车辆列表
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    List<ListCarcoreInfoDTO> queryundistributedlist();
    /**
     * 根据车辆code更新车辆状态
     * @param list
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    List<Long> updatestatusbycode(List<UpdateCarcoreInfoDTO> list);

    /**
     * 根据车架号查询车辆信息
     * @param vinNumber 车辆vin
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    VinCarcoreInfoDTO getCarInfoByVin(String vinNumber);
}
