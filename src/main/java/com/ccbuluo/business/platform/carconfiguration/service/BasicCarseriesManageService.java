package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;
import java.util.Map;

/**
 * 车系管理service
 * @author chaoshuai
 * @date 2018-05-08 10:55:24
 */
public interface BasicCarseriesManageService {

    /**
     * 车系新增
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    StatusDto saveCarseriesManage(CarseriesManage carseriesManage);

    /**
     * 车系编辑
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    StatusDto updateCarseriesManage(CarseriesManage carseriesManage);

    /**
     * 车系删除
     * @param id 车系id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    StatusDto deleteCarseriesManage(Long id);

    /**
     * 车系详情
     * @param id 车系id
     * @return
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:55:05
     */
    CarseriesManage findCarseriesManageDetail(Long id);

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
    Page<Map<String, Object>> queryCarseriesManagePage(Long carbrandId, String carseriesName, int offset, int limit);

    /**
     * 查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    List<CarseriesManage> queryCarseriesManageList(Long carbrandId, String carseriesName);

    /**
     * 查询所有车系 下拉框
     * @param
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    List<CarseriesManage> queryAllCarseriesManageList();
}
