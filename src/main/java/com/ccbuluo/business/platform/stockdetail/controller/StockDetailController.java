package com.ccbuluo.business.platform.stockdetail.controller;

import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  库存controller
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-20 09:26:03
 */
@Api(tags = "商品库存")
@RestController
@RequestMapping("/platform/productstock")
public class StockDetailController {

    @Resource
    StockDetailService stockDetailService;
    /**
     * 查询某个商品在当前登录机构的库存列表
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @exception
     * @author weijb
     * @date 2018-08-20 09:46:03
     */
    @ApiOperation(value = "根据商品code查询某个商品在当前登录机构的库存",notes = "【魏俊标】")
    @GetMapping("/getselfproductstock")
    @ApiImplicitParams({@ApiImplicitParam(name = "productNo", value = "商品编号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<StockBizStockDetailDTO>> getSelfProdectStockBizStockDetailByCode(@RequestParam(required = false) String productNo,
                                                                                           @RequestParam(required = false) String keyword,
                                                                                           @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize){
        return StatusDto.buildDataSuccessStatusDto(stockDetailService.getSelfStockBizStockDetailByCode(productNo, offset, pageSize));
    }
    /**
     *  查询库存详情
     * @param productNo 商品编号
     * @author weijb
     * @date 2018-08-20 10:12:33
     */
    @ApiOperation(value = "查询库存详情",notes = "【魏俊标】")
    @ApiImplicitParam(name = "productNo", value = "商品编号",  required = true, paramType = "query")
    @GetMapping("/getproductdetail")
    public StatusDto getProductDetail(@RequestParam String productNo) {
        return StatusDto.buildSuccessStatusDto(/*stockDetailService.getProductDetail(productNo)*/);
    }
}
