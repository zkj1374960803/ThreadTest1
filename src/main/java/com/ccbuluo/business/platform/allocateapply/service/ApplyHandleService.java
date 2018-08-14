package com.ccbuluo.business.platform.allocateapply.service;

/**
 * 申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 19:26:33
 */
public interface ApplyHandleService {
    /**
     *  申请处理
     * @param applyNo 申请单编号
     * @param applyType 申请类型
     * @date 2018-08-08 10:55:41
     */
    public int applyHandle(String applyNo, String applyType);

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public int cancelApply(String applyNo);
}
