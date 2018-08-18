package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.ccbuluo.business.entity.BizAllocateApply;

/**
 * 申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 19:26:33
 */
public interface ApplyHandleStrategy {
    /**
     *  申请处理
     * @param ba 申请单
     * @date 2018-08-08 10:55:41
     */
    int applyHandle(BizAllocateApply ba);

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    int cancelApply(String applyNo);
}
