package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 平级调拨申请处理（服务间的调拨）
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class SameLevelApplyHandleStrategy extends DefaultApplyHandleStrategy {

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
    /*@Resource
    BizAllocateApplyDao bizAllocateApplyDao;*/

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  调拨申请处理
     * @param ba 申请单
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto applyHandle(BizAllocateApply ba){
        String applyNo = ba.getApplyNo();
        String applyType = ba.getApplyType();
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                throw new CommonException("0", "申请单为空！");
            }
            // 构建占用库存和订单占用库存关系
            // 获取卖方机构code
            String productOrgNo = getProductOrgNo(ba);
            // 查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(productOrgNo, details);
            if(null == stockDetails || stockDetails.size() == 0){
                throw new CommonException("0", "库存为空！");
            }
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
            // 构建占用库存和订单占用库存关系
            List<BizStockDetail> stockDetailList = buildStockAndRelOrdEntity(details,stockDetails,applyType,relOrdstockOccupies);
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, applyType,null,null);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL, relOrdstockOccupies);
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());
            // 批量保存入库计划详情
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());

            String stockType = getStockType(details);
            // 只有正常件才保存库存和占用关系
            if(BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(stockType)){
                // 保存占用库存
                int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
                if(flag == 0){// 更新失败
                    throw new CommonException("0", "更新占用库存失败！");
                }
                // 保存订单占用库存关系
                bizAllocateTradeorderDao.batchInsertRelOrdstockOccupy(relOrdstockOccupies);
            }
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请处理成功！");
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Override
    public StatusDto cancelApply(String applyNo){
        try {
            // 根据申请单编号查询订单占用库存关系表
            List<RelOrdstockOccupy> list = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(applyNo);
            if(null != list && list.size() > 0){
                //根据订单占用库存关系构建库存list
                List<BizStockDetail> stockDetails = buildBizStockDetail(list);
                // 还原被占用的库存
                int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetails);
                if(flag == 0){// 更新失败
                    throw new CommonException("0", "还原占用库存失败！");
                }
            }
            //删除订单占用关系
            bizAllocateTradeorderDao.deleteRelOrdstockOccupyByApplyNo(applyNo);
            // 删除订单
            bizAllocateTradeorderDao.deleteAllocateTradeorderByApplyNo(applyNo);
            // 删除出库计划
            bizOutstockplanDetailDao.deleteOutstockplanDetailByApplyNo(applyNo);
            // 删除入库计划
            bizInstockplanDetailDao.batchInsertInstockplanDetail(applyNo);
            //更新申请单状态(已撤销)
            //bizAllocateApplyDao.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.CANCEL.name());
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请撤销成功！");
    }

    /**
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @param applyTypeEnum 申请类型枚举
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Override
    public Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, BizAllocateApply.AllocateApplyTypeEnum applyTypeEnum, List<RelOrdstockOccupy> relOrdstockOccupies){
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        // 卖方机构出库计划
        outstockplanSeller(outList,relOrdstockOccupies,stockDetails,details, BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.toString());
        // 买方入库计划
        instockplanPurchaser(inList,details, BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.toString());
        return Pair.of(outList, inList);
    }
    /**
     *  入库之后回调事件
     * @param ba 申请单
     * @author weijb
     * @param ba 申请单
     * @return
     */
    @Override
    public StatusDto platformInstockCallback(BizAllocateApply ba){
        // 服务间的调拨没有回调
        return null;
    }
}
