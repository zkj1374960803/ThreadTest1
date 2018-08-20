package com.ccbuluo.business.platform.allocateapply.service;


import com.ccbuluo.business.platform.allocateapply.dto.ProblemAllocateapplyDetailDTO;
import com.ccbuluo.db.Page;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
public interface ProblemAllocateApply {

    /**
     * 问题件申请列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<ProblemAllocateapplyDetailDTO> queryProblemApplyList(String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
    /**
     * 问题件处理列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    Page<ProblemAllocateapplyDetailDTO> queryProblemHandleList(String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
}
