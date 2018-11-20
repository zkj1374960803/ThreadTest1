package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.outstock.dto.ProductStockDTO;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 换货出入库回调
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:42:46
 */
@Service
public class BarterStockInOutCallBack implements StockInOutCallBack{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Autowired
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Autowired
    private BizStockDetailDao bizStockDetailDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Autowired
    InOutCallBackService inOutCallBackService;

    @Override
    public StatusDto inStockCallBack(String docNo,String inRepositoryNo) {
//        BizAllocateApply apply = bizAllocateApplyDao.getByNo(docNo);
//        // 调拨入库之后要更改申请方入库计划状态
//        String curretOrgNo = userHolder.getLoggedUser().getOrganization().getOrgCode();
//        // 只有卖方机构入库的时候才生成出库计划
//        if(curretOrgNo.equals(apply.getOutstockOrgno())){
//            platformInstockCallback(docNo);
//        }
//           调拨入库之后要更改申请方入库计划状态
          bizInstockplanDetailDao.updateCompleteStatus(docNo);
//        // 更改申请单状态
        inOutCallBackService.updateApplyStatus(docNo,inRepositoryNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    @Override
    public StatusDto outStockCallBack(String docNo,String outRepositoryNo) {
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(docNo);
        // 调拨入库之后要更改申请方入库计划状态
        String curretOrgNo = userHolder.getLoggedUser().getOrganization().getOrgCode();
        if(curretOrgNo.equals(apply.getOutstockOrgno())){
            bizInstockplanDetailDao.updateCompleteStatus(docNo);
        }
        // 更改申请单状态
        inOutCallBackService.updateApplyOrderStatus(docNo,outRepositoryNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    /**
     *  入库之后回调事件(换货)
     * @param applyNo 申请单编号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto platformInstockCallback(String applyNo){
        try {
            // 根据单号查询申请单
            BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
            String applyType = apply.getApplyType();
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return StatusDto.buildFailureStatusDto("申请单为空！");
            }
            //获取卖方机构code
            String productOrgNo = getProductOrgNo(apply);
            //查询库存列表
            List<BizStockDetail> stockDetails = bizStockDetailDao.getStockDetailListByApplyNo(applyNo);
            if(null == stockDetails || stockDetails.size() == 0){
                return StatusDto.buildFailureStatusDto("库存列表为空！");
            }
            // 生成平台出库计划
            List<BizOutstockplanDetail> outstockplans = buildPlatformOutstockplan(apply, details, stockDetails);
            // 根据平台出库计划生成机构入库计划
            List<BizInstockplanDetail> bizInstockplanDetails = buildOrgInStockPlan(apply, outstockplans);
            // 批量保存出库计划详情
            bizOutstockplanDetailDao.batchOutstockplanDetail(outstockplans);
            // 保存机构入库计划
            bizInstockplanDetailDao.batchInsertInstockplanDetail(bizInstockplanDetails);
            return StatusDto.buildSuccessStatusDto("出库计划生成成功");
        } catch (Exception e) {
            logger.error("生成出入库计划失败！", e);
            throw e;
        }
    }

    /**
     * 根据订单获取商品所在仓库所属的机构编号
     * @param ba 申请开单
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private String getProductOrgNo(BizAllocateApply ba){
        String sellerOrgno = "";
        // 平台调拨
        if(BizAllocateApply.AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级调拨（服务间的调拨）
        if(BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getOutstockOrgno();
        }
        // 平级直调
        if(BizAllocateApply.AllocateApplyTypeEnum.DIRECTALLOCATE.toString().equals(ba.getApplyType())){
            sellerOrgno = BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM;//  平台的机构编号
        }
        // 商品换货
        if(BizAllocateApply.AllocateApplyTypeEnum.BARTER.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 退货
        if(BizAllocateApply.AllocateApplyTypeEnum.REFUND.toString().equals(ba.getApplyType())){
            sellerOrgno =  ba.getApplyorgNo();//  发起申请的机构编号
        }
        return sellerOrgno;
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
        // 根据入库计划里的商品查询商品库存
        List<String> products = instockplanDetails.stream().map(BizInstockplanDetail::getProductNo).collect(Collectors.toList());
        List<BizStockDetail> bizStockDetailList = bizStockDetailDao.queryStockByProducts(products, userHolder.getLoggedUser().getOrganization().getOrgCode());
        // 校验库存是否足够
        Map<String, List<BizStockDetail>> groupProduct = bizStockDetailList.stream().collect(Collectors.groupingBy(BizStockDetail::getProductNo));
        Map<String, List<BizInstockplanDetail>> inStockPlanProduct = instockplanDetails.stream().collect(Collectors.groupingBy(BizInstockplanDetail::getProductNo));
        for (Map.Entry<String, List<BizInstockplanDetail>> entryP : inStockPlanProduct.entrySet()) {
            List<BizInstockplanDetail> value = entryP.getValue();
            long planOutStockSum = value.stream().mapToLong(BizInstockplanDetail::getPlanInstocknum).sum();
            List<BizStockDetail> bizStockDetailList1 = groupProduct.get(entryP.getKey());
            long stockSum = bizStockDetailList1.stream().mapToLong(BizStockDetail::getValidStock).sum();
            if (planOutStockSum > stockSum) {
                throw new CommonException(Constants.ERROR_CODE, "可用库存不足，无法满足该申请的换货需求，请核对！");
            }
        }
        // 用来存储新的有效库存和占用库存
        List<BizStockDetail> bizStockDetailLists = Lists.newArrayList();
        // 以商品分组，并计算商品库存
        List<BizStockDetail> validStockList = bizStockDetailList.stream().filter(item -> item.getValidStock() > 0).collect(Collectors.toList());
        Map<String, List<BizStockDetail>> validStockProduct = validStockList.stream().collect(Collectors.groupingBy(BizStockDetail::getProductNo));
        for (Map.Entry<String, List<BizStockDetail>> entry : validStockProduct.entrySet()) {
            List<BizStockDetail> productStockDetail = entry.getValue();
            List<BizInstockplanDetail> bizInstockplanDetails = inStockPlanProduct.get(entry.getKey());
            BizInstockplanDetail bizInstockplanDetail = bizInstockplanDetails.get(0);
            // 获取入库计划中的每个商品的计划入库总数量
            long inStockPlanSum = bizInstockplanDetails.stream().mapToLong(BizInstockplanDetail::getPlanInstocknum).sum();
            for (BizStockDetail stockDetail : productStockDetail) {
                // 获取商品库存数量
                Long validStock = stockDetail.getValidStock();
                BizOutstockplanDetail outstockplanPlatform = new BizOutstockplanDetail();
                if (inStockPlanSum <= validStock) {
                    // 设置新的有效库存数量和占用库存数量
                    stockDetail.setValidStock(inStockPlanSum);
                    stockDetail.setOccupyStock(inStockPlanSum);
                    bizStockDetailLists.add(stockDetail);
                    outstockplanPlatform.setPlanOutstocknum(inStockPlanSum);
                    buildOutStockPlan(bizInstockplanDetail,stockDetail, outstockplanPlatform);
                    outstockplanPlatform.setCostPrice(stockDetail.getCostPrice());
                    outstockplanPlatform.setPurchaseInfo(stockDetail.getPurchaseInfo());
                    outstockplanDetails.add(outstockplanPlatform);
                    break;
                } else {
                    // 申请的数量大于库存的数量
                    stockDetail.setValidStock(validStock);
                    stockDetail.setOccupyStock(validStock);
                    inStockPlanSum = inStockPlanSum - validStock;
                    bizStockDetailLists.add(stockDetail);
                    outstockplanPlatform.setPlanOutstocknum(validStock);
                    buildOutStockPlan(bizInstockplanDetail,stockDetail, outstockplanPlatform);
                    outstockplanPlatform.setCostPrice(stockDetail.getCostPrice());
                    outstockplanPlatform.setPurchaseInfo(stockDetail.getPurchaseInfo());
                    outstockplanDetails.add(outstockplanPlatform);
                    if (inStockPlanSum <= 0) {
                        break;
                    }
                }
            }
        }
        // 更新库存表
        bizStockDetailDao.updateValidStockByOutStockPlan(bizStockDetailLists);
        return outstockplanDetails;
    }


    /**
     * 根据平台出库计划构建机构入库计划
     * @param apply 申请单
     * @param outstockplans 平台出库计划
     * @author liuduo
     * @date 2018-11-10 00:31:03
     */
    private List<BizInstockplanDetail> buildOrgInStockPlan(BizAllocateApply apply, List<BizOutstockplanDetail> outstockplans) {
        List<BizInstockplanDetail> instockplanDetailList = Lists.newArrayList();
        for (BizOutstockplanDetail outstockplan : outstockplans) {
            BizInstockplanDetail bizInstockplanDetail = new BizInstockplanDetail();
            bizInstockplanDetail.setProductNo(outstockplan.getProductNo());
            bizInstockplanDetail.setProductType(outstockplan.getProductType());
            bizInstockplanDetail.setProductCategoryname(outstockplan.getProductCategoryname());
            bizInstockplanDetail.setProductName(outstockplan.getProductName());
            bizInstockplanDetail.setProductUnit(outstockplan.getProductUnit());
            bizInstockplanDetail.setTradeNo(String.valueOf(apply.getApplyNo()));
            bizInstockplanDetail.setCompleteStatus(StockPlanStatusEnum.DOING.toString());
            bizInstockplanDetail.preInsert(userHolder.getLoggedUserId());
            bizInstockplanDetail.setInstockRepositoryNo(apply.getInRepositoryNo());
            bizInstockplanDetail.setInstockOrgno(apply.getApplyorgNo());
            bizInstockplanDetail.setSellerOrgno(apply.getOutstockOrgno());
            bizInstockplanDetail.setInstockType(InstockTypeEnum.BARTER.name());
            bizInstockplanDetail.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
            bizInstockplanDetail.setCostPrice(outstockplan.getCostPrice());
            bizInstockplanDetail.setPlanInstocknum(outstockplan.getPlanOutstocknum());
            bizInstockplanDetail.setSupplierNo(outstockplan.getSupplierNo());
            bizInstockplanDetail.setPurchaseInfo(outstockplan.getPurchaseInfo());
            instockplanDetailList.add(bizInstockplanDetail);
        }
        return instockplanDetailList;
    }


    /**
     * 构建平台出库计划
     * @param stockDetail 库存详情
     * @param outstockplanPlatform 出库计划
     * @author liuduo
     * @date 2018-11-10 00:24:15
     */
    private void buildOutStockPlan(BizInstockplanDetail bizInstockplanDetail, BizStockDetail stockDetail, BizOutstockplanDetail outstockplanPlatform) {
        outstockplanPlatform.setOutRepositoryNo(stockDetail.getRepositoryNo());
        outstockplanPlatform.setOutOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        outstockplanPlatform.setStockId(stockDetail.getId());
        outstockplanPlatform.setOutstockType(OutstockTypeEnum.BARTER.toString());
        outstockplanPlatform.setProductNo(bizInstockplanDetail.getProductNo());
        outstockplanPlatform.setProductType(bizInstockplanDetail.getProductType());
        outstockplanPlatform.setProductCategoryname(bizInstockplanDetail.getProductCategoryname());
        outstockplanPlatform.setProductName(bizInstockplanDetail.getProductName());
        outstockplanPlatform.setProductUnit(bizInstockplanDetail.getProductUnit());
        outstockplanPlatform.setTradeNo(bizInstockplanDetail.getTradeNo());
        outstockplanPlatform.setSupplierNo(stockDetail.getSupplierNo());
        outstockplanPlatform.setSalesPrice(BigDecimal.ZERO);
        outstockplanPlatform.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
        outstockplanPlatform.setPlanStatus(StockPlanStatusEnum.DOING.toString());
        outstockplanPlatform.preInsert(userHolder.getLoggedUserId());
    }

    /**
     * 构建出库计划(换货入库之后的回调用)
     * @param in 入库计划
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BizOutstockplanDetail buildBizOutstockplanDetail(BizInstockplanDetail in){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
        outPlan.setOutstockType(OutstockTypeEnum.BARTER.toString());// 出库类型
        outPlan.setProductNo(in.getProductNo());// 商品编号
        outPlan.setProductType(in.getProductType());// 商品类型
        outPlan.setProductCategoryname(in.getProductCategoryname());// 商品分类名称
        outPlan.setProductName(in.getProductName());// 商品名称
        outPlan.setProductUnit(in.getProductUnit());// 商品计量单位
        outPlan.setTradeNo(in.getTradeNo());// 交易批次号（申请单编号）
        outPlan.setSupplierNo(in.getSupplierNo());//供应商编号
        outPlan.setSalesPrice(BigDecimal.ZERO);// 销售价
        // 换货平台出的也是问题件
        outPlan.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
        outPlan.setPlanStatus(StockPlanStatusEnum.DOING.toString());// 出库计划的状态（计划执行中）
        outPlan.preInsert(userHolder.getLoggedUserId());
        outPlan.setStockType(in.getStockType());// 库存类型
        outPlan.setRemark(in.getRemark());// 备注
        return outPlan;
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
}
