package com.ccbuluo.business.platform.order.controller;

import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.order.service.PaymentService;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 *  支付功能
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-22 16:33:00
 */
@Api(tags = "支付功能")
@RestController
@RequestMapping("/platform/payment")
public class PaymentController {
    @Resource
    PaymentService paymentService;

    /**
     *  申请单支付功能
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @ApiOperation(value = "申请单支付功能", notes = "【魏俊标】")
    @GetMapping("/paymentcompletion/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> paymentCompletion(@PathVariable String applyNo){
        return paymentService.paymentCompletion(applyNo);
    }

    /**
     *  根据申请单获取总价格
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-29 15:02:58
     */
    @ApiOperation(value = "根据申请单获取总价格", notes = "【魏俊标】")
    @GetMapping("/gettotalprice/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> getTotalPrice(@PathVariable String applyNo){
        FindAllocateApplyDTO allocate = paymentService.getTotalPrice(applyNo);
        return StatusDto.buildDataSuccessStatusDto(allocate);
    }

    /**
     *  服务单（维修单）支付功能
     * @param serviceOrderno 服务单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @ApiOperation(value = "服务单（维修单）支付功能", notes = "【魏俊标】")
    @GetMapping("/servicepaymentcompletion/{serviceOrderno}")
    @ApiImplicitParam(name = "serviceOrderno", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> servicepaymentcompletion(@PathVariable String serviceOrderno){
        return paymentService.servicepaymentcompletion(serviceOrderno);
    }
}
