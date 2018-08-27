package com.ccbuluo.business.servicecenter.allocateapply.controller;

import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:10:24
 */
@RestController
@RequestMapping("/servicecenter/allocateapply")
@Api(tags = "申请管理（服务中心）")
public class ServiceAllocateApplyController extends BaseController {
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
    @ApiOperation(value = "创建物料或者零配件申请", notes = "【张康健】")
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
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @ApiOperation(value = "查询申请列表", notes = "【张康健】")
    @GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processType", value = "申请类型", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> list(String productType,String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyServiceImpl.findApplyList(productType, processType, applyStatus, applyNo, offset, pageSize);
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
        @ApiImplicitParam(name = "orgType", value = "申请来源", required = false, paramType = "query"),
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
     * 处理申请单
     * @param processApplyDTO json数据格式
     * @return
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    @ApiOperation(value = "处理申请单（当选择采购时不显示调拨目标）", notes = "【张康健】")
    @PostMapping("/processapply")
    public StatusDto<String> processApply(@ApiParam(name = "processApplyDTO", value = "json数据格式", required = true) @RequestBody ProcessApplyDTO processApplyDTO){
        allocateApplyServiceImpl.processApply(processApplyDTO);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查询可调拨库存列表", notes = "【张康健】")
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
    @ApiOperation(value = "查询可调拨库存列表", notes = "【张康健】")
    @GetMapping("/querytransferstock")
    public StatusDto<Page<QueryOrgDTO>> queryTransferStock(@ApiIgnore QueryOrgDTO queryOrgDTO,String productNo ,Integer offset, Integer pageSize){
        Page<QueryOrgDTO> queryOrgDTOPage = allocateApplyServiceImpl.queryTransferStock(queryOrgDTO, productNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(queryOrgDTOPage);
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



}
