package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public int cancelApply(String applyNo){
        return 0;
    }

    /**
     * 构建订单list用于批量保存
     * @param details 申请单详情
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details, String applyType){
        // 构建生成订单（机构1对平台）
        BizAllocateTradeorder purchaserToPlatform = buildOrderEntity(details);
        BizAllocateTradeorder platformToSeller = buildOrderEntity(details);
        purchaserToPlatform.setSellerOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 从买方到平台"平台code"
        platformToSeller.setPurchaserOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 从平台到卖方"平台code"

        // 特殊情况处理
        // 采购
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)){
            // 采购的时候卖方为供应商（供应商不填为空）
            platformToSeller.setSellerOrgno("");
        }
        // 平台直发
        if(AllocateApplyTypeEnum.DIRECTALLOCATE.toString().equals(applyType)){
            // 直调是没有采购订单的
            platformToSeller = null;
        }
        // 商品退换
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType)){
            // 从买方到平台
            platformToSeller = null;
        }
        // 退款
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType)){
            // 从买方到平台
            platformToSeller = null;
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
        StatusDto<String> supplierCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
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
        // 计算订单总价
        BigDecimal total = getTatal(details);
        bt.setTotalPrice(total);
        return bt;
    }

    /**
     *  计算订单总价
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getTatal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(AllocateapplyDetailBO bd : details){
            bigDecimal.add(bd.getSellPrice());
        }
        return bigDecimal;
    }

    /**
     * 根据卖方机构code获取库存详情
     * @param productOrgNo 商品编号
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public List<BizStockDetail> getStockDetailList(String productOrgNo, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(productOrgNo, codeList);
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
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        switch (applyTypeEnum){
            case PURCHASE:    // 采购
                //  给入库之后回调出库计划用
                if(null != stockDetails && null != relOrdstockOccupies){
                    outstockplanPlatform(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
                }
                // 平台入库计划
                instockplanPlatform(inList,details, AllocateApplyTypeEnum.PURCHASE.toString());
                // 买入方入库计划
                instockplanPurchaser(inList,details, AllocateApplyTypeEnum.PURCHASE.toString());
                break;
            case PLATFORMALLOCATE:    // 平台调拨
                // 卖方机构出库计划
                outstockplanSeller(outList, relOrdstockOccupies,stockDetails, details, AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
                // 平台出库计划
                outstockplanPlatform(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
                // 平台入库计划
                instockplanPlatform(inList,details, AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
                // 买方入库计划
                instockplanPurchaser(inList,details, AllocateApplyTypeEnum.PLATFORMALLOCATE.toString());
                break;
            case SAMELEVEL:    // 平级调拨（服务间的调拨）
                // 卖方机构出库计划
                outstockplanSeller(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.SAMELEVEL.toString());
                // 买方入库计划
                instockplanPurchaser(inList,details, AllocateApplyTypeEnum.SAMELEVEL.toString());
                break;
            case DIRECTALLOCATE:    // 直调
                // 平台出库计划
                outstockplanPlatform(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.DIRECTALLOCATE.toString());
                // 买入方入库计划
                instockplanPurchaser(inList,details, AllocateApplyTypeEnum.DIRECTALLOCATE.toString());
                break;
            case BARTER:    // 商品退换
                // 买方出库
                outstockplanPurchaser(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.BARTER.toString());
                // 平台入库
                instockplanPlatform(inList,details, AllocateApplyTypeEnum.BARTER.toString());
                // 平台出库
                outstockplanPlatform(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.BARTER.toString());
                // 买方入库
                instockplanPurchaser(inList,details, AllocateApplyTypeEnum.BARTER.toString());
                break;
            case REFUND:    //  退款
                // 买方出库
                outstockplanPurchaser(outList,relOrdstockOccupies,stockDetails,details, AllocateApplyTypeEnum.REFUND.toString());
                // 平台入库
                instockplanPlatform(inList,details, AllocateApplyTypeEnum.REFUND.toString());
                break;
            default:
                logger.error(applyTypeEnum.toString()+"出现了未知申请类型！");
                break;
        }
        return Pair.of(outList, inList);
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
    private void outstockplanSeller(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 卖方机构出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanDetail2 = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, bd.getProductNo());
                    outstockplanDetail2 = buildBizOutstockplanDetail(ad, applyType);
                    outstockplanDetail2.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    outstockplanDetail2.setOutOrgno(ad.getOutstockOrgno());// 卖方机构编号
                    outstockplanDetail2.setOutRepositoryNo(bd.getRepositoryNo());// 卖方仓库编号（根据机构和商品编号查询的库存）
                    outstockplanDetail2.setStockId(bd.getId());// 库存编号id
                    outstockplanDetail2.setCostPrice(bd.getCostPrice());// 成本价
                    outList.add(outstockplanDetail2);
                    continue;
                }
            }
        }
    }
    /**
     *
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
    private void outstockplanPlatform(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
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
                    outstockplanPlatform = buildBizOutstockplanDetail(ad, applyType);
                    outstockplanPlatform.setOutRepositoryNo(repositoryNo);// 平台仓库编号
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
    private void outstockplanPurchaser(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details, String applyType){
        // 买方出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanPurchaser = new BizOutstockplanDetail();
            for(BizStockDetail bd : stockDetails){
                if(ro.getStockId().intValue() == bd.getId().intValue()){// 关系库存批次id和库存批次id相等
                    AllocateapplyDetailBO ad = getAllocateapplyDetailBO(details, bd.getProductNo());
                    outstockplanPurchaser = buildBizOutstockplanDetail(ad, applyType);
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
    private void instockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 平台入库
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
            // 平台入库计划
            instockplanPlatform = buildBizInstockplanDetail(ad, applyType);
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
    private void instockplanPurchaser(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details, String applyType){
        // 买入方入库计划
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPurchaser = new BizInstockplanDetail();
            instockplanPurchaser = buildBizInstockplanDetail(ad, applyType);
            instockplanPurchaser.setInstockRepositoryNo(ad.getInRepositoryNo());// 入库仓库编号
            instockplanPurchaser.setInstockOrgno(ad.getInstockOrgno());// 买入机构编号
            inList.add(instockplanPurchaser);
        }
    }

    /**
     * 根据商品编号查找到某个商品的申请单详情信息
     * @param stockDetails 库存list
     * @param productNo 商品编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
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
     * @param ad 申请详细
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(AllocateapplyDetailBO ad, String applyType){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        // 调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.BARTER.toString());// 交易类型
        }
        // 退款
        if(AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            outPlan.setOutstockType(OutstockTypeEnum.REFUND.toString());// 交易类型
        }

        outPlan.setProductNo(ad.getProductNo());// 商品编号
        outPlan.setProductType(ad.getProductType());// 商品类型
        outPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(ad.getProductName());// 商品名称
        outPlan.setProductUnit(ad.getUnit());// 商品计量单位
        outPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
        outPlan.setSalesPrice(ad.getSellPrice());// 销售价
        outPlan.setPlanStatus(StockPlanStatusEnum.DOING.toString());// 出库计划的状态（计划执行中）
        outPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        outPlan.setCreateTime(new Date());// 创建时间
        outPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        outPlan.setStockType(ad.getStockType());// 库存类型
        outPlan.setRemark(ad.getRemark());// 备注
        return outPlan;
    }

    /**
     * 构建入库计划
     * @param ad 申请详情
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BizInstockplanDetail buildBizInstockplanDetail(AllocateapplyDetailBO ad, String applyType){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        // 调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 采购
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.PURCHASE.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.BARTER.toString());// 交易类型
        }
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
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        inPlan.setCreateTime(new Date());// 创建时间
        inPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        inPlan.setStockType(ad.getStockType());// 库存类型
        inPlan.setRemark(ad.getRemark());// 备注
        return inPlan;
    }

    /**
     * 构建占用库存和订单占用库存关系
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @param applyType 申请类型
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    public Pair<List<BizStockDetail>, List<RelOrdstockOccupy>>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyType){
        //订单占用库存关系
        List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
        List<AllocateapplyDetailBO> detailBOS = copyList(details);
        for(BizStockDetail ad : stockDetails){// 遍历库存
            //占用库存
            Long occupyStockNum = convertStockDetail(detailBOS, ad);
            //构建订单占用库存关系
            RelOrdstockOccupy ro = new RelOrdstockOccupy();
            ro.setOrderType(applyType);//订单类型(调拨，采购不占用库存)
            ro.setDocNo(ad.getTradeNo());//申请单号
            ro.setStockId(ad.getId());//库存id
            ro.setOccupyNum(occupyStockNum);//占用数量
            ro.setOccupyStatus(StockPlanStatusEnum.DOING.toString());//占用状态occupy_status
            Date time = new Date();
            ro.setOccupyStarttime(time);//占用开始时间
            ro.setCreator(userHolder.getLoggedUserId());//创建人
            ro.setCreateTime(time);//创建时间
            ro.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
            relOrdstockOccupies.add(ro);
        }
        return Pair.of(stockDetails, relOrdstockOccupies);
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
            try {
                BeanUtils.copyProperties(bo, ab);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            detailsCopy.add(bo);
        }
        return detailsCopy;
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
        // 商品退换
        if(AllocateApplyTypeEnum.BARTER.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 退款
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
}
