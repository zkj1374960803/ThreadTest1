package com.ccbuluo.business.platform.allocateapply.controller;

import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerService;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.core.validate.Group;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterListDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:10:24
 */
@RestController
@RequestMapping("/platform/allocateapply")
@Api(tags = "申请管理")
public class AllocateApplyController extends BaseController {
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;
    @Resource(name = "custmanagerServiceImpl")
    private CustmanagerService custmanagerService;
    @Resource
    BizInstockplanDetailDao bizInstockplanDetailDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;

    /**
     * 创建物料或者零配件申请
     * @param allocateApplyDTO 申请单实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @ApiOperation(value = "创建物料或者零配件申请", notes = "【张康健】")
    @PostMapping("/create")
    public StatusDto<String> createAllocateApply(@ApiParam(name = "bizAllocateApply", value = "创建申请json", required = true) @RequestBody @ValidateGroup(Group.Add.class) AllocateApplyDTO allocateApplyDTO){
        allocateApplyServiceImpl.createAllocateApply(allocateApplyDTO);
        return StatusDto.buildSuccessStatusDto();
    }



    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return StatusDto<FindAllocateApplyDTO>
     * @author zhangkangjian
     * @date 2018-08-08 17:02:58
     */
    @ApiOperation(value = "查询申请单详情", notes = "【张康健】")
    @GetMapping("/detail/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> detail(@PathVariable String applyNo){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.findDetail(applyNo));
    }

    /**
     * 查询申请列表
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @ApiOperation(value = "查询申请列表", notes = "【张康健】")
    @GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orgType", value = "申请来源 SERVICECENTER(服务中心)CUSTMANAGER(客户经理)PLATFORM(售后平台);", required = false, paramType = "query"),
        @ApiImplicitParam(name = "whetherQueryAll", value = "是否查询全部", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> list(Boolean whetherQueryAll, String productType,String orgType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyServiceImpl.findApplyList(whetherQueryAll, productType, orgType, processType, applyStatus, applyNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查询处理申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @ApiOperation(value = "查询处理申请列表", notes = "【张康健】")
    @GetMapping("/findprocesslist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orgType", value = "申请来源 SERVICECENTER(服务中心)CUSTMANAGER(客户经理)PLATFORM(售后平台);", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> processList(String productType,String orgType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyServiceImpl.findProcessApplyList(productType,orgType, applyStatus, applyNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查询客户经理列表(创建申请)
     * @param queryCustManagerListDTO 查询条件
     * @return StatusDto<Page<QueryCustManagerListDTO>>
     * @author zhangkangjian
     * @date 2018-08-09 16:58:03
     */
    @ApiOperation(value = "查询客户经理列表", notes = "【张康健】")
    @GetMapping("/querycustmanagerlist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "客户经理的姓名或vin码", required = false, paramType = "query"),
        @ApiImplicitParam(name = "serviceCenter", value = "服务中心的code", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<QueryCustManagerListDTO>> queryCustManagerList(@ApiIgnore QueryCustManagerListDTO queryCustManagerListDTO){
        Page<QueryCustManagerListDTO> page = custmanagerService.queryCustManagerList(queryCustManagerListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 提交申请
     * @param processApplyDTO json数据格式
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    @ApiOperation(value = "提交申请", notes = "【张康健】")
    @PostMapping("/processapply")
    public StatusDto<String> processApply(@ApiParam(name = "processApplyDTO", value = "json数据格式", required = true) @RequestBody ProcessApplyDTO processApplyDTO){
        allocateApplyServiceImpl.processApply(processApplyDTO);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 保存处理申请单
     * @param processApplyDetailDTO json数组数据格式
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-09-12 16:02:08
     */
    @ApiOperation(value = "保存申请单（填报价格）", notes = "【张康健】")
    @PostMapping("/saveprocessapply")
    public StatusDto<String> saveProcessApply(@ApiParam(name = "processApplyDetailDTO", value = "json数组数据格式", required = true) @RequestBody List<ProcessApplyDetailDTO> processApplyDetailDTO){
        allocateApplyServiceImpl.saveProcessApply(processApplyDetailDTO);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 撤销申请单
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 12:02:58
     */
    @ApiOperation(value = "撤销申请单", notes = "【魏俊标】")
    @GetMapping("/cancelapply/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto cancelApply(@PathVariable String applyNo){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.cancelApply(applyNo));
    }


    /**
     * 查询售后平台的信息
     * @return StatusDto<BasicUserOrganization>
     * @author zhangkangjian
     * @date 2018-08-23 11:12:47
     */
    @ApiOperation(value = "查询平台的信息",notes = "【张康健】")
    @GetMapping("/querytopplatform")
    public StatusDto<List<BasicUserOrganization>> queryTopPlatform(){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.queryTopPlatform());
    }


    /**
     * 驳回申请
     * @param applyNo 申请单号
     * @return StatusDto<String> 申请单号
     * @author zhangkangjian
     * @date 2018-09-12 16:10:34
     */
    @ApiOperation(value = "驳回申请",notes = "【张康健】")
    @GetMapping("/rejectapply")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "processMemo", value = "驳回理由", required = true, paramType = "query")
    })
    public StatusDto<String> rejectApply(String applyNo, String processMemo){
        allocateApplyServiceImpl.rejectApply(applyNo, processMemo);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 处理申请(确定出库机构)
     * @param applyNo 申请单号
     * @param outstockOrgno 出库机构
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-09-12 17:47:14
     */
    @ApiOperation(value = "处理申请",notes = "【张康健】")
    @GetMapping("/processoutstockorg")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "outstockOrgno", value = "出库机构", required = true, paramType = "query")
    })
    public StatusDto<String> processOutStockOrg(String applyNo, String outstockOrgno){
        allocateApplyServiceImpl.processOutStockOrg(applyNo, outstockOrgno);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询采购列表
     * @param queryPurchaseListDTO 查询条件
     * @return StatusDto<Page<QueryPurchaseListDTO>>
     * @author zhangkangjian
     * @date 2018-09-13 09:58:35
     */
    @PostMapping("/querypurchaselise")
    @ApiOperation(value = "查询采购列表", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applyNo", value = "采购单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "商品的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<QueryPurchaseListDTO>> queryPurchaseLise(@ApiIgnore QueryPurchaseListDTO queryPurchaseListDTO){
        Page<QueryPurchaseListDTO> page = allocateApplyServiceImpl.queryPurchaseLise(queryPurchaseListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 创建采购单
     * @param createPurchaseBillDTO
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-09-13 13:58:37
     */
    @PostMapping("/createpurchasebill")
    @ApiOperation(value = "创建采购单", notes = "【张康健】")
    public StatusDto<String> createPurchaseBill(@ApiParam(name = "CreatePurchaseBillDTO", value = "创建采购申请json", required = true) @RequestBody CreatePurchaseBillDTO createPurchaseBillDTO){
        allocateApplyServiceImpl.createPurchaseBill(createPurchaseBillDTO);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 保存价格(采购单)
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-15 15:36:20
     */
    @PostMapping("/saveQuote")
    @ApiOperation(value = "保存价格(采购单)", notes = "【张康健】")
    public StatusDto<String> saveQuote(@ApiParam(name = "ConfirmationQuoteDTO", value = "采购单填报价格（确认报价）", required = true) @RequestBody ConfirmationQuoteDTO confirmationQuoteDTO){
        allocateApplyServiceImpl.saveQuote(confirmationQuoteDTO);
        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 采购单填报价格（确认报价）
     * @param confirmationQuoteDTO  报价DTO
     * @return  StatusDto<String>
     * @author zhangkangjian
     * @date 2018-09-13 15:45:47
     */
    @PostMapping("/confirmationquote")
    @ApiOperation(value = "采购单填报价格（确认报价）", notes = "【张康健】")
    public StatusDto<String> confirmationQuote(@ApiParam(name = "ConfirmationQuoteDTO", value = "采购单填报价格（确认报价）", required = true) @RequestBody ConfirmationQuoteDTO confirmationQuoteDTO){
        allocateApplyServiceImpl.confirmationQuote(confirmationQuoteDTO);
        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 查询采购单的付款信息
     * @param applyNo 采购单号
     * @return StatusDto<List<PerpayAmountDTO>>
     * @author zhangkangjian
     * @date 2018-09-13 11:17:58
     */
    @PostMapping("/querypaymentinfo")
    @ApiOperation(value = "查询采购单的付款信息", notes = "【张康健】")
    @ApiImplicitParam(name = "applyNo", value = "采购单号", required = true, paramType = "query")
    public StatusDto<List<PerpayAmountDTO>> queryPaymentInfo(String applyNo){
        List<PerpayAmountDTO> perpayAmountDTO = allocateApplyServiceImpl.queryPaymentInfo(applyNo);
        return StatusDto.buildDataSuccessStatusDto(perpayAmountDTO);
    }

    /**
     * 查询出入库计划基本信息
     * @param applyNo 申请单号
     * @return 入库计划
     * @author zhangkangjian
     * @date 2018-09-13 11:17:58
     */
    @GetMapping("/queryinandoutstockplan")
    @ApiOperation(value = "查询出入库计划", notes = "【张康健】")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "query")
    public StatusDto<Map<String, Object>> queryInStockplan(String applyNo) {
        Map<String, Object> map = allocateApplyServiceImpl.queryListByApplyNoAndInReNo(applyNo);
        return StatusDto.buildDataSuccessStatusDto(map);
    }

    /**
     * 查询可用的服务中心
     * @param province 省
     * @param city 市
     * @param area 区
     * @param name 服务中心名字
     * @return 可用的服务中心
     * @author liuduo
     * @date 2018-07-06 10:00:52
     */
    @ApiOperation(value = "查询可用的服务中心", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "province", value = "省",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "city", value = "市",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "area", value = "区",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "name", value = "服务中心名字",  required = false, paramType = "query")})
    @GetMapping("/queryusableservicecenter")
    public StatusDto<List<QueryServiceCenterListDTO>> queryUsableServiceCenter(@RequestParam(required = false) String province,
                                                                              @RequestParam(required = false) String city,
                                                                              @RequestParam(required = false) String area,
                                                                              @RequestParam(required = false) String name) {
        StatusDtoThriftList<QueryServiceCenterListDTO> queryServiceCenterListDTOStatusDtoThriftList = orgService.findUsableServiceCenter(province, city, area, name);
        return StatusDtoThriftUtils.resolve(queryServiceCenterListDTOStatusDtoThriftList, QueryServiceCenterListDTO.class);
    }



}
