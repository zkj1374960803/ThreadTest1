package com.ccbuluo.business.platform.outstockplan.service;


import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.outstock.dto.updatePlanStatusDTO;

import java.util.List;

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
    /**
     * 根据申请单号和出库仓库查询出库计划
     * @param applyNo 申请单号
     * @param outRepositoryNo 出库仓库编号
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-09 14:38:57
     */
    List<BizOutstockplanDetail> queryOutstockplan(String applyNo, String outRepositoryNo);

    /**
     * 根据出库计划id查询版本号（乐观锁）
     * @param ids 出库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-10 16:43:38
     */
    List<updatePlanStatusDTO> getVersionNoById(List<Long> ids);


    /**
     * 更改出库计划的实际出库数量
     * @param bizOutstockplanDetails 出库计划
     * @author liuduo
     * @date 2018-08-10 16:48:48
     */
    void updateActualOutstocknum(List<BizOutstockplanDetail> bizOutstockplanDetails);

    /**
     * 更新出库计划中的状态和完成时间
     * @param bizOutstockplanDetails 出库计划
     * @author liuduo
     * @date 2018-08-10 17:46:26
     */
    void updatePlanStatus(List<BizOutstockplanDetail> bizOutstockplanDetails);

    /**
     * 根据申请单号查询出库计划
     * @param applyNo 申请单号
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    List<BizOutstockplanDetail> queryOutstockplanList(String applyNo);
}
