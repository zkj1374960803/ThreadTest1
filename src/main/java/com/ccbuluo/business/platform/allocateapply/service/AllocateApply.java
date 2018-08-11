package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProcessApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.db.Page;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:29:38
 */
public interface AllocateApply {
    /**
     * 创建物料或者零配件申请
     * @param bizAllocateApply 申请单实体
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    void createAllocateApply(BizAllocateApply bizAllocateApply);
    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    FindAllocateApplyDTO findDetail(String applyNo);
    /**
     * 查询申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    Page<QueryAllocateApplyListDTO> findApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
    /**
     * 查询处理申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    Page<QueryAllocateApplyListDTO> findProcessApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
    /**
     * 处理申请单
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    void processApply(ProcessApplyDTO processApplyDTO);
    // todo
    Page<FindStockListDTO> findStockList(FindStockListDTO findStockListDTO);
}
