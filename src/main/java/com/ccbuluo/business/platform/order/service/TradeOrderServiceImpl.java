package com.ccbuluo.business.platform.order.service;


import com.ccbuluo.business.entity.BizAllocateTradeorder;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 调拨申请交易订单
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
public class TradeOrderServiceImpl implements TradeOrderService {
    /**
     *  采购申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    public int purchaseApplyHandle(String applyNo){
        // 根据申请单获取申请单详情
        List<BizAllocateapplyDetail> details = new ArrayList<BizAllocateapplyDetail>();//bizAllocateapplyDetailDao.getXXXXByapplyNo(applyNo);
        // 构建生成订单
        List<BizAllocateTradeorder> allocateTradeorderList = buildOrderEntity(details);
        // 构建占用库存
        List<BizStockDetail> stockDetailList = buildStockEntity(details);
        // 保存生成订单
        // 保存占用库存
        return 0;
    }

    /**
     *  调拨申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    public int allocateApplyHandle(String applyNo){
        return 0;
    }

    // 构建生成订单
    private List<BizAllocateTradeorder> buildOrderEntity(List<BizAllocateapplyDetail> details){
        return null;
    }

    // 构建占用库存
    private List<BizStockDetail> buildStockEntity(List<BizAllocateapplyDetail> details){
        return null;
    }

}
