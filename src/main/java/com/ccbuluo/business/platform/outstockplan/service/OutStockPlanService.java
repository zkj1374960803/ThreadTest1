package com.ccbuluo.business.platform.outstockplan.service;


/**
 * 出库计划
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
public interface OutStockPlanService {

    /**
     *  更改出库计划状态
     * @param applyNo 申请单编号
     * @param planStatus 状态
     * @param outRepositoryNo 出库仓库编号
     * @author weijb
     * @date 2018-08-11 12:55:41
     */
    int updateOutStockPlanStatus(String applyNo, String planStatus, String outRepositoryNo);
}
