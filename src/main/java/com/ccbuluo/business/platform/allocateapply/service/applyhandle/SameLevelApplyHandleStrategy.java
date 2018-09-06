package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private UserHolder userHolder;

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
            List<BizAllocateTradeorder> list = buildOrderEntityList(details);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, relOrdstockOccupies);
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
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details){
        List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
        // 构建生成订单(买方到卖方)
        BizAllocateTradeorder purchaserToSeller = buildOrderEntity(details);
        // 特殊情况处理
        // 计算订单总价
        BigDecimal total = getSellTotal(details);
        // 调拨(从买方到卖方)
        purchaserToSeller.setTotalPrice(total);
        // 从买方到卖方
        if(null != purchaserToSeller){
            list.add(purchaserToSeller);
        }
        return list;
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
            bizAllocateApplyDao.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.CANCEL.name());
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
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, List<RelOrdstockOccupy> relOrdstockOccupies){
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        // 卖方机构出库计划
        outstockplanSeller(outList,relOrdstockOccupies,stockDetails,details);
        // 买方入库计划(也要根据出库计划来，并且还要根据供应商来分组合并商品编号)
        instockplanPlatformFromStockAndGroup(inList, outList,details);
        return Pair.of(outList, inList);
    }

    /**
     * 卖方机构出库（机构2）
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private void outstockplanSeller(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details){
        // 卖方机构出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanSeller = new BizOutstockplanDetail();
            for(BizStockDetail stockDetail : stockDetails){
                // 关系库存批次id和库存批次id相等
                if(ro.getStockId().intValue() == stockDetail.getId().intValue()){
                    Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> stockDetail.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                    if (applyFilter.isPresent()) {
                        AllocateapplyDetailBO applyDetail = applyFilter.get();
                        outstockplanSeller = buildBizOutstockplanDetail(applyDetail,stockDetail);
                        // 卖方机构编号
                        outstockplanSeller.setOutOrgno(applyDetail.getOutstockOrgno());
                    }
                    // 库存类型(在创建占用关系的时候赋值)
                    outstockplanSeller.setStockType(ro.getStockType());
                    // 计划出库数量applyNum
                    outstockplanSeller.setPlanOutstocknum(ro.getOccupyNum());
                    // 卖方仓库编号（根据机构和商品编号查询的库存）
                    outstockplanSeller.setOutRepositoryNo(stockDetail.getRepositoryNo());
                    // 库存编号id
                    outstockplanSeller.setStockId(stockDetail.getId());
                    // 成本价
                    outstockplanSeller.setCostPrice(stockDetail.getCostPrice());
                    // 交易类型
                    outstockplanSeller.setOutstockType(OutstockTypeEnum.TRANSFER.toString());
                    outList.add(outstockplanSeller);
                    continue;
                }
            }
        }
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
    /**
     * 买方入库机构构建
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-21 17:47:08
     */
    private void instockplanPlatformFromStockAndGroup(List<BizInstockplanDetail> inList, List<BizOutstockplanDetail> outList, List<AllocateapplyDetailBO> details){
        AllocateapplyDetailBO ad = null;
        if(null != details && details.size() > 0){
            ad = details.get(0);
        }
        // 有可能一个商品有多个供应商（根据商品和供应商分组合并）
        distinstSupplier(inList, outList,details);
    }

    /**
     *   过滤供应商(有可能一个商品有多个供应商（根据商品和供应商分组合并）)
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-21 18:05:46
     */
    private void distinstSupplier(List<BizInstockplanDetail> inList,List<BizOutstockplanDetail> outList,List<AllocateapplyDetailBO> details){
        Optional<BizOutstockplanDetail> outFilter = outList.stream() .filter(outstockplan -> null == outstockplan.getSupplierNo()) .findFirst();
        if (outFilter.isPresent()) {
            outFilter.get().setSupplierNo("");
        }

        List<BizOutstockplanDetail> list = new ArrayList<BizOutstockplanDetail>();
        Map<String, List<BizOutstockplanDetail>> collect = outList.stream().collect(Collectors.groupingBy(BizOutstockplanDetail::getProductNo));
        for (Map.Entry<String, List<BizOutstockplanDetail>> entryP : collect.entrySet()) {
            List<BizOutstockplanDetail> valueP = entryP.getValue();
            BizInstockplanDetail inPlan = null;
            // 根据供应商分组
            Map<String, List<BizOutstockplanDetail>> collect1 = valueP.stream().collect(Collectors.groupingBy(BizOutstockplanDetail::getSupplierNo));
            int i = 0;
            for (Map.Entry<String, List<BizOutstockplanDetail>> entryS : collect1.entrySet()) {
                List<BizOutstockplanDetail> valueS = entryS.getValue();
                if(null != valueS && valueS.size() > 0){
                    if(i == 0){
                        inPlan = buildBizInstockplanDetail(valueS.get(0));
                        BizInstockplanDetail finalInPlan = inPlan;
                        Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> finalInPlan.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                        if (applyFilter.isPresent()) {
                            AllocateapplyDetailBO ad = applyFilter.get();
                            // 入库仓库编号
                            inPlan.setInstockRepositoryNo(ad.getInRepositoryNo());
                            // 买入机构编号
                            inPlan.setInstockOrgno(ad.getInstockOrgno());
                            // 买方入库的成本价等于单子上的销售价
                            inPlan.setCostPrice(ad.getSellPrice());
                        }
                        i++;
                    }
                }
                Long planOutstocknum = 0L;
                for(BizOutstockplanDetail bd : valueS){
                    // 计划出库数量
                    planOutstocknum += bd.getPlanOutstocknum();
                }
                inPlan.setPlanInstocknum(planOutstocknum);
                inList.add(inPlan);
            }
        }
    }
    /**
     *  构建平台调拨的入库计划
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-21 17:51:45
     */
    private BizInstockplanDetail buildBizInstockplanDetail(BizOutstockplanDetail bd){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        inPlan.setProductNo(bd.getProductNo());
        inPlan.setProductType(bd.getProductType());
        inPlan.setProductCategoryname(bd.getProductCategoryname());
        inPlan.setProductName(bd.getProductName());
        inPlan.setProductUnit(bd.getProductUnit());
        inPlan.setTradeNo(bd.getTradeNo());
        inPlan.setSupplierNo(bd.getSupplierNo());
        inPlan.setCostPrice(bd.getCostPrice());
        inPlan.setPlanInstocknum(bd.getPlanOutstocknum());
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());
        inPlan.preInsert(userHolder.getLoggedUserId());
        inPlan.setStockType(bd.getStockType());
        inPlan.setRemark(bd.getRemark());
        inPlan.setInstockType(InstockTypeEnum.TRANSFER.toString());
        return inPlan;
    }
}
