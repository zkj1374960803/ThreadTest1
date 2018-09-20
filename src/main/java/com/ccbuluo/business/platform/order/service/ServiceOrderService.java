package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.order.dto.*;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.ServiceCenterDTO;

import java.util.List;
import java.util.Map;

/**
 * 描述 服务服务单service
 * @author liuduo
 * @date 2018-09-03 17:35:19
 * @version V1.0.0
 */
public interface ServiceOrderService {
    /**
     * 描述 新增服务服务单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author liuduo
     * @date 2018-09-03 18:54:01
     */
    StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO);


    /**
     * 编辑服务单
     * @param editServiceOrderDTO 服务单要保存的信息
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    StatusDto editOrder(EditServiceOrderDTO editServiceOrderDTO);

    /**
     * 查询服务单列表
     * @param orderStatus 服务单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 服务单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    StatusDto<Page<BizServiceOrder>> queryList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize);

    /**
     * 修改服务单状态
     * @param serviceOrderno 服务单编号
     * @param orderStatus 服务单状态
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
     * 根据服务单编号查询服务单详情
     * @param serviceOrderno 服务单编号
     * @return 服务单详情
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
     * @param pageSize 每页数
     * @return 可以分配的客户经理和服务中心
     * @author liuduo
     * @date 2018-09-05 10:40:35
     */
    StatusDto<Page<ServiceCenterDTO>> serviceCenterList(String province, String city, String area, String orgType, String keyword, Integer offset, Integer pageSize);

    /**
     * 分配服务单
     * @param serviceOrderno 服务单编号
     * @param orgType 类型（服务中心还是客户经理）
     * @param orgCodeOrUuid 机构编号或者客户经理uuid
     * @return 服务单是否分配成功
     * @author liuduo
     * @date 2018-09-05 18:32:52
     */
    StatusDto orderAllocation(String serviceOrderno, String orgType, String orgCodeOrUuid);

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     *  @param pageSize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-09-06 10:39:33
     */
    StatusDto<Page<DetailBizServiceMaintainitemDTO>> queryMaintainitem(String keyword, Integer offset, Integer pageSize);

    /**
     * 保存维修单详单
     * @param saveOrderDetailDTO 维修单详单
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-09-06 17:04:02
     */
    StatusDto saveOrderDetail(SaveOrderDetailDTO saveOrderDetailDTO);

    /**
     * 提交服务单
     * @param serviceOrderno 服务单编号
     * @return 服务单是否提交成功
     * @author weijb
     * @date 2018-09-07 17:32:52
     */
    StatusDto ordersubmit(String serviceOrderno);

    /**
     * 验收
     * @param serviceOrderno 服务单编号
     * @return 服务单是否验收成功
     * @author weijb
     * @date 2018-09-07 17:32:52
     */
    StatusDto acceptance(String serviceOrderno);

    /**
     * 查询维修单日志
     * @param serviceOrderno 维修单编号
     * @return 维修单日志
     * @author liuduo
     * @date 2018-09-10 14:45:37
     */
    StatusDto<List<BizServiceLog>> orderLog(String serviceOrderno);

    /**
     * 查询维修单的工时详情和零配件详情
     * @param serviceOrderno 维修单的编号
     * @return Map<String,List<ProductDetailDTO>>
     * @author zhangkangjian
     * @date 2018-09-10 17:41:01
     */
    Map<String,List<ProductDetailDTO>> querymaintainitemAndFittingDetail(String serviceOrderno);

    /**
     * 查询维修单列表(门店用)
     * @param orderStatus 维修单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 维修单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    StatusDto<Page<BizServiceOrder>> queryStoreList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize);

    /**
     * 根据工时code查询公司是否被引用
     * @param maintainitemCode 工时code
     * @return 工时是否被引用
     * @author liuduo
     * @date 2018-09-18 14:36:12
     */
    Boolean getByProductCode(String maintainitemCode);

    /**
     * 查询维修单状态数量
     * @return 维修单各种状态的数量
     * @author liuduo
     * @date 2018-09-19 17:21:44
     */
    Map<String, Long> queryOrderStatusNum(String reportOrgno);

    /**
     * 查询维修单状态数量(门店用)
     * @return 维修单各种状态的数量
     * @author liuduo
     * @date 2018-09-19 17:21:44
     */
    Map<String, Long> queryStoreOrderStatusNum(String reportOrgno);
}
