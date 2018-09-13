package com.ccbuluo.business.platform.order.controller;

import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.order.dto.DetailServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.EditServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.SaveOrderDetailDTO;
import com.ccbuluo.business.platform.order.dto.SaveServiceOrderDTO;
import com.ccbuluo.business.platform.order.service.ServiceOrderService;
import com.ccbuluo.business.vehiclelease.resdto.CarLesseeResDTO;
import com.ccbuluo.business.vehiclelease.service.CarcoreInfoService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.ServiceCenterDTO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述 维修单管理，客户经理
 * @author liuduo
 * @date 2018-09-03 16:22:02
 * @version V1.0.0
 */
@Api(tags = "维修单管理（门店）")
@RestController
@RequestMapping("/platform/storeserviceorder")
public class StoreServiceOrderController extends BaseController {

    @Autowired
    private ServiceOrderService serviceOrderService;
    @Autowired
    private BasicCarcoreInfoService basicCarcoreInfoService;
    @ThriftRPCClient("BasicOrdergenerationSerService")
    private CarcoreInfoService carcoreInfoService;

    /**
     * 保存维修单
     * @param saveServiceOrderDTO 维修单要保存的信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    @ApiOperation(value = "维修单新增", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveOrder(@ApiParam(name = "维修单信息", value = "传入json格式", required = true)@RequestBody SaveServiceOrderDTO saveServiceOrderDTO) {
        return serviceOrderService.saveOrder(saveServiceOrderDTO);
    }



    /**
     * 编辑维修单
     * @param editServiceOrderDTO 维修单要保存的信息
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    @ApiOperation(value = "维修单编辑", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto editOrder(@ApiParam(name = "维修单信息", value = "传入json格式", required = true)@RequestBody EditServiceOrderDTO editServiceOrderDTO) {
        return serviceOrderService.editOrder(editServiceOrderDTO);
    }


    /**
     * 查询维修单列表
     * @param orderStatus 维修单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 维修单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    @ApiOperation(value = "维修单列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderStatus", value = "维修单状态",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "serviceType", value = "服务类型",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "reportOrgno", value = "当前登录人的机构编号",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "关键字",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数",  required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pagesize", value = "每页数",  required = true, paramType = "query", dataType = "int")})
    @GetMapping("/list")
    public StatusDto<Page<BizServiceOrder>> queryList(@RequestParam(required = false) String orderStatus,
                                                      @RequestParam(required = false) String serviceType,
                                                      @RequestParam(required = false) String reportOrgno,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "10") Integer pagesize) {
        return serviceOrderService.queryList(orderStatus, serviceType, reportOrgno, keyword, offset, pagesize);
    }


    /**
     * 修改维修单状态
     * @param serviceOrderno 维修单编号
     * @param orderStatus 维修单状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-09-04 15:37:36
     */
    @ApiOperation(value = "更改维修单状态", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "serviceOrderno", value = "维修单编号",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "orderStatus", value = "维修单类型",  required = true, paramType = "query")})
    @PostMapping("/editstatus")
    public StatusDto editStatus(@RequestParam String serviceOrderno,@RequestParam String orderStatus) {
        return serviceOrderService.editStatus(serviceOrderno, orderStatus);
    }


    /**
     * 查询车牌号下拉框
     * @return 所有车牌号
     * @author liuduo
     * @date 2018-09-04 17:34:08
     */
    @ApiOperation(value = "维修单新增和编辑用的车牌号下拉框", notes = "【刘铎】")
    @GetMapping("/querycarnolist")
    public StatusDto<List<String>> queryCarNoList() {
        return serviceOrderService.queryCarNoList();
    }

    /**
     * 根据车牌号查询车辆信息
     * @param carNo 车牌号
     * @return 车辆信息
     * @author liuduo
     * @date 2018-09-04 16:18:43
     */
    @ApiOperation(value = "根据车牌号查询车辆信息", notes = "【刘铎】")
    @ApiImplicitParam(name = "carNo", value = "车牌号",  required = false, paramType = "query")
    @GetMapping("/getcarbycarno")
    public StatusDto<CarcoreInfoDTO> getCarByCarNo(@RequestParam String carNo) {
        return basicCarcoreInfoService.getCarByCarNo(carNo);
    }


    /**
     * 根据车辆vin码查询车辆承租人的信息
     * @param vinNumber 车辆vin码
     * @return 车辆承租人的信息
     * @author liuduo
     * @date 2018-09-10 10:09:50
     */
    @ApiOperation(value = "根据车辆vin码查询车辆承租人的信息", notes = "【刘铎】")
    @ApiImplicitParam(name = "vinNumber", value = "车辆vin码",  required = false, paramType = "query")
    @GetMapping("/getuserbycarno")
    public StatusDto<CarLesseeResDTO> getUserByCarno(@RequestParam String vinNumber) {
        StatusDtoThriftBean<CarLesseeResDTO> carLessee = carcoreInfoService.findCarLessee(vinNumber);
        return StatusDto.buildDataSuccessStatusDto(StatusDtoThriftUtils.resolve(carLessee, CarLesseeResDTO.class).getData());
    }



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
    @ApiOperation(value = "查询服务中心和客户经理（分配用）", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "province", value = "省",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "city", value = "市",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "area", value = "区",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "orgType", value = "机构类型",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "关键字",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数",  required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pagesize", value = "每页数",  required = true, paramType = "query", dataType = "int")})
    @GetMapping("/servicecenterlist")
    public StatusDto<Page<ServiceCenterDTO>> serviceCenterList(@RequestParam(required = false) String province,
                                                               @RequestParam(required = false) String city,
                                                               @RequestParam(required = false) String area,
                                                               @RequestParam(required = false) String orgType,
                                                               @RequestParam(required = false) String keyword,
                                                               @RequestParam(defaultValue = "0") Integer offset,
                                                               @RequestParam(defaultValue = "10") Integer pagesize) {
        return serviceOrderService.serviceCenterList(province, city, area, orgType, keyword, offset, pagesize);
    }


    /**
     * 根据维修单编号查询维修单详情
     * @param serviceOrderno 维修单编号
     * @return 维修单详情
     * @author liuduo
     * @date 2018-09-05 09:54:40
     */
    @ApiOperation(value = "维修单详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "serviceOrderno", value = "维修单编号",  required = true, paramType = "query")
    @GetMapping("/getdetailbyorderno")
    public StatusDto<DetailServiceOrderDTO> getDetailByOrderNo(@RequestParam String serviceOrderno) {
        return serviceOrderService.getDetailByOrderNo(serviceOrderno);
    }


    /**
     * 查询维修单日志
     * @param serviceOrderno 维修单编号
     * @return 维修单日志
     * @author liuduo
     * @date 2018-09-10 14:45:37
     */
    @ApiOperation(value = "维修单日志", notes = "【刘铎】")
    @ApiImplicitParam(name = "serviceOrderno", value = "维修单编号",  required = true, paramType = "query")
    @GetMapping("/orderlog")
    public StatusDto<List<BizServiceLog>> orderLog(@RequestParam String serviceOrderno) {
        return serviceOrderService.orderLog(serviceOrderno);
    }


    /**
     * 分配维修单
     * @param serviceOrderno 维修单编号
     * @param orgCodeOrUuid 机构编号或者客户经理uuid
     * @return 维修单是否分配成功
     * @author liuduo
     * @date 2018-09-05 18:32:52
     */
    @ApiOperation(value = "分配维修单", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "serviceOrderno", value = "维修单编号",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "orgType", value = "分配的目标类型（服务中心）",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "orgCodeOrUuid", value = "机构编号",  required = true, paramType = "query")})
    @PostMapping("/orderallocation")
    public StatusDto orderAllocation(@RequestParam String serviceOrderno, @RequestParam String orgType, @RequestParam String orgCodeOrUuid) {
        return serviceOrderService.orderAllocation(serviceOrderno, orgType, orgCodeOrUuid);
    }


    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     *  @param pagesize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-09-06 10:39:33
     */
    @ApiOperation(value = "处理时添加工时的列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数", paramType = "query", dataType = "int",required = true),
        @ApiImplicitParam(name = "pagesize", value = "每页数", paramType = "query", dataType = "int",required = true)})
    @GetMapping("/querymaintainitem")
    public StatusDto<Page<DetailBizServiceMaintainitemDTO>> queryMaintainitem(@RequestParam(required = false) String keyword,
                                                                              @RequestParam(defaultValue = "0") Integer offset,
                                                                              @RequestParam(defaultValue = "10") Integer pagesize) {
        return serviceOrderService.queryMaintainitem(keyword, offset, pagesize);
    }


    /**
     * 保存维修单详单
     * @param saveOrderDetailDTO 维修单详单
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-09-06 17:04:02
     */
    @ApiOperation(value = "保存工时和零配件", notes = "【刘铎】")
    @PostMapping("/saveorderdetail")
    public StatusDto saveOrderDetail(@ApiParam(name = "维修单信息", value = "传入json格式", required = true)@RequestBody SaveOrderDetailDTO saveOrderDetailDTO) {
        return serviceOrderService.saveOrderDetail(saveOrderDetailDTO);
    }

    /**
     * 提交订单
     * @param serviceOrderno 订单编号
     * @return 订单是否提交成功
     * @author weijb
     * @date 2018-09-07 17:32:52
     */
    @ApiOperation(value = "提交订单", notes = "【魏俊标】")
    @ApiImplicitParam(name = "serviceOrderno", value = "订单编号",  required = true, paramType = "query")
    @PostMapping("/ordersubmit")
    public StatusDto ordersubmit(@RequestParam String serviceOrderno) {
        return serviceOrderService.ordersubmit(serviceOrderno);
    }

    /**
     * 验收
     * @param serviceOrderno 订单编号
     * @return 订单是否提交成功
     * @author weijb
     * @date 2018-09-07 18:32:52
     */
    @ApiOperation(value = "验收", notes = "【魏俊标】")
    @ApiImplicitParam(name = "serviceOrderno", value = "订单编号",  required = true, paramType = "query")
    @PostMapping("/acceptance")
    public StatusDto acceptance(@RequestParam String serviceOrderno) {
        return serviceOrderService.acceptance(serviceOrderno);
    }



}
