package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.ApplyTypeEnum;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstock.service.OutstockOrderService;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
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
import java.util.Optional;

/**
 * 退货申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class RefundApplyHandleStrategy extends DefaultApplyHandleStrategy {

    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Resource
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    OutstockOrderService outstockOrderService;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    private UserHolder userHolder;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  退货申请处理
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
            //获取申请方机构code
            String applyorgNo = getProductOrgNo(ba);
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details);
            // 退货只生成一个订单
            if(null != list && list.size() >0 ){
                // 卖方机构为申请机构
                list.get(0).setSellerOrgno(applyorgNo);
                // 买方机构为平台
                list.get(0).setPurchaserOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            }

            //查询库存列表
            // 查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(applyorgNo, details);
            if(null == stockDetails || stockDetails.size() == 0){
                throw new CommonException("0", "库存为空！");
            }
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
            // 构建占用库存和订单占用库存关系
            List<BizStockDetail> stockDetailList = buildStockAndRelOrdEntity(details,stockDetails,applyType,relOrdstockOccupies);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.REFUND, relOrdstockOccupies);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());
            // 批量保存入库计划详情
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());
            // 查询出库计划
            List<BizOutstockplanDetail> outstockPlans = bizOutstockplanDetailDao.getOutstockplansByApplyNo(applyNo,applyorgNo);
            // 调用自动出库
            outstockOrderService.autoSaveOutstockOrder(applyNo, outstockPlans,ApplyTypeEnum.APPLYORDER.name());
            String stockType = getStockType(details);
            // 只有正常件才保存库存和占用关系
            if(BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(stockType)){
                // 保存占用库存
                //int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
                int flag = 0;
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
        return StatusDto.buildSuccessStatusDto("退换货没有撤销！");
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
        // 买方出库
        problemOutstockplanPurchaser(outList,relOrdstockOccupies,stockDetails,details);
        // 平台入库
        instockplanPlatform(inList,outList,details);
        return Pair.of(outList, inList);
    }

    /**
     * 平台入库构建(cost取自库存的价格)
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-21 17:47:08
     */
    private void instockplanPlatform(List<BizInstockplanDetail> inList, List<BizOutstockplanDetail> outList, List<AllocateapplyDetailBO> details){
        for (BizOutstockplanDetail outstockplan : outList) {
            BizInstockplanDetail inPlan = buildBizInstockplanDetail(outstockplan);
            Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> inPlan.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
            if (applyFilter.isPresent()) {
                AllocateapplyDetailBO ad = applyFilter.get();
                // 入库仓库编号
                inPlan.setInstockRepositoryNo(ad.getInRepositoryNo());
                // 买入机构编号
                inPlan.setInstockOrgno(ad.getInstockOrgno());
            }
            inPlan.setPlanInstocknum(outstockplan.getPlanOutstocknum());
            // 根据平台的no查询平台的仓库
            List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            String repositoryNo = "";
            if(null != list && list.size() > 0){
                repositoryNo = list.get(0).getStorehouseCode();
            }
            inPlan.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
            inPlan.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台机构编号
            inList.add(inPlan);
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
        inPlan.setInstockType(InstockTypeEnum.BARTER.toString());// 交易类型
        return inPlan;
    }

    /**
     * 问题件买方出库
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private void problemOutstockplanPurchaser(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details){
        // 申请方出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanPurchaser = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO detail = new AllocateapplyDetailBO();
                    Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> bd.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                    if (applyFilter.isPresent()) {
                        detail = applyFilter.get();
                    }
                    outstockplanPurchaser = buildBizOutstockplanDetail(detail,bd);
                    outstockplanPurchaser.setStockType(ro.getStockType());// 库存类型(在创建占用关系的时候赋值)
                    outstockplanPurchaser.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanPurchaser.setOutRepositoryNo(bd.getRepositoryNo());// 仓库code
                    outstockplanPurchaser.setOutOrgno(detail.getApplyorgNo());// 卖方机构code(退货的时候为申请机构)
                    outstockplanPurchaser.setStockId(bd.getId());// 库存编号id
                    outstockplanPurchaser.setCostPrice(bd.getCostPrice());// 成本价
                    outstockplanPurchaser.setOutstockType(OutstockTypeEnum.REFUND.toString());// 交易类型
                    outList.add(outstockplanPurchaser);
                    continue;
                }
            }
        }
    }

        /**
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details){
        List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
        // 构建生成订单（机构1对平台）
        BizAllocateTradeorder purchaserToPlatform = buildOrderEntity(details);// 买方到平台
        purchaserToPlatform.setSellerOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 从买方到平台"平台code"
        // 计算订单总价
        BigDecimal total = getSellTotal(details);
        purchaserToPlatform.setTotalPrice(total);
        list.add(purchaserToPlatform);

        return list;
    }
}
