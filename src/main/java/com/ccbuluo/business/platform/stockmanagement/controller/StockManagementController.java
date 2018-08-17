package com.ccbuluo.business.platform.stockmanagement.controller;

import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterService;
import com.ccbuluo.business.platform.stockmanagement.service.StockManagementService;
import com.ccbuluo.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "库存管理列表查询", notes = "【张康健】")
    @GetMapping("/list")
    public void list(){


    }

}
