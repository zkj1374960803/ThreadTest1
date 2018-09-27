package com.ccbuluo.business.custmanager.allocateapply.controller;

import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerService;
import com.ccbuluo.business.platform.instock.service.InstockOrderService;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:10:24
 */
@RestController
@RequestMapping("/custmanager/allocateapply")
@Api(tags = "申请管理（客户经理）")
public class CustAllocateApplyController extends BaseController {
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;
    @Resource(name = "custmanagerServiceImpl")
    private CustmanagerService custmanagerService;


    /**
     * 创建物料或者零配件申请
     * @param allocateApplyDTO 申请单实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @ApiOperation(value = "创建物料申请", notes = "【张康健】")
    @PostMapping("/create")
    public StatusDto<String> createAllocateApply(@ApiParam(name = "bizAllocateApply", value = "创建申请json", required = true) @RequestBody AllocateApplyDTO allocateApplyDTO){
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
        @ApiImplicitParam(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> list(String productType,String orgType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyServiceImpl.findApplyList(null, productType, orgType, processType, applyStatus, applyNo, offset, pageSize);
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
        @ApiImplicitParam(name = "processType", value = "申请类型", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> processList(String productType,String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyServiceImpl.findProcessApplyList(productType,processType, applyStatus, applyNo, offset, pageSize);
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
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看调拨库存", notes = "【张康健】")
    @GetMapping("/findstocklist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryCode", value = "分类的code", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> findStockList(@ApiIgnore FindStockListDTO findStockListDTO){
        Page<FindStockListDTO> page = allocateApplyServiceImpl.findStockList(findStockListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查询可调拨库存列表
     * @param queryOrgDTO 查询的条件
     * @param  offset 偏移量
     * @param  pageSize 每页显示的数量
     * @return StatusDto<Page<QueryOrgDTO>>
     * @author zhangkangjian
     * @date 2018-08-13 16:50:05
     */
    @ApiOperation(value = "查看调拨库存", notes = "【张康健】")
    @GetMapping("/querytransferstock")
    public StatusDto<Page<QueryOrgDTO>> queryTransferStock(@ApiIgnore QueryOrgDTO queryOrgDTO, String productNo, Integer offset, Integer pageSize){
        Page<QueryOrgDTO> queryOrgDTOPage = allocateApplyServiceImpl.queryTransferStock(queryOrgDTO, productNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(queryOrgDTOPage);
    }

    /**
     * 问题件申请查询(创建问题件，查询问题件列表)
     * @param orgCode 机构的code
     * @return StatusDto<List<StockBizStockDetailDTO>>
     * @author zhangkangjian
     * @date 2018-08-22 14:37:40
     */
    @ApiOperation(value = "问题件申请查询(创建问题件，查询问题件列表)",notes = "【张康健】")
    @GetMapping("/queryproblemstocklist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orgCode", value = "所属机构的编号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)", required = false, paramType = "query")
    })

    public StatusDto<List<StockBizStockDetailDTO>> queryProblemStockList(String orgCode, String productType) {
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.queryProblemStockList(orgCode, productType));
    }

    /**
     * 查询客户经理关联的服务中心
     * @param useruuid
     * @return StatusDto<Map<String,String>>
     * @author zhangkangjian
     * @date 2018-08-24 17:37:13
     */
    @GetMapping("/findcustmanagerservicecenter")
    @ApiOperation(value = "查询客户经理关联的服务中心", notes = "【张康健】")
    @ApiImplicitParam(name = "useruuid", value = "用户uuid", required = false, paramType = "query")
    public StatusDto<Map<String, String>> findCustManagerServiceCenter(String useruuid) throws IOException {
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.findCustManagerServiceCenter(useruuid));
    }

    /**
     * 查询客户经理待领取的物料
     * @param completeStatus  "状态（PENDING待领取，CONFIRMRECEIPT已领取）
     * @param keyword 物料编号/物料名称
     * @param offset 偏移量
     * @param pageSize 每页显示的数量
     * @return StatusDto<List<QueryPendingMaterialsDTO>>
     * @author zhangkangjian
     * @date 2018-08-25 20:40:35
     */
    @GetMapping("/querypendingmaterials")
    @ApiOperation(value = "查询客户经理待领取的物料", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "completeStatus", value = "状态（DOING待领取，COMPLETE已领取）", required = false, paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "物料编号/物料名称", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<QueryPendingMaterialsDTO>> queryPendingMaterials(String completeStatus, String keyword, Integer offset, Integer pageSize){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyServiceImpl.queryPendingMaterials(completeStatus, keyword, offset, pageSize));
    }

    /**
     * 客户经理领取物料
     * @param id 入库计划的id
     * @param productNo 商品的编号
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-27 14:53:10
     */
    @PostMapping("/receivingmaterials")
    @ApiOperation(value = "客户经理领取物料", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = true, paramType = "query")
    })
    public StatusDto<String> receivingmaterials(Long id, String productNo){
        allocateApplyServiceImpl.receivingmaterials(id, productNo);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询出入库计划
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


}
