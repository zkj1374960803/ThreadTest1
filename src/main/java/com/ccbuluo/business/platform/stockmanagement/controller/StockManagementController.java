package com.ccbuluo.business.platform.stockmanagement.controller;

import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterService;
import com.ccbuluo.business.platform.stockmanagement.service.StockManagementService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @author zhangkangjian
 * @date 2018-08-16 10:23:20
 */
@Api(tags = "库存管理")
@RestController
@RequestMapping("/platform/stockmanagement")
public class StockManagementController extends BaseController {
    @Autowired
    private StockManagementService stockManagementService;
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;

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

}
