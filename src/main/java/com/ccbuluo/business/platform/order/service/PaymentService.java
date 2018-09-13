package com.ccbuluo.business.platform.order.service;


import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付功能
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-22 16:33:00
 */
public interface PaymentService {

    /**
     *  支付完成调用接口
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    StatusDto paymentCompletion(String applyNo);
    /**
     *  根据申请单获取总价格
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-29 15:02:58
     */
    FindAllocateApplyDTO getTotalPrice(String applyNo);

    /**
     *  服务单（维修单）支付功能
     * @param serviceOrderno 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-10 17:02:58
     */
    StatusDto servicepaymentcompletion(String serviceOrderno);

    /**
     *  根据维修单获取总价格
     * @param serviceOrderno 维修单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-11 14:02:58
     */
    BizServiceOrder getServiceOrdernoPrice(String serviceOrderno);

    /**
     *  创建预付款单据（采购）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    StatusDto saveCustomerServiceAdvanceCounter(String applyNo);

    /**
     *  创建退货款单据（问题件退货）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    StatusDto saveCustomerServiceRefundCounter(String applyNo);

    /**
     *  创建索赔单付款单据
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-12 20:02:58
     */
    StatusDto saveCustomerServiceMarketCounter(String claimOrdno);
}
