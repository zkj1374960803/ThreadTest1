package com.ccbuluo.business.platform.order.controller;

import com.ccbuluo.business.entity.BizAllocateTradeorder;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.order.service.PaymentService;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
     *  申请单支付功能（采购）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @ApiOperation(value = "申请单支付功能（采购）", notes = "【魏俊标】")
    @GetMapping("/purchasepayment/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> purchasePayment(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
        return paymentService.purchasePayment(applyNo);
    }

    /**
     *  申请单支付功能（调拨）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @ApiOperation(value = "申请单支付功能（调拨）", notes = "【魏俊标】")
    @GetMapping("/samelevelpayment/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> samelevelPayment(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
        return paymentService.samelevelPayment(applyNo);
    }

    /**
     *  申请单支付功能（退货）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @ApiOperation(value = "申请单支付功能（退货）", notes = "【魏俊标】")
    @PostMapping("/refundpayment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "actualAmount", value = "实际退货的金额", required = true, paramType = "query")
    })
    public StatusDto<FindAllocateApplyDTO> refundPayment(@ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo, BigDecimal actualAmount){
        return paymentService.refundPayment(applyNo,actualAmount);
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
    public StatusDto<FindAllocateApplyDTO> getTotalPrice(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
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
    public StatusDto<String> servicepaymentcompletion(@PathVariable @ValidateNotNull(message = "claimOrdno(服务单claimOrdno)不能为空") String serviceOrderno){
        return paymentService.servicepaymentcompletion(serviceOrderno);
    }
    /**
     *  根据维修单获取总价格
     * @param serviceOrderno 维修单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-11 14:02:58
     */
    @ApiOperation(value = "根据服务单（维修单）获取总价格", notes = "【魏俊标】")
    @GetMapping("/getserviceordernoprice/{serviceOrderno}")
    @ApiImplicitParam(name = "serviceOrderno", value = "申请单号", required = true, paramType = "path")
    public StatusDto<BizServiceOrder> getServiceOrdernoPrice(@PathVariable @ValidateNotNull(message = "claimOrdno(服务单claimOrdno)不能为空") String serviceOrderno){
        BizServiceOrder serviceOrder = paymentService.getServiceOrdernoPrice(serviceOrderno);
        return StatusDto.buildDataSuccessStatusDto(serviceOrder);
    }

    /**
     *  创建预付款单据（采购）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    @ApiOperation(value = "创建预付款单据（平台采购）", notes = "【魏俊标】")
    @GetMapping("/savecustomerserviceadvancecounter/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<String> saveCustomerServiceAdvanceCounter(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
        return paymentService.saveCustomerServiceAdvanceCounter(applyNo);
    }

    /**
     *  创建退货款单据（问题件退货）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    @ApiOperation(value = "创建退货款单据（问题件退货）", notes = "【魏俊标】")
    @GetMapping("/savecustomerservicerefundcounter/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<String> saveCustomerServiceRefundCounter(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
        return paymentService.saveCustomerServiceRefundCounter(applyNo);
    }

    /**
     *  创建索赔单付款单据
     * @param claimOrdno 索赔单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    @ApiOperation(value = "创建索赔单付款单据", notes = "【魏俊标】")
    @GetMapping("/savecustomerservicemarketcounter/{serviceOrderno}")
    @ApiImplicitParam(name = "claimOrdno", value = "申请单号", required = true, paramType = "path")
    public StatusDto<String> saveCustomerServiceMarketCounter(@PathVariable @ValidateNotNull(message = "claimOrdno(索赔单claimOrdno)不能为空") String claimOrdno){
        return paymentService.saveCustomerServiceMarketCounter(claimOrdno);
    }
}
