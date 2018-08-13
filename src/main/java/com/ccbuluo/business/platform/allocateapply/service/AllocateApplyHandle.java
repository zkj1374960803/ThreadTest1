package com.ccbuluo.business.platform.allocateapply.service;

/**
 * 申请处理调用相关处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 17:48:56
 */
public interface AllocateApplyHandle {
    /**
     *  申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    int purchaseApplyHandle(String applyNo);

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    int cancelApply(String applyNo);
}
