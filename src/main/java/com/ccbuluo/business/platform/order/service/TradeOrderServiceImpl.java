package com.ccbuluo.business.platform.order.service;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.constants.StockPlanEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
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
    public int purchaseApplyHandle(String applyNo){
        int flag = 0;
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailDTO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单（采购）
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, Constants.PROCESS_TYPE_PURCHASE);
            // 查询库存列表(平台的库存列表)
            List<BizStockDetail> stockDetails = getStockDetailList(BusinessPropertyHolder.TOP_SERVICECENTER, details);
            // 构建出库和入库计划并保存(平台入库，平台出库，买方入库)
            buildOutAndInstockplanDetailAndSave(details, stockDetails, Constants.PROCESS_TYPE_PURCHASE);
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
    public void buildOutAndInstockplanDetailAndSave(List<AllocateapplyDetailDTO> details, List<BizStockDetail> stockDetails, String processType){
        // 采购
        if(Constants.PROCESS_TYPE_PURCHASE.equals(processType)){
            for(BizStockDetail bd : stockDetails){
                AllocateapplyDetailDTO ad = getAllocateapplyDetailByProductNo(details, bd.getProductNo());
                // 平台出库计划
                BizOutstockplanDetail outstockplanDetail1 = buildBizOutstockplanDetail(bd, ad, processType);
                outstockplanDetail1.setOutRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 出库仓库编号（平台code）
                // 平台入库计划
                BizInstockplanDetail instockplanDetail1 = buildBizInstockplanDetail(bd, ad, processType);
                instockplanDetail1.setInstockRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 入库仓库编号
                // 买入方入库计划
                BizInstockplanDetail instockplanDetail2 = buildBizInstockplanDetail(bd, ad, processType);
                instockplanDetail2.setInstockRepositoryNo(ad.getInstockOrgno());// 入库仓库编号
                // 保存出库
                Long id = bizOutstockplanDetailDao.saveEntity(outstockplanDetail1);
                instockplanDetail1.setOutstockPlanid(id);//出库计划id
                // 保存入库
                bizInstockplanDetailDao.saveEntity(instockplanDetail1);
                bizInstockplanDetailDao.saveEntity(instockplanDetail2);
            }
        }
        if(Constants.PROCESS_TYPE_TRANSFER.equals(processType)){// 调拨
            for(BizStockDetail bd : stockDetails){
                AllocateapplyDetailDTO ad = getAllocateapplyDetailByProductNo(details, bd.getProductNo());
                // 卖方机构出库计划
                BizOutstockplanDetail outstockplanDetail1 = buildBizOutstockplanDetail(bd, ad, processType);
                outstockplanDetail1.setOutRepositoryNo(ad.getOutstockOrgno());// 卖方仓库编号
                // 平台入库计划
                BizInstockplanDetail instockplanDetail1 = buildBizInstockplanDetail(bd, ad, processType);
                instockplanDetail1.setInstockRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台code
                // 平台出库计划
                BizOutstockplanDetail outstockplanDetail2 = buildBizOutstockplanDetail(bd, ad, processType);
                outstockplanDetail2.setOutRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台code
                // 买方入库计划
                BizInstockplanDetail instockplanDetail2 = buildBizInstockplanDetail(bd, ad, processType);
                instockplanDetail2.setInstockRepositoryNo(ad.getInstockOrgno());// 买方机构仓库编号
                // 保存出库
                bizOutstockplanDetailDao.saveEntity(outstockplanDetail1);
                Long id = bizOutstockplanDetailDao.saveEntity(outstockplanDetail2);
                instockplanDetail1.setOutstockPlanid(id);// 出库计划id
                // 保存入库
                bizInstockplanDetailDao.saveEntity(instockplanDetail1);
                bizInstockplanDetailDao.saveEntity(instockplanDetail2);
            }
        }
    }

    /**
     * 构建出库计划
     * @param bd  库存
     * @param ad 申请详情
     * @return
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(BizStockDetail bd, AllocateapplyDetailDTO ad,String processType){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        outPlan.setOutstockType(processType);// 交易类型
        outPlan.setStockId(bd.getId());// 库存id
        outPlan.setProductNo(bd.getProductNo());// 商品编号
        outPlan.setProductType(bd.getProductType());// 商品类型
        outPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        outPlan.setProductUnit(ad.getUnit());// 商品计量单位
        outPlan.setTradeNo(String.valueOf(ad.getId()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setCostPrice(ad.getCostPrice());// 成本价
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setPlanOutstocknum(ad.getApplyNum());// 计划出库数量applyNum
        outPlan.setActualOutstocknum(ad.getApplyNum());// 实际出库数量
        outPlan.setPlanStatus(StockPlanEnum.NOTEFFECTIVE.toString());// 出库计划的状态（未生效）
        outPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        outPlan.setCreateTime(new Date());// 创建时间
        outPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return outPlan;
    }
    /**
     * 构建入库计划
     * @param bd  库存
     * @param ad 申请详情
     * @return
     */
    private BizInstockplanDetail buildBizInstockplanDetail(BizStockDetail bd, AllocateapplyDetailDTO ad,String processType){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        inPlan.setInstockType(processType);// 交易类型
        inPlan.setProductNo(bd.getProductNo());// 商品编号
        inPlan.setProductType(bd.getProductType());// 商品类型
        inPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        inPlan.setProductUnit(ad.getUnit());// 商品计量单位
        inPlan.setTradeNo(String.valueOf(ad.getId()));// 交易批次号（申请单编号）
        inPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        inPlan.setCostPrice(ad.getCostPrice());// 成本价
        inPlan.setPlanInstocknum(ad.getApplyNum());// 计划入库数量
        inPlan.setActualInstocknum(ad.getApplyNum());// 实际入库数量
        inPlan.setCompleteStatus(StockPlanEnum.NOTEFFECTIVE.toString());// 完成状态（未生效）
        inPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        inPlan.setCreateTime(new Date());// 创建时间
        inPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return inPlan;
    }

    // 根据商品编号查找到某个商品的申请单详情信息
    private AllocateapplyDetailDTO getAllocateapplyDetailByProductNo(List<AllocateapplyDetailDTO> details, String productNo){
        AllocateapplyDetailDTO applyDetail = new AllocateapplyDetailDTO();
        for(AllocateapplyDetailDTO ad : details){
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
    public int allocateApplyHandle(String applyNo){
        int flag = 0;
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailDTO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单(调拨)
            List<BizAllocateTradeorder> list = buildOrderEntityList(details, Constants.PROCESS_TYPE_TRANSFER);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建占用库存和订单占用库存关系
            //获取卖方机构code
            String sellerOrgNo = getSellerOrgNo(list);
            //查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(sellerOrgNo, details);
            Pair<List<BizStockDetail>, List<RelOrdstockOccupy>> pair = buildStockAndRelOrdEntity(details,stockDetails);
            List<BizStockDetail> stockDetailList = pair.getLeft();
            // 构建订单占用库存关系
            List<RelOrdstockOccupy> relOrdstockOccupies = pair.getRight();
            // 保存占用库存
            bizStockDetailDao.batchUpdateStockDetil(stockDetailList);
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
    private List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailDTO> details, String processType){
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
    private BizAllocateTradeorder buildOrderEntity(List<AllocateapplyDetailDTO> details){
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
        for(AllocateapplyDetailDTO bd : details){
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
    private BigDecimal getTatal(List<AllocateapplyDetailDTO> details){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(AllocateapplyDetailDTO bd : details){
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
    private Pair<List<BizStockDetail>, List<RelOrdstockOccupy>>  buildStockAndRelOrdEntity(List<AllocateapplyDetailDTO> details, List<BizStockDetail> stockDetails){
        //订单占用库存关系
        List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
        for(BizStockDetail ad : stockDetails){// 遍历库存
            //占用库存
            Long occupyStockNum = convertStockDetail(details, ad);
            //构建订单占用库存关系
            RelOrdstockOccupy ro = new RelOrdstockOccupy();
            ro.setOrderType("调拨");//订单类型TODO这里肯定是调拨，采购不占用库存，这个字段是不是没有意义
            ro.setDocNo(ad.getTradeNo());//申请单号
            ro.setStockId(ad.getId());//库存id
            ro.setOccupyNum(occupyStockNum);//占用数量
            ro.setOccupyStatus("占用中");//占用状态occupy_status
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
    private List<BizStockDetail> getStockDetailList(String sellerOrgno, List<AllocateapplyDetailDTO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(sellerOrgno, codeList);
    }
    // 获取商品code
    private List<String> getProductList(List<AllocateapplyDetailDTO> details){
        List<String> list = new ArrayList<String>();
        for(AllocateapplyDetailDTO ad : details){
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
    private Long convertStockDetail(List<AllocateapplyDetailDTO> details, BizStockDetail stockDetail){
        Long occupyStockNum = 0l;//占用数量
        for(AllocateapplyDetailDTO ad : details){
            if(ad.getProductNo().equals(stockDetail.getProductNo())){// 找到对应商品
                // 调拨申请数量
                Long applyNum = ad.getApplyNum();
                if(applyNum.intValue() == 0){// 库存有多批次重复的商品，那么下次遍历的时候就不用作处理了
                    continue;
                }
                // 有效库存
                Long validStock = stockDetail.getValidStock();
                if(validStock.intValue() == applyNum.intValue()){// 如果本批次的库存正好等于要调拨的数量
                    validStock = 0l;// 剩余库存为零
                    ad.setApplyNum(0l);//需要调拨的数量也设置为零
                    //记录占用数量
                    occupyStockNum = validStock;
                }
                if(validStock.intValue() < applyNum.intValue()){// 如果本批次的库存缺少
                    validStock = 0l;// 剩余库存为零
                    ad.setApplyNum(applyNum - validStock);// 下次再有库存过来的时候，就会减去剩下的调拨商品数量
                    //记录占用数量
                    occupyStockNum = validStock;
                }
                if(validStock.intValue() > applyNum.intValue()){// 如果本批次的库存充足
                    validStock = applyNum - validStock;// 剩余库存为零
                    ad.setApplyNum(0l);//需要调拨的数量也设置为零,下次再有库存过来的时候就不操作了
                    //记录占用数量
                    occupyStockNum = applyNum;
                }
                // 占用库存
                stockDetail.setOccupyStock(applyNum);
                // 有效库存
                stockDetail.setValidStock(validStock);
                break;
            }
        }
        return occupyStockNum;
    }

}
