package com.ccbuluo.business.platform.instock.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.platform.instock.dto.SaveBizInstockOrderDTO;
import com.ccbuluo.business.platform.instock.service.InstockOrderService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库单controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:03:59
 */
@Api(tags = "入库单")
@RestController
@RequestMapping("/platform/instockorder")
public class InstockOrderController extends BaseController {

    @Autowired
    private InstockOrderService instockOrderService;

    /**
     * 根据申请单号状态查询申请单号集合
     * @param applyStatus 申请单状态
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @ApiOperation(value = "申请单号查询", notes = "【刘铎】")
    @ApiImplicitParam(name = "applyStatus", value = "申请单号状态",  required = true, paramType = "query")
    @GetMapping("/queryapplyno")
    public StatusDto<List<String>> queryApplyNo(@RequestParam String applyStatus) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.queryApplyNo(applyStatus));
    }


    /**
     * 保存入库单
     * @param applyNo 申请单号
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @ApiOperation(value = "入库单新增", notes = "【刘铎】")
    @ApiImplicitParam(name = "applyNo", value = "申请单号",  required = true, paramType = "query")
    @PostMapping("/save")
    public StatusDto<String> saveInstockOrder(@RequestParam String applyNo, @RequestBody List<BizInstockorderDetail> bizInstockorderDetailList) {
        return instockOrderService.saveInstockOrder(applyNo, bizInstockorderDetailList);
    }
}
