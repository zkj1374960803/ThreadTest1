package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.business.entity.BizAllocateApply.ApplyStatusEnum;
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
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    private UserHolder userHolder;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  平台调拨申请处理
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
                return StatusDto.buildFailureStatusDto("申请单为空！");
            }
            //获取卖方机构code
            String sellerOrgNo = getProductOrgNo(ba);
            // 查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(sellerOrgNo, details);
            if(null == stockDetails || stockDetails.size() == 0){
                return StatusDto.buildFailureStatusDto("库存为空！");
            }
            // 构建占用库存和订单占用库存关系
            Pair<List<BizStockDetail>, List<RelOrdstockOccupy>> pair = buildStockAndRelOrdEntity(details,stockDetails,applyType);
            List<BizStockDetail> stockDetailList = pair.getLeft();
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = pair.getRight();
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.PLATFORMALLOCATE, relOrdstockOccupies);
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, applyType,pir.getLeft(),sellerOrgNo);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 保存占用库存
            int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
            if(flag == 0){// 更新失败
                throw new CommonException("0", "更新占用库存失败！");
            }
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());
            // 批量保存入库计划详情
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());
            // 保存订单占用库存关系
            bizAllocateTradeorderDao.batchInsertRelOrdstockOccupy(relOrdstockOccupies);
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请处理成功！");
    }

    /**
     * 获取成本价
     * @param stockDetails
     * @param stockId 库存批次id
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getCostPrice(List<BizStockDetail> stockDetails, Long stockId){
        BigDecimal costPrice = BigDecimal.ZERO;
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
    public StatusDto cancelApply(String applyNo){
        try {
            // 根据申请单编号查询订单占用库存关系表
            List<RelOrdstockOccupy> list = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(applyNo);
            //根据订单占用库存关系构建库存list
            List<BizStockDetail> stockDetails = buildBizStockDetail(list);
            // 还原被占用的库存
            int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetails);
            if(flag == 0){// 更新失败
                throw new CommonException("0", "还原占用库存失败！");
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
            bizAllocateApplyDao.updateApplyOrderStatus(applyNo, ApplyStatusEnum.CANCEL.name());
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请撤销成功！");
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
        return super.platformInstockCallback(ba);
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
        outstockplanSeller(outList, relOrdstockOccupies,stockDetails, details, BizAllocateApply.AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
        //  构建平台入库计划：平台入库计划的成本价是卖方机构库存的成本价（单个商品有可能有多个批次库存，所以这里从处理计划构建）
        instockplanPlatformFromStock(inList, outList);
        // 买方入库计划(也要根据出库计划来，并且还要根据供应商来分组合并商品编号)
        instockplanPlatformFromStockAndGroup(inList, outList,details);
        return Pair.of(outList, inList);
    }

    /**
     * 平台入库计划构建
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-21 17:47:08
     */
    private void instockplanPlatformFromStock(List<BizInstockplanDetail> inList, List<BizOutstockplanDetail> outList){
        // 根据平台的no查询平台的仓库
        List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        String repositoryNo = "";
        if(null != list && list.size() > 0){
            repositoryNo = list.get(0).getStorehouseCode();
        }
        for(BizOutstockplanDetail bd : outList){
            BizInstockplanDetail inPlan = buildBizInstockplanDetail(bd);
            inPlan.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
            inPlan.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台机构编号
            inList.add(inPlan);
        }
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
     * 根据商品编号获取申请单详情
     * @param details 申请详细
     * @param productNo 商品编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private AllocateapplyDetailBO getAllocateapplyDetailBO(List<AllocateapplyDetailBO> details, String productNo){
        AllocateapplyDetailBO adb = new AllocateapplyDetailBO();
        for(AllocateapplyDetailBO ab : details){
            if(ab.getProductNo().equals(productNo)){
                adb = ab;
                break;
            }
        }
        return adb;
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
        inPlan.setProductNo(bd.getProductNo());// 商品编号
        inPlan.setProductType(bd.getProductType());// 商品类型
        inPlan.setProductCategoryname(bd.getProductCategoryname());// 商品分类名称
        inPlan.setProductName(bd.getProductName());// 商品名称
        inPlan.setProductUnit(bd.getProductUnit());// 商品计量单位
        inPlan.setTradeNo(bd.getTradeNo());// 交易批次号（申请单编号）
        inPlan.setSupplierNo(bd.getSupplierNo());//供应商编号
        inPlan.setCostPrice(bd.getCostPrice());// 成本价
        inPlan.setPlanInstocknum(bd.getPlanOutstocknum());// 计划入库数量
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.preInsert(userHolder.getLoggedUserId());
        inPlan.setStockType(bd.getStockType());// 库存类型
        inPlan.setRemark(bd.getRemark());// 备注
        inPlan.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型
        return inPlan;
    }
}
