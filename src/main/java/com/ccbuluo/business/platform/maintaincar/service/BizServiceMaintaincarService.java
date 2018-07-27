package com.ccbuluo.business.platform.maintaincar.service;

import com.ccbuluo.business.entity.BizServiceMaintaincar;
import com.ccbuluo.business.platform.maintaincar.dto.SearchBizServiceMaintaincarDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

/**
 * 车辆基本信息service
 * @author chaoshuai
 * @date 2018-05-08 10:55:54
 */
public interface BizServiceMaintaincarService {

    /**
     * 新增车辆
     * @param bizServiceMaintaincar 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    StatusDto saveServiceMaintaincar(BizServiceMaintaincar bizServiceMaintaincar);
    /**
     * 编辑车辆
     * @param bizServiceMaintaincar 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    StatusDto editServiceMaintaincar(BizServiceMaintaincar bizServiceMaintaincar);

    /**
     * 车辆基本信息验证唯一性
     * @param bizServiceMaintaincar 车辆基本信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    StatusDto findServiceMaintaincarVerification(BizServiceMaintaincar bizServiceMaintaincar);


    /**
     * 根据车辆id查询车辆详情
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    BizServiceMaintaincar queryServiceMaintaincarByCarId(Long carId);

    /**
     * 根据车辆id删除车辆
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    int deleteServiceMaintaincarByCarId(Long carId);
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
    Page<SearchBizServiceMaintaincarDTO> queryServiceMaintaincarList(Long carbrandId, Long carseriesId, Integer carStatus, String keyword, Integer offset, Integer pageSize);

}
