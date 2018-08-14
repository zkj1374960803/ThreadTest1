package com.ccbuluo.business.platform.allocateapply.utils;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.constants.StockPlanEnum;
import com.ccbuluo.business.entity.BizAllocateTradeorder;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 申请处理公共方法
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 19:50:43
 */
@Service
public class ApplyHandleUtils {

    @Resource
    private UserHolder userHolder;
    @Resource
    private GenerateDocCodeService generateDocCodeService;
    @Resource
    private BizStockDetailDao bizStockDetailDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @param processType 交易类型
     * @return
     */
    public List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details, String processType){
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
    public BizAllocateTradeorder buildOrderEntity(List<AllocateapplyDetailBO> details){
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

    // 根据卖方机构code获取库存详情
    public List<BizStockDetail> getStockDetailList(String sellerOrgno, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(sellerOrgno, codeList);
    }

    // 获取商品code
    private static List<String> getProductList(List<AllocateapplyDetailBO> details){
        List<String> list = new ArrayList<String>();
        for(AllocateapplyDetailBO ad : details){
            list.add(ad.getProductNo());
        }
        return list;
    }

    /**
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @return
     */
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
            for(AllocateapplyDetailBO ad : details){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
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

    // 根据商品编号查找到某个商品的申请单详情信息
    private BizStockDetail getBizStockDetailByProductNo(List<BizStockDetail> stockDetails, String productNo){
        BizStockDetail applyDetail = new BizStockDetail();
        for(BizStockDetail ad : stockDetails){
            if (productNo.equals(ad.getProductNo())){
                applyDetail = ad;
            }
        }
        return applyDetail;
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
        outPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setCostPrice(ad.getCostPrice());// 成本价
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setPlanOutstocknum(ad.getApplyNum());// 计划出库数量applyNum
//        outPlan.setActualOutstocknum(ad.getApplyNum());// 实际出库数量
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
        inPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        inPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        inPlan.setCostPrice(ad.getCostPrice());// 成本价
        inPlan.setPlanInstocknum(ad.getApplyNum());// 计划入库数量
//        inPlan.setActualInstocknum(ad.getApplyNum());// 实际入库数量
        inPlan.setCompleteStatus(StockPlanEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        inPlan.setCreateTime(new Date());// 创建时间
        inPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return inPlan;
    }
}
