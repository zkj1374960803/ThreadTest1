package com.ccbuluo.business.platform.order.service;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.constants.StockPlanEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
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
import java.util.Date;
import java.util.List;

/**
 * 调拨申请交易订单
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
@Service
public class TradeOrderServiceImpl implements TradeOrderService {
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private UserHolder userHolder;
    @Resource
    private GenerateDocCodeService generateDocCodeService;
    @Resource
    BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    BizStockDetailDao bizStockDetailDao;
    @Resource
    BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    BizInstockplanDetailDao bizInstockplanDetailDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  采购申请处理
     * @param applyNo 申请单编号
     *  @return 0返回失败，1返回成功
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int purchaseApplyHandle(String applyNo){
        int flag = 0;
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单（采购）
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, Constants.PROCESS_TYPE_PURCHASE);
            // 查询库存列表(平台的库存列表)
            List<BizStockDetail> stockDetails = getStockDetailList(BusinessPropertyHolder.TOP_SERVICECENTER, details);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, Constants.PROCESS_TYPE_PURCHASE);
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
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String processType){
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        // 采购
        if(Constants.PROCESS_TYPE_PURCHASE.equals(processType)){
            for(AllocateapplyDetailBO ad : details){// 采购的时候不生成出库计划，因为没有库存信息
                // 平台出库计划
//                BizOutstockplanDetail outstockplanDetail1 = buildBizOutstockplanDetail(ad, processType);
//                outstockplanDetail1.setStockId("这个批次库存id从哪里来TODO");// 批次库存id(平台的库存)
//                outstockplanDetail1.setOutRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 出库仓库编号（平台code）
                // 平台入库计划+
                BizInstockplanDetail instockplanDetail1 = new BizInstockplanDetail();
                instockplanDetail1 = buildBizInstockplanDetail(ad, processType);
                instockplanDetail1.setInstockRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 入库仓库编号
                // 买入方入库计划
                BizInstockplanDetail instockplanDetail2 = new BizInstockplanDetail();
                instockplanDetail2 = buildBizInstockplanDetail(ad, processType);
                instockplanDetail2.setInstockRepositoryNo(ad.getInstockOrgno());// 入库仓库编号

                inList.add(instockplanDetail1);
                inList.add(instockplanDetail2);
            }
        }
        if(Constants.PROCESS_TYPE_TRANSFER.equals(processType)){// 调拨
            int i = 0;
            for(BizStockDetail bd : stockDetails){
                AllocateapplyDetailBO ad = getAllocateapplyDetailByProductNo(details, bd.getProductNo());
                // 卖方机构出库计划
                BizOutstockplanDetail outstockplanDetail1 = new BizOutstockplanDetail();
                outstockplanDetail1 = buildBizOutstockplanDetail(ad, processType);
                outstockplanDetail1.setOutRepositoryNo(bd.getRepositoryNo());// 卖方仓库编号（根据机构和商品编号查询的库存）
                if(null != ad.getProcessOrgno() && Constants.PLATFORM.equals(ad.getProcessOrgno())){// 如果是平台参与
                    // 平台入库计划
                    BizInstockplanDetail instockplanDetail1 = new BizInstockplanDetail();
                    instockplanDetail1 = buildBizInstockplanDetail(ad, processType);
                    instockplanDetail1.setInstockRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台code
                    // 平台出库计划
                    BizOutstockplanDetail outstockplanDetail2 = new BizOutstockplanDetail();
                    outstockplanDetail2 = buildBizOutstockplanDetail(ad, processType);
                    outstockplanDetail2.setOutRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台code
                    outList.add(outstockplanDetail2);
                    inList.add(instockplanDetail1);
                }
                // 买方入库计划
                BizInstockplanDetail instockplanDetail2 = new BizInstockplanDetail();
                instockplanDetail2 = buildBizInstockplanDetail(ad, processType);
                instockplanDetail2.setInstockRepositoryNo(ad.getInstockOrgno());// 买方机构仓库编号
                outList.add(outstockplanDetail1);
                inList.add(instockplanDetail2);
            }
        }
        return Pair.of(outList, inList);
    }

    /**
     * 构建出库计划
     * @param ad 申请详情
     * @return
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(AllocateapplyDetailBO ad, String processType){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        outPlan.setOutstockType(processType);// 交易类型
        outPlan.setProductNo(ad.getProductNo());// 商品编号
        outPlan.setProductType(ad.getProductType());// 商品类型
        outPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(ad.getProductName());// 商品名称
        outPlan.setProductUnit(ad.getUnit());// 商品计量单位
        outPlan.setTradeNo(String.valueOf(ad.getId()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setCostPrice(ad.getCostPrice());// 成本价
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setPlanOutstocknum(ad.getApplyNum());// 计划出库数量applyNum
        outPlan.setActualOutstocknum(ad.getApplyNum());// 实际出库数量
        outPlan.setPlanStatus(StockPlanEnum.DOING.toString());// 出库计划的状态（计划执行中）
        outPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        outPlan.setCreateTime(new Date());// 创建时间
        outPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return outPlan;
    }
    /**
     * 构建入库计划
     * @param ad 申请详情
     * @return
     */
    private BizInstockplanDetail buildBizInstockplanDetail(AllocateapplyDetailBO ad, String processType){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        inPlan.setInstockType(processType);// 交易类型
        inPlan.setProductNo(ad.getProductNo());// 商品编号
        inPlan.setProductType(ad.getProductType());// 商品类型
        inPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        inPlan.setProductName(ad.getProductName());// 商品名称
        inPlan.setProductUnit(ad.getUnit());// 商品计量单位
        inPlan.setTradeNo(String.valueOf(ad.getId()));// 交易批次号（申请单编号）
        inPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        inPlan.setCostPrice(ad.getCostPrice());// 成本价
        inPlan.setPlanInstocknum(ad.getApplyNum());// 计划入库数量
        inPlan.setActualInstocknum(ad.getApplyNum());// 实际入库数量
        inPlan.setCompleteStatus(StockPlanEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        inPlan.setCreateTime(new Date());// 创建时间
        inPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return inPlan;
    }

    // 根据商品编号查找到某个商品的申请单详情信息
    private AllocateapplyDetailBO getAllocateapplyDetailByProductNo(List<AllocateapplyDetailBO> details, String productNo){
        AllocateapplyDetailBO applyDetail = new AllocateapplyDetailBO();
        for(AllocateapplyDetailBO ad : details){
            if (productNo.equals(ad.getProductNo())){
                applyDetail = ad;
            }
        }
        return applyDetail;
    }
    /**
     *  调拨申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int allocateApplyHandle(String applyNo){
        int flag = 0;
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, Constants.PROCESS_TYPE_TRANSFER);
            // 构建占用库存和订单占用库存关系
            //获取卖方机构code
            String sellerOrgNo = getSellerOrgNo(list);
            //查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(sellerOrgNo, details);
            Pair<List<BizStockDetail>, List<RelOrdstockOccupy>> pair = buildStockAndRelOrdEntity(details,stockDetails);
            List<BizStockDetail> stockDetailList = pair.getLeft();
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = pair.getRight();
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pir = buildOutAndInstockplanDetail(details, stockDetails, Constants.PROCESS_TYPE_TRANSFER);
            // 保存占用库存
            flag = bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
            if(flag == 0){// 更新失败
                throw new CommonException("0", "更新占用库存失败！");
            }
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(pir.getLeft());
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

    // 根据订单列表获取卖方机构code
    private String getSellerOrgNo(List<BizAllocateTradeorder> list){
        String sellerOrgno = "";
        for(BizAllocateTradeorder bt : list){
            //卖方机构：不能为平台或供应商（为空）
            if(StringUtils.isNotBlank(bt.getSellerOrgno()) && !BusinessPropertyHolder.TOP_SERVICECENTER.equals(bt.getSellerOrgno())){
                sellerOrgno = bt.getSellerOrgno();
            }
        }
        return sellerOrgno;
    }

    /**
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @param processType 交易类型
     * @return
     */
    private List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details, String processType){
        // 构建生成订单（机构1对平台）
        BizAllocateTradeorder bizAllocateTradeorder1 = buildOrderEntity(details);
        BizAllocateTradeorder bizAllocateTradeorder2 = buildOrderEntity(details);
        bizAllocateTradeorder1.setSellerOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从买方到平台"平台code"
        bizAllocateTradeorder2.setPurchaserOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从平台到卖方"平台code"
        List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
        if(Constants.PROCESS_TYPE_PURCHASE.equals(processType)){// 采购的时候卖方为供应商（此时为空）
            bizAllocateTradeorder2.setSellerOrgno("");// (供应商不填为空)
        }
        list.add(bizAllocateTradeorder1);// 从买方到平台
        list.add(bizAllocateTradeorder2);// 从平台到卖方
        return list;
    }
    // 构建生成订单
    private BizAllocateTradeorder buildOrderEntity(List<AllocateapplyDetailBO> details){
        BizAllocateTradeorder bt = new BizAllocateTradeorder();
        // 生成订单编号
        StatusDto<String> supplierCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
        if(!supplierCode.isSuccess()){
            throw new CommonException(supplierCode.getCode(), "生成订单编号失败！");
        }
        bt.setOrderNo(supplierCode.getData());//订单号
        bt.setCreator(userHolder.getLoggedUserId());//处理人
        bt.setCreateTime(new Date());
        bt.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        bt.setOrderStatus("PAYMENTWAITING");//默认待支付
        for(AllocateapplyDetailBO bd : details){
            bt.setApplyNo(bd.getApplyNo());// 申请单编号
            bt.setPurchaserOrgno(bd.getInstockOrgno());//买方机构
            bt.setSellerOrgno(bd.getOutstockOrgno());//卖方机构
            bt.setTradeType(bd.getProcessType());//交易类型
            break;
        }
        // 计算订单总价
        BigDecimal total = getTatal(details);
        bt.setTotalPrice(total);
        return bt;
    }
    // 计算订单总价
    private BigDecimal getTatal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(AllocateapplyDetailBO bd : details){
            bigDecimal.add(bd.getSellPrice());
        }
        return bigDecimal;
    }
    /**
     * 构建占用库存和订单占用库存关系
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    private Pair<List<BizStockDetail>, List<RelOrdstockOccupy>>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails){
        //订单占用库存关系
        List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
        for(BizStockDetail ad : stockDetails){// 遍历库存
            //占用库存
            Long occupyStockNum = convertStockDetail(details, ad);
            //构建订单占用库存关系
            RelOrdstockOccupy ro = new RelOrdstockOccupy();
            ro.setOrderType(Constants.PROCESS_TYPE_TRANSFER);//订单类型(调拨，采购不占用库存)
            ro.setDocNo(ad.getTradeNo());//申请单号
            ro.setStockId(ad.getId());//库存id
            ro.setOccupyNum(occupyStockNum);//占用数量
            ro.setOccupyStatus(StockPlanEnum.DOING.toString());//占用状态occupy_status
            Date time = new Date();
            ro.setOccupyStarttime(time);//占用开始时间
            ro.setCreator(userHolder.getLoggedUserId());//创建人
            ro.setCreateTime(time);//创建时间
            ro.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
            relOrdstockOccupies.add(ro);
        }
        return Pair.of(stockDetails, relOrdstockOccupies);
    }

    // 根据卖方机构code获取库存详情
    private List<BizStockDetail> getStockDetailList(String sellerOrgno, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(sellerOrgno, codeList);
    }
    // 获取商品code
    private List<String> getProductList(List<AllocateapplyDetailBO> details){
        List<String> list = new ArrayList<String>();
        for(AllocateapplyDetailBO ad : details){
            list.add(ad.getProductNo());
        }
        return list;
    }

    /**
     * 遍历库存并转换可用库存
     * @param details 申请单详情
     * @param stockDetail 库存对象
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    private Long convertStockDetail(List<AllocateapplyDetailBO> details, BizStockDetail stockDetail){
        Long occupyStockNum = 0l;//占用数量
        for(AllocateapplyDetailBO ad : details){
            if(ad.getProductNo().equals(stockDetail.getProductNo())){// 找到对应商品
                // 调拨申请数量
                Long applyNum = ad.getApplyNum();
                // 有效库存
                Long validStock = stockDetail.getValidStock();
                if(validStock.intValue() == applyNum.intValue()){// 如果本批次的库存正好等于要调拨的数量
                    validStock = 0l;// 剩余库存为零
                    ad.setApplyNum(0l);//需要调拨的数量也设置为零
                    //记录占用数量
                    occupyStockNum = validStock;
                }
                if(validStock.intValue() < applyNum.intValue()){// 如果本批次的库存缺少
                    ad.setApplyNum(applyNum - validStock);// 下次再有库存过来的时候，就会减去剩下的调拨商品数量
                    //记录占用数量
                    occupyStockNum = validStock;// 占用了全部可用库存
                    validStock = 0l;// 剩余库存为零
                }
                if(validStock.intValue() > applyNum.intValue()){// 如果本批次的库存充足
                    validStock = validStock - applyNum;// 剩余库存为零
                    ad.setApplyNum(0l);//需要调拨的数量也设置为零,下次再有库存过来的时候就不操作了
                    //记录占用数量
                    occupyStockNum = applyNum;
                }
                // 占用库存
                stockDetail.setOccupyStock(occupyStockNum);
                // 有效库存
                stockDetail.setValidStock(validStock);
                break;
            }
        }
        return occupyStockNum;
    }

    /**
     *  撤销申请
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

    // 根据申请单编号查询订单占用库存关系表
    private List<BizStockDetail> buildBizStockDetail(List<RelOrdstockOccupy> list){
        List<BizStockDetail> stockDetails =  new ArrayList<BizStockDetail>();
        List<Long> sList = getStockDtailIds(list);
        if(null == sList || sList.size() == 0){
            return null;
        }
        stockDetails = bizStockDetailDao.getStockDetailListByIds(sList);
        for(BizStockDetail bd : stockDetails){
            for(RelOrdstockOccupy roo : list){
                if(bd.getId().intValue() == roo.getStockId().intValue()){
                    if(bd.getValidStock() != null && roo.getOccupyNum() != null && bd.getOccupyStock() != null){
                        bd.setValidStock(bd.getValidStock() + roo.getOccupyNum());//具体到某一条库存明细id，所被占用的数量（更新的时候要加上原来的数量）
                        bd.setOccupyStock(bd.getOccupyStock() - roo.getOccupyNum());// 占用的库存也要减去当时的占用记录
                    }
                }
        }
        }
        return stockDetails;
    }
    // 获取库存详情的ids
    private List<Long> getStockDtailIds(List<RelOrdstockOccupy> list){
        List<Long> slist = new ArrayList<Long>();
        for(RelOrdstockOccupy r : list){
            slist.add(r.getStockId());
        }
        return slist;
    }
}
