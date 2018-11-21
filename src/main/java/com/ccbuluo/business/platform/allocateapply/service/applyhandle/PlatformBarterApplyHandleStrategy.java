package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 平台退换货申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class PlatformBarterApplyHandleStrategy extends DefaultApplyHandleStrategy {

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
    @Autowired
    private UserHolder userHolder;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  退换货申请处理
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
            Map<String, List<BizStockDetail>> product = stockDetails.stream().collect(Collectors.groupingBy(BizStockDetail::getSupplierNo));
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
            // 构建占用库存和订单占用库存关系
            buildStockAndRelOrdEntitys(details,product,applyType,relOrdstockOccupies);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, BizAllocateApply.AllocateApplyTypeEnum.BARTER, relOrdstockOccupies);
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());

            // 批量保存入库计划详情
            bizInstockplanDetailDao.batchInsertInstockplanDetail(pir.getRight());
            // 调用自动出库
            // 根据申请查询出库计划
            List<BizOutstockplanDetail> outstockplansByApplyNo = bizOutstockplanDetailDao.getOutstockplansByApplyNo(applyNo, applyorgNo);
            outstockOrderService.autoSaveOutstockOrder(applyNo, outstockplansByApplyNo,ApplyTypeEnum.APPLYORDER.name());
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
        return null;
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
//        problemInstockplanPlatform(inList,details, BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString());
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
                    outstockplanPurchaser.setCostPrice(new BigDecimal(bd.getPurchasePrice()));
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
            instockplanPurchaser.setPlanInstocknum(outstockplan.getPlanOutstocknum());
            instockplanPurchaser.setSupplierNo(outstockplan.getSupplierNo());
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
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void problemInstockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 平台入库
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
            // 平台入库计划
            instockplanPlatform = buildBizInstockplanDetail(ad);
            // 交易类型
            instockplanPlatform.setInstockType(InstockTypeEnum.BARTER.toString());
            // 库存类型(在创建占用关系的时候赋值)
            instockplanPlatform.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());
            //  平台采购类型的成本价在生成入库计划的时候是0，等入库回调的时候再回填
            instockplanPlatform.setCostPrice(BigDecimal.ZERO);
            // 根据平台的no查询平台的仓库
            List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            String repositoryNo = "";
            if(null != list && list.size() > 0){
                repositoryNo = list.get(0).getStorehouseCode();
            }
            instockplanPlatform.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
            instockplanPlatform.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台机构编号
            // 卖方机构的编号
            instockplanPlatform.setSellerOrgno(ad.getOutstockOrgno());
            inList.add(instockplanPlatform);
        }
    }



    /**
     * 构建占用库存和订单占用库存关系(不管是正常申请和退换货申请，每个申请单都有可能存在正常件和问题件)
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @param applyType 申请类型
     * @param relOrdstockOccupies 订单占用库存关系
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    public List<BizStockDetail>  buildStockAndRelOrdEntitys(List<AllocateapplyDetailBO> details, Map<String, List<BizStockDetail>> stockDetails, String applyType, List<RelOrdstockOccupy> relOrdstockOccupies){
        for(AllocateapplyDetailBO detail : details){
            // 申请个数
            Long applyNum = detail.getApplyNum();
            // 申请商品的数据已经出库完成了
            if(applyNum.intValue() == 0){
                continue;
            }
            List<BizStockDetail> bizStockDetailList = stockDetails.get(detail.getSupplierNo());
            //占用库存
            convertStockDetail(bizStockDetailList,detail,applyNum,applyType, relOrdstockOccupies);
        }
//        List<BizStockDetail> list = distinctStockDetail(bizStockDetailList,relOrdstockOccupies);
        return null;
    }
    private List<BizStockDetail> distinctStockDetail(List<BizStockDetail> stockDetails,List<RelOrdstockOccupy> relOrdstockOccupies){
        List<BizStockDetail> list = new ArrayList<BizStockDetail>();
        for(BizStockDetail stock : stockDetails){
            Optional<RelOrdstockOccupy> relFilter = relOrdstockOccupies.stream() .filter(relOrdstock -> stock.getId().intValue() == relOrdstock.getStockId().intValue()) .findFirst();
            if (relFilter.isPresent()) {
                list.add(stock);
            }
        }
        return list;
    }

    /**
     * 遍历库存并转换可用库存(不管是正常申请和退换货申请，每个申请单都有可能存在正常件和问题件)
     * @param detail 申请单详情
     * @param stockDetail 库存对象
     * @param applyType 申请类型
     * @param relOrdstockOccupies 订单占用库存关系
     * 申请数量
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    private Long convertStockDetail(List<BizStockDetail> stockDetail,AllocateapplyDetailBO detail, Long applyNum, String applyType, List<RelOrdstockOccupy> relOrdstockOccupies){
        //占用数量
        Long occupyStockNum = 0L;
        // 被占用库存（有效库存或问题库存）
        Long validStock = 0L;
        // 库存的id已经被排序（先入先出）
        for(BizStockDetail stock : stockDetail){
            if(applyNum.intValue() == 0 && stock.getProductNo().equals(detail.getProductNo())){
                stock.setOccupyStock(0L);
                continue;
            }
            // 找到对应商品
            if(stock.getProductNo().equals(detail.getProductNo())){
                // 问题件
                if(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name().equals(detail.getStockType())){
                    validStock = stock.getProblemStock();
                }else if(BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(detail.getStockType())){// 正常件
                    validStock = stock.getValidStock();
                }
                if(null == validStock || validStock.intValue() == 0){
                    stock.setOccupyStock(0L);
                    continue;
                }
                // 如果本批次的库存正好等于要调拨的数量
                if(validStock.intValue() == applyNum.intValue()){
                    // 剩余库存为零
                    //记录占用数量
                    occupyStockNum = validStock;
                    validStock = 0L;
                    applyNum = 0L;
                }else if(validStock.intValue() < applyNum.intValue()){// 如果本批次的库存缺少
                    // 下次再有库存过来的时候，就会减去剩下的调拨商品数量
                    applyNum = applyNum - validStock;
                    // 记录占用数量，占用了全部可用库存
                    occupyStockNum = validStock;
                    // 剩余库存为零
                    validStock = 0L;
                }else if(validStock.intValue() > applyNum.intValue()){// 如果本批次的库存充足
                    // 剩余库存为零
                    validStock = validStock - applyNum;
                    //需要调拨的数量也设置为零,下次再有库存过来的时候就不操作了
                    //记录占用数量
                    occupyStockNum = applyNum;
                    applyNum = 0L;
                }
                // 只有正常件的时候才保存占用库存
                if(BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(detail.getStockType())){
                    // 占用库存
                    stock.setOccupyStock(occupyStockNum);
                    // 有效库存(在保存的时候会用有效库存减去占用库存)
                }
                //构建订单占用库存关系
                RelOrdstockOccupy ro = new RelOrdstockOccupy();
                ro.setOrderType(applyType);
                //申请单号
                ro.setDocNo(detail.getApplyNo());
                //库存id
                ro.setStockId(stock.getId());
                //占用数量
                ro.setOccupyNum(occupyStockNum);
                //占用状态occupy_status
                ro.setOccupyStatus(StockPlanStatusEnum.DOING.toString());
                Date time = new Date();
                //占用开始时间
                ro.setOccupyStarttime(time);
                ro.preInsert(userHolder.getLoggedUserId());
                // 库存类型
                ro.setStockType(detail.getStockType());
                relOrdstockOccupies.add(ro);
            }
        }
        return applyNum;
    }

}
