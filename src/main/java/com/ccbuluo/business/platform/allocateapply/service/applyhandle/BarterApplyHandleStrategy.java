package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.InstockTypeEnum;
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
            outstockOrderService.autoSaveOutstockOrder(applyNo, outstockPlans);
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
        // 申请方出库
        problemOutstockplanPurchaser(outList,relOrdstockOccupies,stockDetails,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
        // 平台入库
        problemInstockplanPlatform(inList,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
        // 平台出库
//        outstockplanPlatform(outList,relOrdstockOccupies,stockDetails,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
        // 申请方入库（换货：买方机构的入库要以出库的数据来构建（不同批次，不同价格）（问题件库存））
        problemInstockplanPurchaser(inList,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
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
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO detail = new AllocateapplyDetailBO();
                    Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> bd.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                    if (applyFilter.isPresent()) {
                        detail = applyFilter.get();
                    }
                    outstockplanPurchaser = buildBizOutstockplanDetail(detail, applyType,bd);
                    outstockplanPurchaser.setStockType(ro.getStockType());// 库存类型(在创建占用关系的时候赋值)
                    outstockplanPurchaser.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanPurchaser.setOutRepositoryNo(bd.getRepositoryNo());// 仓库code
                    outstockplanPurchaser.setOutOrgno(detail.getApplyorgNo());// 换货的时候是申请方机构出货
                    outstockplanPurchaser.setStockId(bd.getId());// 库存编号id
                    outstockplanPurchaser.setCostPrice(bd.getCostPrice());// 成本价
                    outList.add(outstockplanPurchaser);
                    continue;
                }
            }
        }
    }

    /**
     * 申请方入库
     * @param details 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemInstockplanPurchaser(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 买入方入库计划
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPurchaser = new BizInstockplanDetail();
            instockplanPurchaser = buildBizInstockplanDetail(ad, applyType);
            instockplanPurchaser.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());// 库存类型(在创建占用关系的时候赋值)
            instockplanPurchaser.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型（只有平台是采购，机构是调拨）
            instockplanPurchaser.setInstockRepositoryNo(ad.getInRepositoryNo());// 入库仓库编号
            instockplanPurchaser.setInstockOrgno(ad.getApplyorgNo());// 申请方入机构编号
            inList.add(instockplanPurchaser);
        }
    }

    /**
     * 平台入库
     * @param details 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemInstockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 平台入库
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
            // 平台入库计划
            instockplanPlatform = buildBizInstockplanDetail(ad, applyType);
            instockplanPlatform.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());// 库存类型(在创建占用关系的时候赋值)
            instockplanPlatform.setCostPrice(BigDecimal.ZERO);//  平台采购类型的成本价在生成入库计划的时候是0，等入库回调的时候再回填
            // 根据平台的no查询平台的仓库
            List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            String repositoryNo = "";
            if(null != list && list.size() > 0){
                repositoryNo = list.get(0).getStorehouseCode();
            }
            instockplanPlatform.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
            instockplanPlatform.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台机构编号
            inList.add(instockplanPlatform);
        }
    }
}
