package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
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
    public int applyHandle(BizAllocateApply ba){
        return 0;
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
     * @return
     */
    public List<BizAllocateTradeorder> buildOrderEntityList(List<AllocateapplyDetailBO> details, String applyType){
        // 构建生成订单（机构1对平台）
        BizAllocateTradeorder bizAllocateTradeorder1 = buildOrderEntity(details);
        BizAllocateTradeorder bizAllocateTradeorder2 = buildOrderEntity(details);
        bizAllocateTradeorder1.setSellerOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从买方到平台"平台code"
        bizAllocateTradeorder2.setPurchaserOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从平台到卖方"平台code"

        // 特殊情况处理
        // 采购
        if(AllocateApplyEnum.PURCHASE.toString().equals(applyType)){
            // 采购的时候卖方为供应商（供应商不填为空）
            bizAllocateTradeorder2.setSellerOrgno("");
        }
        // 平级直调
        if(AllocateApplyEnum.DIRECTALLOCATE.toString().equals(applyType)){
            // 直调是没有采购订单的
            bizAllocateTradeorder2 = null;
        }
        // 商品退换
        if(AllocateApplyEnum.BARTER.toString().equals(applyType)){
            // 从买方到平台
            bizAllocateTradeorder2 = null;
        }
        // 退款
        if(AllocateApplyEnum.REFUND.toString().equals(applyType)){
            // 从买方到平台
            bizAllocateTradeorder2 = null;
        }

        List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
        // 从买方到平台
        if(null != bizAllocateTradeorder1){
            list.add(bizAllocateTradeorder1);
        }
        // 从平台到卖方
        if(null != bizAllocateTradeorder2){
            list.add(bizAllocateTradeorder2);
        }
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
    /**
     *  计算订单总价
     */
    private BigDecimal getTatal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(AllocateapplyDetailBO bd : details){
            bigDecimal.add(bd.getSellPrice());
        }
        return bigDecimal;
    }

    /**
     *  根据卖方机构code获取库存详情
     */
    public List<BizStockDetail> getStockDetailList(String productOrgNo, List<AllocateapplyDetailBO> details){
        // 根据卖方code和商品code（list）查出库存列表
        List<String> codeList = getProductList(details);
        return bizStockDetailDao.getStockDetailListByOrgAndProduct(productOrgNo, codeList);
    }

    /**
     * 获取商品code
     */
    private static List<String> getProductList(List<AllocateapplyDetailBO> details){
        List<String> list = new ArrayList<String>();
        for(AllocateapplyDetailBO ad : details){
            list.add(ad.getProductNo());
        }
        return list;
    }

    /**
     *  入库计划去重
     * @param
     * @return
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
     * @param applyType 申请类型
     * @return
     */
    public Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyType){
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        // 卖方机构出库（机构2）
        BizOutstockplanDetail outstockplanDetail2 = new BizOutstockplanDetail();
        // 平台出库
        BizOutstockplanDetail outstockplanDetail = new BizOutstockplanDetail();
        // 买方出库
        BizOutstockplanDetail outstockplanDetail1 = new BizOutstockplanDetail();
        // 平台入库
        BizInstockplanDetail instockplanDetail = new BizInstockplanDetail();
        // 买方入库
        BizInstockplanDetail instockplanDetail1 = new BizInstockplanDetail();
        for(AllocateapplyDetailBO ad : details){
            // 采购
            if(AllocateApplyEnum.PURCHASE.toString().equals(applyType)){
                // 平台出库计划
//                BizOutstockplanDetail outstockplanDetail1 = buildBizOutstockplanDetail(ad, processType);
//                outstockplanDetail1.setStockId("这个批次库存id从哪里来TODO");// 批次库存id(平台的库存)
//                outstockplanDetail1.setOutRepositoryNo(BusinessPropertyHolder.TOP_SERVICECENTER);// 出库仓库编号（平台code）
                // 平台入库计划
                instockplanDetail(instockplanDetail,ad, applyType);
                // 买入方入库计划
                instockplanDetail1(instockplanDetail1,ad, applyType);
                inList.add(instockplanDetail);
                inList.add(instockplanDetail1);
            }
            // 平台调拨
            if(AllocateApplyEnum.PLATFORMALLOCATE.toString().equals(applyType)){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
                // 卖方机构出库计划
                outstockplanDetail2(outstockplanDetail2,ad,bd, applyType);
                outList.add(outstockplanDetail2);
                // 平台出库计划
                outstockplanDetail(outstockplanDetail,ad, bd,applyType);
                outList.add(outstockplanDetail);
                // 平台入库计划
                instockplanDetail(instockplanDetail,ad, applyType);
                inList.add(instockplanDetail);
                // 买方入库计划
                instockplanDetail1(instockplanDetail1,ad, applyType);
                inList.add(instockplanDetail1);
                distinctInstockplan(inList);
            }
            // 平级调拨（服务间的调拨）
            if(AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType)){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
                // 卖方机构出库计划
                outstockplanDetail2(outstockplanDetail2,ad,bd, applyType);
                outList.add(outstockplanDetail2);
                // 买方入库计划
                instockplanDetail1(instockplanDetail1,ad, applyType);
                inList.add(instockplanDetail1);
                distinctInstockplan(inList);
            }
            // 直调
            if(AllocateApplyEnum.DIRECTALLOCATE.toString().equals(applyType)){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
                // 平台出库计划
                outstockplanDetail(outstockplanDetail,ad,bd, applyType);
                outList.add(outstockplanDetail);// 平台出库
                // 买入方入库计划
                instockplanDetail1(instockplanDetail1,ad, applyType);
                inList.add(instockplanDetail1);// 机构1入库
                distinctInstockplan(inList);
            }
            // 商品退换
            if(AllocateApplyEnum.BARTER.toString().equals(applyType)){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
                // 买方出库
                outstockplanDetail1(outstockplanDetail1,ad,bd, applyType);
                outList.add(outstockplanDetail1);
                // 平台入库
                instockplanDetail(instockplanDetail,ad, applyType);
                inList.add(instockplanDetail);
                // 平台出库
                outstockplanDetail(outstockplanDetail,ad, bd,applyType);
                outList.add(outstockplanDetail);
                // 买方入库
                instockplanDetail1(instockplanDetail1,ad, applyType);
                inList.add(instockplanDetail1);
            }
            // 退款
            if(AllocateApplyEnum.REFUND.toString().equals(applyType)){
                BizStockDetail bd = getBizStockDetailByProductNo(stockDetails, ad.getProductNo());
                // 买方出库
                outstockplanDetail1(outstockplanDetail1,ad,bd, applyType);
                outList.add(outstockplanDetail1);
                // 平台入库
                instockplanDetail(instockplanDetail,ad, applyType);
                inList.add(instockplanDetail);
            }
        }

        return Pair.of(outList, inList);
    }
    /**
     * 卖方机构出库（机构2）
      */
    private void outstockplanDetail2(BizOutstockplanDetail outstockplanDetail2, AllocateapplyDetailBO ad,BizStockDetail bd, String applyType){
        // 卖方机构出库计划
        outstockplanDetail2 = buildBizOutstockplanDetail(ad, applyType);
        outstockplanDetail2.setOutRepositoryNo(bd.getRepositoryNo());// 卖方仓库编号（根据机构和商品编号查询的库存）
        outstockplanDetail2.setStockId(bd.getId());// 库存编号id
        outstockplanDetail2.setCostPrice(bd.getCostPrice());// 成本价
    }
    /**
     * 平台出库
     */
    private void outstockplanDetail(BizOutstockplanDetail outstockplanDetail, AllocateapplyDetailBO ad,BizStockDetail bd, String applyType){
        // 平台出库计划
        outstockplanDetail = buildBizOutstockplanDetail(ad, applyType);
        // 根据平台的no查询平台的仓库
        List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.TOP_SERVICECENTER);
        String repositoryNo = "";
        if(null != list && list.size() > 0){
            repositoryNo = list.get(0).getStorehouseCode();
        }
        outstockplanDetail.setOutRepositoryNo(repositoryNo);// 平台仓库编号
        outstockplanDetail.setOutOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台code
        outstockplanDetail.setStockId(bd.getId());// 库存编号id
        outstockplanDetail.setCostPrice(bd.getCostPrice());// 成本价
    }
    /**
     * 买方出库
     */
    private void outstockplanDetail1(BizOutstockplanDetail outstockplanDetail1, AllocateapplyDetailBO ad,BizStockDetail bd, String applyType){
        // 买方出库计划
        outstockplanDetail1 = buildBizOutstockplanDetail(ad, applyType);
        outstockplanDetail1.setOutRepositoryNo(bd.getRepositoryNo());// 仓库code
        outstockplanDetail1.setOutOrgno(ad.getInstockOrgno());// 买方机构code
        outstockplanDetail1.setStockId(bd.getId());// 库存编号id
        outstockplanDetail1.setCostPrice(bd.getCostPrice());// 成本价
    }
    /**
     * 平台入库
     */
    private void instockplanDetail(BizInstockplanDetail instockplanDetail, AllocateapplyDetailBO ad, String applyType){
        // 平台入库计划
        instockplanDetail = buildBizInstockplanDetail(ad, applyType);
        // 根据平台的no查询平台的仓库
        List<QueryStorehouseDTO> list = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(BusinessPropertyHolder.TOP_SERVICECENTER);
        String repositoryNo = "";
        if(null != list && list.size() > 0){
            repositoryNo = list.get(0).getStorehouseCode();
        }
        instockplanDetail.setInstockRepositoryNo(repositoryNo);// 平台仓库编号
        instockplanDetail.setInstockOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 平台机构编号
    }
    /**
     * 买方入库
     */
    private void instockplanDetail1(BizInstockplanDetail instockplanDetail1, AllocateapplyDetailBO ad, String applyType){
        // 买入方入库计划
        instockplanDetail1 = buildBizInstockplanDetail(ad, applyType);
        instockplanDetail1.setInstockRepositoryNo(ad.getInRepositoryNo());// 入库仓库编号
        instockplanDetail1.setInstockOrgno(ad.getInstockOrgno());// 买入机构编号
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
     * @param applyType 申请类型
     * @return
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(AllocateapplyDetailBO ad, String applyType){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        // 调拨
        if(AllocateApplyEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
            outPlan.setOutstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 采购
        if(AllocateApplyEnum.PURCHASE.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
            outPlan.setOutstockType(InstockTypeEnum.PURCHASE.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyEnum.BARTER.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
            outPlan.setOutstockType(InstockTypeEnum.BARTER.toString());// 交易类型
        }

        outPlan.setProductNo(ad.getProductNo());// 商品编号
        outPlan.setProductType(ad.getProductType());// 商品类型
        outPlan.setProductCategoryname(ad.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(ad.getProductName());// 商品名称
        outPlan.setProductUnit(ad.getUnit());// 商品计量单位
        outPlan.setTradeNo(String.valueOf(ad.getApplyNo()));// 交易批次号（申请单编号）
        outPlan.setSupplierNo(ad.getSupplierNo());//供应商编号
        outPlan.setApplyDetailId(ad.getId());//申请单详单id
//        outPlan.setCostPrice(ad.getCostPrice());// 成本价
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
     * @param applyType 申请类型
     * @return
     */
    private BizInstockplanDetail buildBizInstockplanDetail(AllocateapplyDetailBO ad, String applyType){
        BizInstockplanDetail inPlan = new BizInstockplanDetail();
        // 调拨
        if(AllocateApplyEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.TRANSFER.toString());// 交易类型
        }
        // 采购
        if(AllocateApplyEnum.PURCHASE.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
            inPlan.setInstockType(InstockTypeEnum.PURCHASE.toString());// 交易类型
        }
        // 换货
        if(AllocateApplyEnum.BARTER.toString().equals(applyType) || AllocateApplyEnum.SERVICEALLOCATE.toString().equals(applyType) ){
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
        inPlan.setCompleteStatus(StockPlanEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.setCreator(userHolder.getLoggedUserId());// 创建人
        inPlan.setCreateTime(new Date());// 创建时间
        inPlan.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);//删除标识
        return inPlan;
    }

    /**
     * 构建占用库存和订单占用库存关系
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @param applyNo 申请类型
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    public Pair<List<BizStockDetail>, List<RelOrdstockOccupy>>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyNo){
        //订单占用库存关系
        List<RelOrdstockOccupy> relOrdstockOccupies = new ArrayList<RelOrdstockOccupy>();
        List<AllocateapplyDetailBO> detailBOS = copyList(details);
        for(BizStockDetail ad : stockDetails){// 遍历库存
            //占用库存
            Long occupyStockNum = convertStockDetail(detailBOS, ad);
            //构建订单占用库存关系
            RelOrdstockOccupy ro = new RelOrdstockOccupy();
            ro.setOrderType(applyNo);//订单类型(调拨，采购不占用库存)
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
     *根据订单获取商品所在仓库所属的机构编号
     */
    public String getProductOrgNo(BizAllocateApply ba){
        String sellerOrgno = "";
        // 平台调拨
        if(AllocateApplyEnum.PLATFORMALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级调拨（服务间的调拨）
        if(AllocateApplyEnum.SERVICEALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级直调
        if(AllocateApplyEnum.DIRECTALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = BusinessPropertyHolder.TOP_SERVICECENTER;//  平台的机构编号
        }
        // 商品退换
        if(AllocateApplyEnum.BARTER.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 退款
        if(AllocateApplyEnum.REFUND.toString().equals(ba.getApplyType())){
            sellerOrgno =  ba.getApplyorgNo();//  发起申请的机构编号
        }
        return sellerOrgno;
    }
}
