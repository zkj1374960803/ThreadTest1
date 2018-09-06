package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.order.dto.DetailServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.EditServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.SaveServiceOrderDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.ServiceCenterDTO;

import java.util.List;

/**
 * 描述 服务订单service
 * @author liuduo
 * @date 2018-09-03 17:35:19
 * @version V1.0.0
 */
public interface ServiceOrderService {
    /**
     * 描述 新增服务订单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author liuduo
     * @date 2018-09-03 18:54:01
     */
    StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO);


    /**
     * 编辑订单
     * @param editServiceOrderDTO 订单要保存的信息
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    StatusDto editOrder(EditServiceOrderDTO editServiceOrderDTO);

    /**
     * 查询订单列表
     * @param orderStatus 订单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 订单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    StatusDto<Page<BizServiceOrder>> queryList(String orderStatus, String serviceType, String keyword, Integer offset, Integer pagesize);

    /**
     * 修改订单状态
     * @param serviceOrderno 订单编号
     * @param orderStatus 订单状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-09-04 15:37:36
     */
    StatusDto editStatus(String serviceOrderno, String orderStatus);

    /**
     * 查询车牌号下拉框
     * @return 所有车牌号
     * @author liuduo
     * @date 2018-09-04 17:34:08
     */
    StatusDto<List<String>> queryCarNoList();

    /**
     * 根据订单编号查询订单详情
     * @param serviceOrderno 订单编号
     * @return 订单详情
     * @author liuduo
     * @date 2018-09-05 09:54:40
     */
    StatusDto<DetailServiceOrderDTO> getDetailByOrderNo(String serviceOrderno);

    /**
     * 查询服务中心和客户经理（分配用）
     * @param province 省
     * @param city 市
     * @param area 区
     * @param orgType 机构类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 可以分配的客户经理和服务中心
     * @author liuduo
     * @date 2018-09-05 10:40:35
     */
    StatusDto<Page<ServiceCenterDTO>> serviceCenterList(String province, String city, String area, String orgType, String keyword, Integer offset, Integer pagesize);

    /**
     * 分配订单
     * @param serviceOrderno 订单编号
     * @param orgCodeOrUuid 机构编号或者客户经理uuid
     * @return 订单是否分配成功
     * @author liuduo
     * @date 2018-09-05 18:32:52
     */
    StatusDto orderAllocation(String serviceOrderno, String orgCodeOrUuid);
}
