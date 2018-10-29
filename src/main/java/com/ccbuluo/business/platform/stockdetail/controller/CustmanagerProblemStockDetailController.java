package com.ccbuluo.business.platform.stockdetail.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleContext;
import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.service.ProblemStockDetailService;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  问题库存controller
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-15 08:01:51
 */
@Api(tags = "问题库存(客户经理端)")
@RestController
@RequestMapping("/platform/custmanagerproblemstock")
public class CustmanagerProblemStockDetailController extends BaseController {
    @Resource
    private ProblemStockDetailService problemStockDetailService;
    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsProductService carpartsProductService;
    @Resource
    ApplyHandleContext applyHandleContext;

    /**
     * 本机构物料问题库存列表（服务中心端用）
     * @param productCategory 物料分类
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "当前登录机构物料问题库存列表（客户经理端）",notes = "【魏俊标】")
    @GetMapping("/selfequipmentlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "productCategory", value = "物料类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> querySelfequipmentStockList(@RequestParam(required = false) String productCategory,
                                                                                   @RequestParam(required = false) String keyword,
                                                                                   @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                   @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        boolean category = false;
        if(StringUtils.isNotBlank(productCategory)){
            category = true;
        }
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.querySelfStockBizStockDetailDTOList(category, Constants.PRODUCT_TYPE_EQUIPMENT,productCategory, null, keyword, offset, pageSize));
    }

    /**
     * 本机构零配件问题库存列表（服务中心端用）
     * @param productCategory 零配件分类
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "当前登录机构零配件问题库存列表（客户经理端）",notes = "【魏俊标】")
    @GetMapping("/selffittingslist")
    @ApiImplicitParams({@ApiImplicitParam(name = "productCategory", value = "零配件分类code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> querySelffittingsStockList(@RequestParam(required = false) String productCategory,
                                                                              @RequestParam(required = false) String keyword,
                                                                              @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                              @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        List<BasicCarpartsProductDTO> productList = null;
        boolean category = false;
        if(StringUtils.isNotBlank(productCategory)){
            category = true;
            productList = carpartsProductService.queryCarpartsProductListByCategoryCode(productCategory);
        }
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.querySelfStockBizStockDetailDTOList(category, Constants.PRODUCT_TYPE_FITTINGS,null, productList, keyword, offset, pageSize));
    }

    /**
     * 查询问题库存详情
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    @ApiOperation(value = "查询问题库存详情", notes = "【魏俊标】")
    @GetMapping("/problemdetail/{id}")
    @ApiImplicitParam(name = "id", value = "库存批次id", required = true, paramType = "path")
    public StatusDto<ProblemStockBizStockDetailDTO> getProblemStockDetail(@PathVariable @ValidateNotNull(message = "id(库存批次id)不能为空") Long id){
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.getProblemStockDetail(id));
    }

}
