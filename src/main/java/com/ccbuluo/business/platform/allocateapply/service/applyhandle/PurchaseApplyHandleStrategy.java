package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class PurchaseApplyHandleStrategy extends DefaultApplyHandleStrategy {

    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private UserHolder userHolder;
    @Resource
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    private BizStockDetailDao bizStockDetailDao;

    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     *  采购申请处理
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
            // 构建生成订单（采购）
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, applyType);

            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, null, BizAllocateApply.AllocateApplyTypeEnum.PURCHASE, null);
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            flag =1;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return flag;
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public int cancelApply(String applyNo){
        return 0;
    }

    /**
     *  入库之后回调事件
     * @param ba 申请单
     * @return
     */
    public StatusDto instockAfterCallBack(BizAllocateApply ba){
        String applyNo = ba.getApplyNo();
        String applyType = ba.getApplyType();
        // 根据申请单获取申请单详情
        List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
        if(null == details || details.size() == 0){
            return StatusDto.buildSuccessStatusDto();
        }
        //获取卖方机构code
        String productOrgNo = getProductOrgNo(ba);
        //查询库存列表
        List<BizStockDetail> stockDetails = getStockDetailList(productOrgNo, details);
        if(null == stockDetails || stockDetails.size() == 0){
            return StatusDto.buildSuccessStatusDto();
        }
        // 构建占用库存和订单占用库存关系
        Pair<List<BizStockDetail>, List<RelOrdstockOccupy>> pair = buildStockAndRelOrdEntity(details,stockDetails,applyType);
        List<BizStockDetail> stockDetailList = pair.getLeft();
        // 构建订单占用库存关系
        List<RelOrdstockOccupy> relOrdstockOccupies = pair.getRight();
        // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
        Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.PLATFORMALLOCATE, relOrdstockOccupies);

        // 保存占用库存
        int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
        if(flag == 0){// 更新失败
            throw new CommonException("0", "更新占用库存失败！");
        }
        // 保存订单占用库存关系
        bizAllocateTradeorderDao.batchInsertRelOrdstockOccupy(relOrdstockOccupies);
        // 批量保存出库计划详情
        bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());
        return null;
    }

}
