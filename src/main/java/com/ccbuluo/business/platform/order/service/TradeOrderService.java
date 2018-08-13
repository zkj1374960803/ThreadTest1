package com.ccbuluo.business.platform.order.service;


/**
 * 调拨申请交易订单
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
public interface TradeOrderService {
    /**
     *  采购申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    int purchaseApplyHandle(String applyNo);

    /**
     *  调拨申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    int allocateApplyHandle(String applyNo);

    /**
     *  撤销申请
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    int cancelApply(String applyNo);
}
