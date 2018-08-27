package com.ccbuluo.business.platform.inputstockplan.service;


import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.outstock.dto.UpdatePlanStatusDTO;

import java.util.List;

/**
 * 入库计划
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
public interface InputStockPlanService {

    /**
     * 根据申请单编号查询入库计划
     * @param applyNo 申请单编号
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-08 11:14:56
     */
    List<BizInstockplanDetail> queryListByApplyNo(String applyNo, String status, String inRepositoryNo);

    /**
     * 根据入库计划id查询版本号
     * @param ids 入库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    List<UpdatePlanStatusDTO> getVersionNoById(List<Long> ids);

    /**
     * 更新入库计划中的实际入库数量
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-08 20:17:42
     */
    void updateActualInstockNum(List<BizInstockplanDetail> bizInstockplanDetailList);

    /**
     * 修改入库计划的完成状态
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    void updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetailList);

    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    List<BizInstockplanDetail> queryInstockplan(String applyNo,  String inRepositoryNo, String productType);
    /**
     *  更入出库计划状态
     * @param applyNo 申请单编号
     * @param completeStatus 状态
     * @param instockRepositoryNo 入库仓库编号
     * @author weijb
     * @date 2018-08-11 12:55:41
     */
    int updateInStockPlanStatus(String applyNo, String completeStatus, String instockRepositoryNo);

    /**
     * 根据申请单号查询入库仓库
     * @param applyNo 入库单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    List<String> getByApplyNo(String applyNo, String orgCode);

    /**
     * 根据申请单号和入库仓库查询入库计划
     * @param applyNo 申请单号
     * @param inRepositoryNo 入库仓库
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    List<BizInstockplanDetail> queryListByApplyNoAndInReNo(String applyNo, String inRepositoryNo);
}
