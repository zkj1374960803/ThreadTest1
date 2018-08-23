package com.ccbuluo.business.platform.stockdetail.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dto.CheckStockQuantityDTO;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProductStockInfoDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.stockdetail.dao.FindBatchStockListDTO;
import com.ccbuluo.business.platform.stockdetail.dao.FindStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.service.StockManagementService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 库存管理
 * @author zhangkangjian
 * @date 2018-08-16 10:23:20
 */
@Api(tags = "库存管理")
@RestController
@RequestMapping("/platform/stockmanagement")
public class StockManagementController extends BaseController {
    @Resource(name = "stockManagementServiceImpl")
    private StockManagementService stockManagementService;
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;


    /**
     * 查看物料调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看物料调拨库存", notes = "【张康健】")
    @GetMapping("/findequipmentstocklist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "equiptypeId", value = "物料类型的id", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "orgNo", value = "服务中心的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "type", value = "(注：PLATFORM集团，SERVICECENTER服务中心，CUSTMANAGER客户经理)", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> findEquipmentStockList(@ApiIgnore FindStockListDTO findStockListDTO){
        findStockListDTO.setProductType(Constants.PRODUCT_TYPE_EQUIPMENT);
        Page<FindStockListDTO> page = allocateApplyServiceImpl.findStockList(findStockListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查看零配件调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看零配件调拨库存", notes = "【张康健】")
    @GetMapping("/findfittingsstocklist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryCode", value = "零配件类型的code", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "orgNo", value = "服务中心的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "type", value = "(注：PLATFORM集团，SERVICECENTER服务中心，CUSTMANAGER客户经理)", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> findFittingsStockList(@ApiIgnore FindStockListDTO findStockListDTO){
        findStockListDTO.setProductType(Constants.PRODUCT_TYPE_FITTINGS);
        Page<FindStockListDTO> page = allocateApplyServiceImpl.findStockList(findStockListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查看库存详情
     * @param productNo 商品的编号
     * @param productType 商品类型
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看库存详情", notes = "【张康健】")
    @GetMapping("/findstockproductdetail")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)", required = true, paramType = "query"),
        @ApiImplicitParam(name = "type", value = "(注：PLATFORM集团，SERVICECENTER服务中心，CUSTMANAGER客户经理)", required = true, paramType = "query")
    })
    public StatusDto<FindStockDetailDTO> findStockProductDetail(String productNo, String productType, String type){
        FindStockDetailDTO findStockDetailDTO = stockManagementService.findStockProductDetail(productNo, productType, type);
        return StatusDto.buildDataSuccessStatusDto(findStockDetailDTO);
    }

    /**
     * 查看库存详情（批次库存列表查询）
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindBatchStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-21 10:14:35
     */
    @ApiOperation(value = "查看库存详情（批次库存列表查询）", notes = "【张康健】")
    @GetMapping("/findbatchstocklist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)", required = true, paramType = "query"),
        @ApiImplicitParam(name = "type", value = "(注：PLATFORM集团，SERVICECENTER服务中心，CUSTMANAGER客户经理)", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindBatchStockListDTO>> findBatchStockList(@ApiIgnore FindStockListDTO findStockListDTO){
        Page<FindBatchStockListDTO> page = stockManagementService.findBatchStockList(findStockListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查看调拨库存
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
        @ApiImplicitParam(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> findStockList(@ApiIgnore FindStockListDTO findStockListDTO){
        Page<FindStockListDTO> page = allocateApplyServiceImpl.findStockList(findStockListDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 可调拨库存
     * @param queryOrgDTO 查询的条件
     * @param  offset 偏移量
     * @param  pageSize 每页显示的数量
     * @return StatusDto<Page<QueryOrgDTO>>
     * @author zhangkangjian
     * @date 2018-08-13 16:50:05
     */
    @ApiOperation(value = "可调拨库存", notes = "【张康健】")
    @GetMapping("/querytransferstock")
    public StatusDto<Page<QueryOrgDTO>> queryTransferStock(@ApiIgnore QueryOrgDTO queryOrgDTO, Integer offset, Integer pageSize){
        Page<QueryOrgDTO> queryOrgDTOPage = allocateApplyServiceImpl.queryTransferStock(queryOrgDTO, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(queryOrgDTOPage);
    }

    /**
     * 检查库存
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-15 13:51:06
     */
    @ApiOperation(value = "检查库存", notes = "【张康健】")
    @PostMapping("/checkstockquantity")
    public StatusDto<List<ProductStockInfoDTO>> checkStockQuantity(@ApiParam(name = "CheckStockQuantityDTO", value = "Json数据", required = true) @RequestBody CheckStockQuantityDTO checkStockQuantityDTO){
        return allocateApplyServiceImpl.checkStockQuantity(checkStockQuantityDTO);
    }


}
