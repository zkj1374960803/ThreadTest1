package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import org.springframework.stereotype.Service;

/**
 * 退换申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class BarterApplyHandleStrategy extends DefaultApplyHandleStrategy {
    /**
     *  退换申请处理
     * @param applyNo 申请单编号
     * @applyType 申请类型
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Override
    public int applyHandle(String applyNo, String applyType){
        return 0;
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Override
    public int cancelApply(String applyNo){
        return 0;
    }
}
