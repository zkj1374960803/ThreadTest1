package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.ProductStockInfoDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstock.service.OutstockOrderService;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 换货申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class BarterApplyHandleStrategy extends DefaultApplyHandleStrategy {

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
    private BizStockDetailDao bizStockDetailDao;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    private UserHolder userHolder;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  换货申请处理
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
            // 判断库存是否满足
            checkStock(ba.getApplyorgNo(),details);
            // 构建占用库存和订单占用库存关系
            //获取申请方机构code
            String applyorgNo = getProductOrgNo(ba);
            //查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(applyorgNo, details);
            if(null == stockDetails || stockDetails.size() == 0){
                throw new CommonException("0", "库存为空！");
            }
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
            // 构建占用库存和订单占用库存关系
            List<BizStockDetail> stockDetailList = buildStockAndRelOrdEntity(details,stockDetails,applyType,relOrdstockOccupies);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.BARTER, relOrdstockOccupies);
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
            // 查询出库计划
            List<BizOutstockplanDetail> outstockPlans = bizOutstockplanDetailDao.getOutstockplansByApplyNo(applyNo,applyorgNo);
            // 调用自动出库
            outstockOrderService.autoSaveOutstockOrder(applyNo, outstockPlans,ApplyTypeEnum.APPLYORDER.name());
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请处理成功！");
    }

    /**
     *  申请撤销（更改退换货类型用）
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Override
    public StatusDto cancelApply(String applyNo){
        try {
            //删除订单占用关系
            bizAllocateTradeorderDao.deleteRelOrdstockOccupyByApplyNo(applyNo);
            // 删除订单
            bizAllocateTradeorderDao.deleteAllocateTradeorderByApplyNo(applyNo);
            // 删除出库计划
            bizOutstockplanDetailDao.deleteOutstockplanDetailByApplyNo(applyNo);
            // 删除入库计划
            bizInstockplanDetailDao.batchInsertInstockplanDetail(applyNo);
            //恢复自动出库的数据 TODO
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("执行完成！");
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
        // 申请方出库
        problemOutstockplanPurchaser(outList,relOrdstockOccupies,stockDetails,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
        // 平台入库
        problemInstockplanPlatform(inList,details, outList);
        // 申请方入库（换货：买方机构的入库要以出库的数据来构建（不同批次，不同价格）（问题件库存））
        problemInstockplanPurchaser(inList,details, outList);
        return Pair.of(outList, inList);
    }

    /**
     *买方出库
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemOutstockplanPurchaser(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 买方出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanPurchaser = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                // 关系库存批次id和库存批次id相等
                if(ro.getStockId().intValue() == bd.getId().intValue()){
                    AllocateapplyDetailBO detail = new AllocateapplyDetailBO();
                    Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> bd.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                    if (applyFilter.isPresent()) {
                        detail = applyFilter.get();
                    }
                    outstockplanPurchaser = buildBizOutstockplanDetail(detail,bd);
                    // 库存类型(在创建占用关系的时候赋值)
                    outstockplanPurchaser.setStockType(ro.getStockType());
                    // 计划出库数量applyNum
                    outstockplanPurchaser.setPlanOutstocknum(ro.getOccupyNum());
                    // 仓库code
                    outstockplanPurchaser.setOutRepositoryNo(bd.getRepositoryNo());
                    // 换货的时候是申请方机构出货
                    outstockplanPurchaser.setOutOrgno(detail.getApplyorgNo());
                    // 库存编号id
                    outstockplanPurchaser.setStockId(bd.getId());
                    // 成本价
                    outstockplanPurchaser.setCostPrice(bd.getCostPrice());
                    // 交易类型
                    outstockplanPurchaser.setOutstockType(OutstockTypeEnum.BARTER.toString());
                    outstockplanPurchaser.setPurchaseInfo(bd.getPurchaseInfo());
                    outList.add(outstockplanPurchaser);
                    continue;
                }
            }
        }
    }

    /**
     * 申请方入库
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemInstockplanPurchaser(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, List<BizOutstockplanDetail> outList){
        Map<String, List<AllocateapplyDetailBO>> byProductNoGroup = details.stream().collect(Collectors.groupingBy(AllocateapplyDetailBO::getProductNo));
        for (BizOutstockplanDetail outstockplan : outList) {
            List<AllocateapplyDetailBO> allocateapplyDetailBOS = byProductNoGroup.get(outstockplan.getProductNo());
            AllocateapplyDetailBO allocateapplyDetailBO = allocateapplyDetailBOS.get(0);
            BizInstockplanDetail instockplanPurchaser;
            instockplanPurchaser = buildApplyBizInstockplanDetail(allocateapplyDetailBO);
            instockplanPurchaser.setInstockType(InstockTypeEnum.BARTER.name());
            instockplanPurchaser.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
            instockplanPurchaser.setCostPrice(outstockplan.getCostPrice());
            instockplanPurchaser.setPurchaseInfo(outstockplan.getPurchaseInfo());
            inList.add(instockplanPurchaser);
        }
    }

    /**
     * 构建申请方入库计划
     * @param ad 申请详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BizInstockplanDetail buildApplyBizInstockplanDetail(AllocateapplyDetailBO ad){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        inPlan.setProductNo(ad.getProductNo());// 商品编号
        inPlan.setProductType(ad.getProductType());// 商品类型
        inPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        inPlan.setProductName(ad.getProductName());// 商品名称
        inPlan.setProductUnit(ad.getUnit());// 商品计量单位
        inPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        inPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        inPlan.setPlanInstocknum(ad.getApplyNum());// 计划入库数量
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.preInsert(userHolder.getLoggedUserId());
        inPlan.setRemark(ad.getRemark());// 备注
        inPlan.setInstockRepositoryNo(ad.getInRepositoryNo());
        inPlan.setInstockOrgno(ad.getApplyorgNo());// 申请方入机构编号
        // 卖方机构的编号
        inPlan.setSellerOrgno(ad.getOutstockOrgno());
        return inPlan;
    }

    /**
     * 平台入库
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemInstockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, List<BizOutstockplanDetail> outList){
        // 平台入库
//        for(AllocateapplyDetailBO ad : details){
//            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
//            // 平台入库计划
//            instockplanPlatform = buildBizInstockplanDetail(ad);
//            // 交易类型
//            instockplanPlatform.setInstockType(InstockTypeEnum.BARTER.toString());
//            // 库存类型(在创建占用关系的时候赋值)
//            instockplanPlatform.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());
//            //  平台采购类型的成本价在生成入库计划的时候是0，等入库回调的时候再回填
//            instockplanPlatform.setCostPrice(BigDecimal.ZERO);
//            // 根据平台的no查询平台的仓库
//            List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
//            String repositoryNo = "";
//            if(null != list && list.size() > 0){
//                repositoryNo = list.get(0).getStorehouseCode();
//            }
//            instockplanPlatform.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
//            instockplanPlatform.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台机构编号
//            // 卖方机构的编号
//            instockplanPlatform.setSellerOrgno(ad.getApplyorgNo());
//            inList.add(instockplanPlatform);
//        }

        for (BizOutstockplanDetail outstockplan : outList) {
            BizInstockplanDetail inPlan = buildBizInstockplanDetail(outstockplan);
            Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> inPlan.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
            if (applyFilter.isPresent()) {
                AllocateapplyDetailBO ad = applyFilter.get();
                // 入库仓库编号
                inPlan.setInstockRepositoryNo(ad.getInRepositoryNo());
                // 买入机构编号
                inPlan.setInstockOrgno(ad.getInstockOrgno());
                // 卖方机构的编号
                inPlan.setSellerOrgno(ad.getApplyorgNo());
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
            inPlan.setPurchaseInfo(outstockplan.getPurchaseInfo());
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

}
