package com.ccbuluo.business.platform.claimorder.controller;

import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 索赔单
 * @author zhangkangjian
 * @date 2018-09-08 10:13:49
 */
@Api(tags = "索赔单(平台端)")
@RestController
@RequestMapping("/platform/claimorder")
public class ClaimOrderController{
    @Resource(name = "claimOrderServiceImpl")
    private ClaimOrderService claimOrderServiceImpl;

    /**
     * 查询索赔单的详情
     * @param bizServiceClaimorder 查询条件
     * @return StatusDto<BizServiceClaimorder>
     * @author zhangkangjian
     * @date 2018-09-08 14:35:15
     */
    @ApiOperation(value = "查询索赔单的详情", notes = "【张康健】")
    @GetMapping("/findclaimorderdetail")
    @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = true, paramType = "query")
    public StatusDto<BizServiceClaimorder> findClaimOrderDetail(@ApiIgnore BizServiceClaimorder bizServiceClaimorder){
        BizServiceClaimorder claimOrderDetail = claimOrderServiceImpl.findClaimOrderDetail(bizServiceClaimorder);
        return StatusDto.buildDataSuccessStatusDto(claimOrderDetail);
    }

    /**
     * 查询索赔单的列表
     * @param claimOrdno 索赔单号
     * @param docStatus 索赔单状态
     * @param offset 索赔单状态
     * @param pageSize 每页显示的数量
     * @return 索赔单的列表
     * @author zhangkangjian
     * @date 2018-09-08 16:12:24
     */
    @ApiOperation(value = "查询索赔单的列表", notes = "【张康健】")
    @GetMapping("/queryclaimorderlist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "docStatus", value = "索赔单状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryClaimorderListDTO>> queryClaimorderList(String claimOrdno, String docStatus, int offset, int pageSize){
        return claimOrderServiceImpl.queryClaimorderList(claimOrdno, docStatus, offset, pageSize);
    }

    /**
     * 验收索赔单
     * @param claimOrdno 索赔单号
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-09-10 10:14:11
     */
    @ApiOperation(value = "验收索赔单", notes = "【张康健】")
    @GetMapping("/acceptanceclaimsheet")
    @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = true, paramType = "query")
    public StatusDto<String> acceptanceClaimSheet(String claimOrdno){
        claimOrderServiceImpl.updateDocStatusAndProcessTime(claimOrdno, BizServiceClaimorder.DocStatusEnum.PENDINGPAYMENT.name());
        return StatusDto.buildSuccessStatusDto();
    }
    
    /**
     * 索赔单付款
     * @param claimOrdno 索赔单号
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-09-10 10:38:07
     */
    @ApiOperation(value = "索赔单付款", notes = "【张康健】")
    @GetMapping("/billofpayment")
    @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = true, paramType = "query")
    public StatusDto<String> billOfPayment(String claimOrdno){
        return claimOrderServiceImpl.updateDocStatusAndRepayTime(claimOrdno, BizServiceClaimorder.DocStatusEnum.COMPLETED.name());
    }


    /**
     * 查询支付价格
     * @param serviceOrdno 维修单号
     * @return  Map<String, Double>
     * @author zhangkangjian
     * @date 2018-09-12 14:02:21
     */
    @ApiOperation(value = "查询支付索赔单金额", notes = "【张康健】")
    @GetMapping("/findpaymentamount")
    @ApiImplicitParam(name = "serviceOrdno", value = "维修单号", required = true, paramType = "query")
    public StatusDto<Map<String, Double>> findPaymentAmount(String serviceOrdno){
        Map<String, Double> paymentAmount = claimOrderServiceImpl.findPaymentAmount(serviceOrdno);
        return StatusDto.buildDataSuccessStatusDto(paymentAmount);
    }


}