package com.ccbuluo.business.platform.stockdetail.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 *  问题库存controller
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-15 08:01:51
 */
@Api(tags = "问题库存(服务中心端)")
@RestController
@RequestMapping("/platform/serviceproblemstock")
public class ServiceProblemStockDetailController extends BaseController {
    @Resource
    private ProblemStockDetailService problemStockDetailService;
    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsProductService carpartsProductService;
    @Resource
    ApplyHandleContext applyHandleContext;

    /**
     * 查看问题件物料调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看问题件物料库存(服务中心端)", notes = "【张康健】")
    @GetMapping("/selfequipmentlist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "equiptypeId", value = "物料类型的id", required = false, paramType = "query"),
        @ApiImplicitParam(name = "productNo", value = "商品的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "orgNo", value = "服务中心的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> queryequipmentStockList(@ValidateNotNull @ApiIgnore FindStockListDTO findStockListDTO) {
        findStockListDTO.setProductType(Constants.PRODUCT_TYPE_EQUIPMENT);
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.queryStockBizStockDetailDTOList(findStockListDTO));
    }

    /**
     * 查看问题件零配件调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @ApiOperation(value = "查看问题件零配件库存(服务中心端)", notes = "【张康健】")
    @GetMapping("/selffittingslist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "零配件代码/名称/名称首字母大写", required = false, paramType = "query"),
        @ApiImplicitParam(name = "orgNo", value = "服务中心的编号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<FindStockListDTO>> queryfittingsStockList(@ValidateNotNull @ApiIgnore FindStockListDTO findStockListDTO) {
        findStockListDTO.setProductType(Constants.PRODUCT_TYPE_FITTINGS);
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.queryStockBizStockDetailDTOList(findStockListDTO));
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
