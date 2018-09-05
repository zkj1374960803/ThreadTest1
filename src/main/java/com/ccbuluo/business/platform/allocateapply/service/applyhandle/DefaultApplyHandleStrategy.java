package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:09:03
 */
@Service
public class DefaultApplyHandleStrategy implements ApplyHandleStrategy {

    @Resource
    private UserHolder userHolder;
    @Resource
    private GenerateDocCodeService generateDocCodeService;
    @Resource
    private BizStockDetailDao bizStockDetailDao;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    BizInstockplanDetailDao bizInstockplanDetailDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  申请处理
     * @param ba 申请单
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Override
    public StatusDto applyHandle(BizAllocateApply ba){
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
        return StatusDto.buildSuccessStatusDto("申请撤销成功！");
    }

    /**
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @param applyType 申请类型
     * @param sellerOrgNo 卖方机构
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details, String applyType,List<BizOutstockplanDetail> outStocks,String sellerOrgNo){
        // 构建生成订单（机构1对平台）
        BizAllocateTradeorder purchaserToPlatform = buildOrderEntity(details);// 买方到平台
        BizAllocateTradeorder platformToSeller = buildOrderEntity(details);// 平台到卖方
        BizAllocateTradeorder purchaserToSeller = buildOrderEntity(details);// 买方到卖方
        purchaserToPlatform.setSellerOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 从买方到平台"平台code"
        platformToSeller.setPurchaserOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 从平台到卖方"平台code"
        // 特殊情况处理
        // 计算订单总价
        BigDecimal total = getSellTotal(details);
        // 采购平台直发(从买方到平台)
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)){
            // 采购的时候卖方为供应商（供应商不填为空）
            platformToSeller.setSellerOrgno("");
            platformToSeller = null;
            purchaserToSeller = null;
            purchaserToPlatform.setTotalPrice(total);
        }
        // 平台直发(从买方到平台)
        if(AllocateApplyTypeEnum.DIRECTALLOCATE.toString().equals(applyType)){
            platformToSeller = null;
            purchaserToSeller = null;
            purchaserToPlatform.setTotalPrice(total);
        }
        // 商品换货（不生成订单）
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType)){
            purchaserToPlatform = null;
            platformToSeller = null;
            purchaserToSeller = null;
        }
        // 退货(从买方到平台)
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType)){
            platformToSeller = null;
            purchaserToSeller = null;
            purchaserToPlatform.setTotalPrice(total);
        }
        // 平台调拨（从买方到平台到卖方）
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType)){
            purchaserToSeller = null;
            platformToSeller.setTotalPrice(total);
            BigDecimal costTotal = getCostTatol(outStocks,sellerOrgNo);
            purchaserToPlatform.setTotalPrice(costTotal);

        }
        // 平级调拨(从买方到卖方)
        if(AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType)){
            purchaserToPlatform = null;
            platformToSeller = null;
            purchaserToSeller.setTotalPrice(total);
        }


        List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
        // 从买方到平台
        if(null != purchaserToPlatform){
            list.add(purchaserToPlatform);
        }
        // 从平台到卖方
        if(null != platformToSeller){
            list.add(platformToSeller);
        }
        // 从买方到卖方
        if(null != purchaserToSeller){
            list.add(purchaserToSeller);
        }
        return list;
    }

    /**
     * 构建生成订单
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */

    public BizAllocateTradeorder buildOrderEntity(List<AllocateapplyDetailBO> details){
        BizAllocateTradeorder bt = new BizAllocateTradeorder();
        // 生成订单编号
        StatusDto<String> supplierCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.JY);
        if(!supplierCode.isSuccess()){
            throw new CommonException(supplierCode.getCode(), "生成订单编号失败！");
        }
        bt.setOrderNo(supplierCode.getData());//订单号
        bt.setCreator(userHolder.getLoggedUserId());//处理人
        bt.setCreateTime(new Date());
        bt.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        bt.setOrderStatus(OrderStatusEnum.PAYMENTWAITING.name());//默认待支付
        for(AllocateapplyDetailBO bd : details){
            bt.setApplyNo(bd.getApplyNo());// 申请单编号
            bt.setPurchaserOrgno(bd.getInstockOrgno());//买方机构
            bt.setSellerOrgno(bd.getOutstockOrgno());//卖方机构
            bt.setTradeType(bd.getApplyType());//交易类型
            break;
        }
        return bt;
    }

    /**
     *  计算订单总价(卖方成本价)
     * @param outStocks 卖方出库计划
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getCostTatol(List<BizOutstockplanDetail> outStocks,String sellerOrgNo){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal costPrice = BigDecimal.ZERO;
        for(BizOutstockplanDetail outStock : outStocks){
            // 卖方机构
            if(sellerOrgNo.equals(outStock.getOutOrgno())){
                // 占用商品的数量(卖方计划出库数量)
                BigDecimal occupyNum = BigDecimal.valueOf(outStock.getPlanOutstocknum());
                // 成本价格
                costPrice = outStock.getCostPrice();
                bigDecimal = bigDecimal.add(occupyNum.multiply(costPrice));
            }
        }
        return bigDecimal;
    }

    /**
     *  计算订单总价
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getSellTotal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal sellPrice = BigDecimal.ZERO;
        BigDecimal appNum = BigDecimal.ZERO;
        for(AllocateapplyDetailBO bd : details){
            if(null != bd.getSellPrice()){
                //单价
                sellPrice = bd.getSellPrice();
                // 数量
                appNum = BigDecimal.valueOf(bd.getApplyNum());
            }
            bigDecimal = bigDecimal.add(sellPrice.multiply(appNum));
        }
        return bigDecimal;
    }

    /**
     * 根据卖方机构code获取库存详情
     * @param sellerOrgNo 卖方机构编号
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizStockDetail> getStockDetailList(String sellerOrgNo, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(sellerOrgNo, codeList);
    }

    /**
     * 根据卖方机构code获取问题件库存详情
     * @param sellerOrgNo 卖方机构编号
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizStockDetail> getProblemStockDetailList(String sellerOrgNo, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getProblemStockDetailListByOrgAndProduct(sellerOrgNo, codeList);
    }

    /**
     * 获取商品code
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private static List<String> getProductList(List<AllocateapplyDetailBO> details){
        List<String> list = new ArrayList<String>();
        for(AllocateapplyDetailBO ad : details){
            list.add(ad.getProductNo());
        }
        return list;
    }


    /**
     * 入库计划去重
     * @param inList 入库计划list
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private void distinctInstockplan(List<BizInstockplanDetail> inList){
        for  ( int  i  =   0 ; i  <  inList.size()  -   1 ; i ++ )  {
            for  ( int  j  =  inList.size()  -   1 ; j  >  i; j -- )  {
                if  (inList.get(j).getProductNo().equals(inList.get(i).getProductNo()))  {
                    inList.remove(j);
                }
            }
        }
    }

    /**
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @param applyTypeEnum 申请类型枚举
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, AllocateApplyTypeEnum applyTypeEnum, List<RelOrdstockOccupy> relOrdstockOccupies){
        return null;
    }

    /**
     * 卖方机构出库（机构2）
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void outstockplanSeller(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 卖方机构出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanSeller = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, bd.getProductNo());
                    outstockplanSeller = buildBizOutstockplanDetail(ad, applyType,bd);
                    outstockplanSeller.setStockType(ro.getStockType());// 库存类型(在创建占用关系的时候赋值)
                    outstockplanSeller.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanSeller.setOutOrgno(ad.getOutstockOrgno());// 卖方机构编号
                    outstockplanSeller.setOutRepositoryNo(bd.getRepositoryNo());// 卖方仓库编号（根据机构和商品编号查询的库存）
                    outstockplanSeller.setStockId(bd.getId());// 库存编号id
                    outstockplanSeller.setCostPrice(bd.getCostPrice());// 成本价
                    outList.add(outstockplanSeller);
                    continue;
                }
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
     * 平台出库
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
     public void outstockplanPlatform(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 平台出库计划
        // 根据平台的机构编号查询平台的仓库
        List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        String repositoryNo = "";
        if(null != list && list.size() > 0){
            repositoryNo = list.get(0).getStorehouseCode();
        }
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanPlatform = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, bd.getProductNo());
                    outstockplanPlatform = buildBizOutstockplanDetail(ad, applyType,bd);
                    outstockplanPlatform.setStockType(ro.getStockType());// 库存类型(在创建占用关系的时候赋值)
                    outstockplanPlatform.setOutRepositoryNo(repositoryNo);// 平台仓库编号
                    outstockplanPlatform.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanPlatform.setOutOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台code
                    outstockplanPlatform.setStockId(bd.getId());// 库存编号id
                    outstockplanPlatform.setCostPrice(bd.getCostPrice());// 成本价
                    outList.add(outstockplanPlatform);
                    continue;
                }
            }
        }
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
    public void outstockplanPurchaser(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 买方出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanPurchaser = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, bd.getProductNo());
                    outstockplanPurchaser = buildBizOutstockplanDetail(ad, applyType,bd);
                    outstockplanPurchaser.setStockType(ro.getStockType());// 库存类型(在创建占用关系的时候赋值)
                    outstockplanPurchaser.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanPurchaser.setOutRepositoryNo(bd.getRepositoryNo());// 仓库code
                    outstockplanPurchaser.setOutOrgno(ad.getOutstockOrgno());// 卖方机构code
                    outstockplanPurchaser.setStockId(bd.getId());// 库存编号id
                    outstockplanPurchaser.setCostPrice(bd.getCostPrice());// 成本价
                    outList.add(outstockplanPurchaser);
                    continue;
                }
            }
        }
    }

    /**
     * 平台入库
     * @param details 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void instockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 平台入库
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
            // 平台入库计划
            instockplanPlatform = buildBizInstockplanDetail(ad, applyType);
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

    /**
     * 买方入库
     * @param details 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public void instockplanPurchaser(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 买入方入库计划
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPurchaser = new BizInstockplanDetail();
            instockplanPurchaser = buildBizInstockplanDetail(ad, applyType);
            instockplanPurchaser.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型（只有平台是采购，机构是调拨）
            instockplanPurchaser.setInstockRepositoryNo(ad.getInRepositoryNo());// 入库仓库编号
            instockplanPurchaser.setInstockOrgno(ad.getInstockOrgno());// 买入机构编号
            inList.add(instockplanPurchaser);
        }
    }

    /**
     * 构建出库计划
     * @param ad 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    protected BizOutstockplanDetail buildBizOutstockplanDetail(AllocateapplyDetailBO ad, String applyType,BizStockDetail bd){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        // 调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType)  || AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.BARTER.toString());// 交易类型
        }
        // 退货
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.REFUND.toString());// 交易类型
        }

        outPlan.setProductNo(ad.getProductNo());// 商品编号
        outPlan.setProductType(ad.getProductType());// 商品类型
        outPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(ad.getProductName());// 商品名称
        outPlan.setProductUnit(ad.getUnit());// 商品计量单位
        outPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(bd.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setPlanStatus(StockPlanStatusEnum.DOING.toString());// 出库计划的状态（计划执行中）
        outPlan.preInsert(userHolder.getLoggedUserId());
        outPlan.setRemark(ad.getRemark());// 备注
        return outPlan;
    }

    /**
     * 构建出库计划(入库之后的回调用)
     * @param in 入库计划
     * @param applyType 申请类型
     * @param details 申请单详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(BizInstockplanDetail in, String applyType, List<AllocateapplyDetailBO> details){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        // 调拨和平台采购都属于调拨出库
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) || AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)){
            outPlan.setOutstockType(OutstockTypeEnum.TRANSFER.toString());// 出库类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.BARTER.toString());// 出库类型
        }
        // 退货
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.REFUND.toString());// 出库类型
        }
        outPlan.setProductNo(in.getProductNo());// 商品编号
        outPlan.setProductType(in.getProductType());// 商品类型
        outPlan.setProductCategoryname(in.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(in.getProductName());// 商品名称
        outPlan.setProductUnit(in.getProductUnit());// 商品计量单位
        outPlan.setTradeNo(in.getTradeNo());// 交易批次号（申请单编号）
        outPlan.setSupplierNo(in.getSupplierNo());//供应商编号
        AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, in.getProductNo());
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setStockType(ad.getStockType());// 库存类型(在创建占用关系的时候赋值)
        outPlan.setPlanStatus(StockPlanStatusEnum.DOING.toString());// 出库计划的状态（计划执行中）
        outPlan.preInsert(userHolder.getLoggedUserId());
        outPlan.setStockType(in.getStockType());// 库存类型
        outPlan.setRemark(in.getRemark());// 备注
        return outPlan;
    }

    /**
     * 构建入库计划
     * @param ad 申请详情
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    protected BizInstockplanDetail buildBizInstockplanDetail(AllocateapplyDetailBO ad, String applyType){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();

        inPlan.setProductNo(ad.getProductNo());// 商品编号
        inPlan.setProductType(ad.getProductType());// 商品类型
        inPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        inPlan.setProductName(ad.getProductName());// 商品名称
        inPlan.setProductUnit(ad.getUnit());// 商品计量单位
        inPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        inPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        inPlan.setCostPrice(ad.getSellPrice());// 成本价(入库的成本价是详单的销售价)
        inPlan.setPlanInstocknum(ad.getApplyNum());// 计划入库数量
//        inPlan.setActualInstocknum(ad.getApplyNum());// 实际入库数量
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.preInsert(userHolder.getLoggedUserId());
        inPlan.setStockType(ad.getStockType());// 库存类型
        inPlan.setRemark(ad.getRemark());// 备注
        // 调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 采购
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)){
            inPlan.setInstockType(InstockTypeEnum.PURCHASE.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType)){
            inPlan.setInstockType(InstockTypeEnum.BARTER.toString());// 交易类型
            inPlan.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());// 库存类型 （问题件申请机构入库的时候应该是有效库存）
            inPlan.setCostPrice(BigDecimal.ZERO);// 成本价(退货和换货的成本价是零)
        }
        // 退货
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.BARTER.toString());// 交易类型
            inPlan.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());// 库存类型 （问题件平台入库的时候应该是问题库存）
            inPlan.setCostPrice(BigDecimal.ZERO);// 成本价(退货和换货的成本价是零)
        }
        return inPlan;
    }

//    /**
//     * 构建占用库存和订单占用库存关系
//     * @param details 申请单详情
//     * @param stockDetails 库存列表
//     * @param applyType 申请类型
//     * @author weijb
//     * @date 2018-08-08 17:55:41
//     */
//    public Pair<List<BizStockDetail>, List<RelOrdstockOccupy>>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyType){
//        //订单占用库存关系
//        List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
//        Map<String,Long> map = getProductStock(details);
//        for(BizStockDetail ad : stockDetails){// 遍历库存
//            Long applyNum = map.get(ad.getProductNo());
//            if(applyNum.intValue() == 0){// 说明申请商品的数据已经出库完成了
//                continue;
//            }
//            //占用库存
//            Long occupyStockNum = convertStockDetail(details, ad,map);
//            //构建订单占用库存关系
//            RelOrdstockOccupy ro = new RelOrdstockOccupy();
//            ro.setOrderType(applyType);//订单类型(调拨，采购不占用库存)
//            Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> ad.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
//            if (applyFilter.isPresent()) {
//                AllocateapplyDetailBO applyDetail = applyFilter.get();
//                ro.setDocNo(applyDetail.getApplyNo());//申请单号
//            }
//            ro.setStockId(ad.getId());//库存id
//            ro.setOccupyNum(occupyStockNum);//占用数量
//            ro.setOccupyStatus(StockPlanStatusEnum.DOING.toString());//占用状态occupy_status
//            Date time = new Date();
//            ro.setOccupyStarttime(time);//占用开始时间
//            ro.preInsert(userHolder.getLoggedUserId());
//            relOrdstockOccupies.add(ro);
//        }
//        return Pair.of(stockDetails, relOrdstockOccupies);
//    }

    private Map<String,Long> getProductStock(List<AllocateapplyDetailBO> details){
        Map<String, Long> map = new HashMap<String, Long>();
        for(AllocateapplyDetailBO ab : details){
            map.put(ab.getProductNo(),ab.getApplyNum());
        }
        return map;
    }
    /**
     * 复制list
     * @param
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private List<AllocateapplyDetailBO> copyList(List<AllocateapplyDetailBO> details){
        List<AllocateapplyDetailBO> detailsCopy = new ArrayList<AllocateapplyDetailBO>();
        for (AllocateapplyDetailBO ab : details){
            AllocateapplyDetailBO bo = new AllocateapplyDetailBO();
            BeanUtils.copyProperties(bo, ab);
            detailsCopy.add(bo);
        }
        return detailsCopy;
    }

//    /**
//     * 遍历库存并转换可用库存
//     * @param details 申请单详情
//     * @param stockDetail 库存对象
//     * @author weijb
//     * @date 2018-08-08 17:55:41
//     */
//    private Long convertStockDetail(List<AllocateapplyDetailBO> details, BizStockDetail stockDetail, Map<String,Long> map){
//        Long occupyStockNum = 0L;//占用数量
//        for(AllocateapplyDetailBO ad : details){
//            if(ad.getProductNo().equals(stockDetail.getProductNo())){// 找到对应商品
//                // 调拨申请数量
//                Long applyNum = map.get(ad.getProductNo());
//                // 有效库存
//                Long validStock = stockDetail.getValidStock();
//                if(validStock.intValue() == applyNum.intValue()){// 如果本批次的库存正好等于要调拨的数量
//                    validStock = 0L;// 剩余库存为零
//                    map.put(ad.getProductNo(), 0L);//需要调拨的数量也设置为零
//                    //记录占用数量
//                    occupyStockNum = validStock;
//                }
//                if(validStock.intValue() < applyNum.intValue()){// 如果本批次的库存缺少
//                    map.put(ad.getProductNo(), applyNum - validStock);// 下次再有库存过来的时候，就会减去剩下的调拨商品数量
//                    //记录占用数量
//                    occupyStockNum = validStock;// 占用了全部可用库存
//                    validStock = 0L;// 剩余库存为零
//                }
//                if(validStock.intValue() > applyNum.intValue()){// 如果本批次的库存充足
//                    validStock = validStock - applyNum;// 剩余库存为零
//                    map.put(ad.getProductNo(), 0L);//需要调拨的数量也设置为零,下次再有库存过来的时候就不操作了
//                    //记录占用数量
//                    occupyStockNum = applyNum;
//                }
//                // 占用库存
//                stockDetail.setOccupyStock(occupyStockNum);
//                // 有效库存
//                stockDetail.setValidStock(validStock);
//                break;
//            }
//        }
//        return occupyStockNum;
//    }

    /**
     * 根据订单获取商品所在仓库所属的机构编号
     * @param ba 申请开单
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public String getProductOrgNo(BizAllocateApply ba){
        String sellerOrgno = "";
        // 平台调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级调拨（服务间的调拨）
        if(AllocateApplyTypeEnum.SAMELEVEL.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级直调
        if(AllocateApplyTypeEnum.DIRECTALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM;//  平台的机构编号
        }
        // 商品换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 退货
        if(AllocateApplyTypeEnum.REFUND.toString().equals(ba.getApplyType())){
            sellerOrgno =  ba.getApplyorgNo();//  发起申请的机构编号
        }
        return sellerOrgno;
    }

    /**
     * 根据申请单编号查询订单占用库存关系表
     * @param list  库存和占用关系 list
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizStockDetail> buildBizStockDetail(List<RelOrdstockOccupy> list){
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

    /**
     * 获取库存详情的ids
     * @param list 库存和占用关系 list
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private List<Long> getStockDtailIds(List<RelOrdstockOccupy> list){
        List<Long> slist = new ArrayList<Long>();
        for(RelOrdstockOccupy r : list){
            slist.add(r.getStockId());
        }
        return slist;
    }
    /**
     *  入库之后回调事件
     * @param ba 申请单
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto platformInstockCallback(BizAllocateApply ba){
        String applyNo = ba.getApplyNo();
        String applyType = ba.getApplyType();
        // 根据申请单获取申请单详情
        List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
        if(null == details || details.size() == 0){
            return StatusDto.buildFailureStatusDto("申请单为空！");
        }
        //获取卖方机构code
        String productOrgNo = getProductOrgNo(ba);
        //查询库存列表
        List<BizStockDetail> stockDetails = bizStockDetailDao.getStockDetailListByApplyNo(applyNo);
        if(null == stockDetails || stockDetails.size() == 0){
            return StatusDto.buildFailureStatusDto("库存列表为空！");
        }
        List<BizOutstockplanDetail> outstockplans = buildPlatformOutstockplan(ba, details, stockDetails);
        // 构建平台出库计划并保存(特殊处理，根据平台的入库计划来构建)
        convertStockDetail(stockDetails);
        // 保存占用库存
        int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetails);
        // 更新失败
        if(flag == 0){
            throw new CommonException("0", "更新占用库存失败！");
        }
        // 批量保存出库计划详情
        bizOutstockplanDetailDao.batchOutstockplanDetail(outstockplans);
        return StatusDto.buildSuccessStatusDto("出库计划生成成功");
    }

    /**
     *  转换平台的出库计划
     * @param
     * @exception 
     * @return 
     * @author weijb
     * @date 2018-08-20 18:05:05
     */
    private void convertStockDetail(List<BizStockDetail> stockDetailList){
        for(BizStockDetail bd : stockDetailList){
            bd.setOccupyStock(bd.getValidStock());// 把占用库存设置为可用库存
            bd.setValidStock(0L);// 把可用库存设置为零
        }
    }

    /**
     *  构建平台的出库计划
     * @param ba 申请单
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-20 17:12:43
     */
    private List<BizOutstockplanDetail> buildPlatformOutstockplan(BizAllocateApply ba, List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails){
        List<BizOutstockplanDetail> outstockplanDetails = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> instockplanDetails = bizInstockplanDetailDao.getInstockplanDetailByApplyNo(ba.getApplyNo());
        BizStockDetail bizStockDetail = new BizStockDetail();
        for(BizInstockplanDetail in : instockplanDetails){
            Optional<BizStockDetail> stockFilter = stockDetails.stream() .filter(stockDetail -> in.getId().equals(stockDetail.getInstockPlanid())) .findFirst();
            if (stockFilter.isPresent()) {
                bizStockDetail = stockFilter.get();
            }
            BizOutstockplanDetail outstockplanPlatform = new BizOutstockplanDetail();
            outstockplanPlatform = buildBizOutstockplanDetail(in, ba.getApplyType(), details);
            outstockplanPlatform.setOutRepositoryNo(bizStockDetail.getRepositoryNo());// 平台仓库编号
            outstockplanPlatform.setPlanOutstocknum(in.getPlanInstocknum());// 计划出库数量applyNum
            outstockplanPlatform.setOutOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台code
            outstockplanPlatform.setStockId(bizStockDetail.getId());// 库存编号id
            outstockplanPlatform.setCostPrice(in.getCostPrice());// 成本价
            outstockplanDetails.add(outstockplanPlatform);
        }
        return outstockplanDetails;
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
    public List<BizStockDetail>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyType, List<RelOrdstockOccupy> relOrdstockOccupies){
        for(AllocateapplyDetailBO detail : details){
            // 申请个数
            Long applyNum = detail.getApplyNum();
            // 申请商品的数据已经出库完成了
            if(applyNum.intValue() == 0){
                continue;
            }
            //占用库存
            convertStockDetail(stockDetails,detail,applyNum,applyType, relOrdstockOccupies);
        }
        Map<String,Long> map = getProductStock(details);
        return stockDetails;
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

    /**
     *  获取详单的库存类型（正常件或是问题件）
     * @param details 申请单详细
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-27 17:11:35
     */
    public String getStockType(List<AllocateapplyDetailBO> details){
        Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(applyDetail.getStockType())) .findFirst();
        if (applyFilter.isPresent()) {
            return BizStockDetail.StockTypeEnum.VALIDSTOCK.name();
        }else {
            return BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name();
        }
    }
}
