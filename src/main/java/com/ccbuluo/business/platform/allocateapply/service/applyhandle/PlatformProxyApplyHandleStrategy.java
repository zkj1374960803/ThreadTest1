package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 平台调拨申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class PlatformProxyApplyHandleStrategy extends DefaultApplyHandleStrategy {

    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private BizStockDetailDao bizStockDetailDao;
    @Resource
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  平台调拨申请处理
     * @param ba 申请单
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Override
    public int applyHandle(BizAllocateApply ba){
        int flag = 0;
        String applyNo = ba.getApplyNo();
        String applyType = ba.getApplyType();
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, applyNo);
            // 构建占用库存和订单占用库存关系
            //获取卖方机构code
            String productOrgNo = getProductOrgNo(ba);
            //查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(productOrgNo, details);
            if(null == stockDetails || stockDetails.size() == 0){
                return 0;
            }
            // 构建占用库存和订单占用库存关系
            Pair<List<BizStockDetail>, List<RelOrdstockOccupy>> pair = buildStockAndRelOrdEntity(details,stockDetails,applyType);
            List<BizStockDetail> stockDetailList = pair.getLeft();
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = pair.getRight();
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, applyType, relOrdstockOccupies);
            // 保存占用库存
            flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
            if(flag == 0){// 更新失败
                throw new CommonException("0", "更新占用库存失败！");
            }
            // 对出库详情处理，因为出库计划要记录到具体的库存批次id对应的出库数量
            List<BizOutstockplanDetail> outstockplanDetails = pir.getLeft();
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(outstockplanDetails);
            // 批量保存入库计划详情
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());
            // 保存订单占用库存关系
            bizAllocateTradeorderDao.batchInsertRelOrdstockOccupy(relOrdstockOccupies);
            flag =1;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return flag;
    }
    /**
     * 获取成本价
     * @param stockDetails
     * @param stockId 库存批次id
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getCostPrice(List<BizStockDetail> stockDetails, Long stockId){
        BigDecimal costPrice = new BigDecimal("0");
        for(BizStockDetail bd : stockDetails){
            if(bd.getId().intValue() == stockId.intValue()){
                costPrice = bd.getCostPrice();
                break;
            }
        }
        return costPrice;
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int cancelApply(String applyNo){
        int flag = 0;
        try {
            // 根据申请单编号查询订单占用库存关系表
            List<RelOrdstockOccupy> list = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(applyNo);
            //根据订单占用库存关系构建库存list
            List<BizStockDetail> stockDetails = buildBizStockDetail(list);
            // 还原被占用的库存
            flag = bizStockDetailDao.batchUpdateStockDetil(stockDetails);
            if(flag == 0){// 更新失败
                throw new CommonException("0", "更新占用库存失败！");
            }
            //删除订单占用关系
            bizAllocateTradeorderDao.deleteRelOrdstockOccupyByApplyNo(applyNo);
            // 删除订单
            bizAllocateTradeorderDao.deleteAllocateTradeorderByApplyNo(applyNo);
            // 删除出库计划
            bizOutstockplanDetailDao.deleteOutstockplanDetailByApplyNo(applyNo);
            // 删除入库计划
            bizInstockplanDetailDao.batchInsertInstockplanDetail(applyNo);
            flag =1;
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
        return flag;
    }
}
