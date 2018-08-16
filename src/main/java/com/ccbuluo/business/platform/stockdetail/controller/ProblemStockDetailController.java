package com.ccbuluo.business.platform.stockdetail.controller;

import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.service.ProblemStockDetailService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *  问题库存controller
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-15 08:01:51
 */
@Api(tags = "问题库存")
@RestController
@RequestMapping("/platform/problemstock")
public class ProblemStockDetailController extends BaseController {
    @Resource
    private ProblemStockDetailService problemStockDetailService;
    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsProductService carpartsProductService;


    /**
     * 商品问题库存列表
     * @param productType 商品类型
     * @param categoryCode 零配件分类
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "商品问题库存列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "productType", value = "物料类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "categoryCode", value = "零配件分类code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> queryStockBizStockDetailDTOList(@RequestParam(required = false) String productType,
                                                                                   @RequestParam(required = false) String categoryCode,
                                                                                   @RequestParam(required = false) String keyword,
                                                                                   @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                   @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        List<BasicCarpartsProductDTO> productList = null;
        if(StringUtils.isNotBlank(categoryCode)){
            productList = carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode);
        }
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.queryStockBizStockDetailDTOList(productType, productList, keyword, offset, pageSize));
    }

    /**
     * 本机构商品问题库存列表
     * @param productType 商品类型
     * @param categoryCode 零配件分类
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "当前登录机构商品问题库存列表",notes = "【魏俊标】")
    @GetMapping("/selflist")
    @ApiImplicitParams({@ApiImplicitParam(name = "productType", value = "物料类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "categoryCode", value = "零配件分类code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> querySelfStockBizStockDetailDTOList(@RequestParam(required = false) String productType,
                                                                                   @RequestParam(required = false) String categoryCode,
                                                                                   @RequestParam(required = false) String keyword,
                                                                                   @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                   @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        List<BasicCarpartsProductDTO> productList = null;
        if(StringUtils.isNotBlank(categoryCode)){
            productList = carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode);
        }
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.querySelfStockBizStockDetailDTOList(productType, productList, keyword, offset, pageSize));
    }

    /**
     * 根据商品code查询某个商品的问题件库存
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "根据商品code查询某个物料的问题件库存",notes = "【魏俊标】")
    @GetMapping("/getprodectstock")
    @ApiImplicitParams({@ApiImplicitParam(name = "productNo", value = "商品编号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> getProdectStockBizStockDetailByCode(@RequestParam(required = false) String productNo,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize){
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.getProdectStockBizStockDetailByCode(productNo, offset, pageSize));
    }

    /**
     * 根据商品code查询某个商品在当前登录机构的问题件库存
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @ApiOperation(value = "根据商品code查询某个商品在当前登录机构的问题件库存",notes = "【魏俊标】")
    @GetMapping("/getselfprodectstock")
    @ApiImplicitParams({@ApiImplicitParam(name = "productNo", value = "商品编号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> getSelfProdectStockBizStockDetailByCode(@RequestParam(required = false) String productNo,
                                                             @RequestParam(required = false) String keyword,
                                                             @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                             @RequestParam(required = false, defaultValue = "20") Integer pageSize){
        return StatusDto.buildDataSuccessStatusDto(problemStockDetailService.getSelfProdectStockBizStockDetailByCode(productNo, offset, pageSize));
    }
}
