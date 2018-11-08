package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
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
        // 根据申请单获取申请单详情
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
        List<BizOutstockplanDetail> outstockplans = buildPlatformOutstockplan(apply, details, stockDetails);
        // 构建平台出库计划并保存(特殊处理，根据平台的入库计划来构建)
//        convertStockDetail(stockDetails);
        // 保存占用库存
//        int flag = bizStockDetailDao.batchUpdateStockDetil(stockDetails);
//        // 更新失败
//        if(flag == 0){
//            throw new CommonException("0", "更新占用库存失败！");
//        }
        // 批量保存出库计划详情
        bizOutstockplanDetailDao.batchOutstockplanDetail(outstockplans);
        return StatusDto.buildSuccessStatusDto("出库计划生成成功");
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
        // 以商品分组，并计算商品库存
        Map<String, List<BizStockDetail>> groupProduct = bizStockDetailList.stream().collect(Collectors.groupingBy(BizStockDetail::getProductNo));
        Map<String, BizInstockplanDetail> singleProductStockDetail = instockplanDetails.stream().collect(Collectors.toMap(BizInstockplanDetail::getProductNo, Function.identity()));
        for (Map.Entry<String, List<BizStockDetail>> entryP : groupProduct.entrySet()) {
            // 校验库存是否满足
            List<BizStockDetail> value = entryP.getValue();
            long count = value.stream().map(BizStockDetail::getValidStock).count();
            BizInstockplanDetail bizInstockplanDetail = singleProductStockDetail.get(entryP.getKey());
            if (bizInstockplanDetail.getPlanInstocknum() > count) {
                throw new CommonException(ba.getApplyNo(), "可用库存不足，无法满足该申请的换货需求，请核对！");
            }
        }
        // 用来存储新的有效库存和占用库存
        List<BizStockDetail> bizStockDetailLists = Lists.newArrayList();
        List<BizStockDetail> ValidStockDetail = bizStockDetailList.stream().filter(item -> item.getValidStock() > 0).collect(Collectors.toList());
        Map<String, List<BizStockDetail>> byProductGroupStockDetail = ValidStockDetail.stream().collect(Collectors.groupingBy(BizStockDetail::getProductNo));
        for(BizInstockplanDetail in : instockplanDetails){
            // 计划入库数量
            Long planInstocknum = in.getPlanInstocknum();
            List<BizStockDetail> actualStockDetailList = byProductGroupStockDetail.get(in.getProductNo());
            for (BizStockDetail stockDetail : actualStockDetailList) {
                BizOutstockplanDetail outstockplanPlatform;
                outstockplanPlatform = buildBizOutstockplanDetail(in);
                outstockplanPlatform.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
                Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> in.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                if (applyFilter.isPresent()) {
                    outstockplanPlatform.setApplyDetailId(applyFilter.get().getId());//申请单详单id
                }
                // 获取商品库存数量
                Long validStock = stockDetail.getValidStock();
                // 申请的数量小于库存的数量
                if (planInstocknum <= validStock) {
                    // 设置新的有效库存数量和占用库存数量
                    stockDetail.setValidStock(validStock - planInstocknum);
                    stockDetail.setOccupyStock(planInstocknum);
                    bizStockDetailLists.add(stockDetail);
                    outstockplanPlatform.setOutRepositoryNo(stockDetail.getRepositoryNo());// 平台仓库编号
                    outstockplanPlatform.setPlanOutstocknum(planInstocknum);// 计划出库数量applyNum
                    outstockplanPlatform.setOutOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台code
                    outstockplanPlatform.setStockId(stockDetail.getId());// 库存编号id
                    outstockplanPlatform.setCostPrice(in.getCostPrice());// 成本价
                    outstockplanDetails.add(outstockplanPlatform);
                    break;
                } else {
                    // 申请的数量大于库存的数量
                    stockDetail.setValidStock(0L);
                    stockDetail.setOccupyStock(validStock);
                    planInstocknum = planInstocknum - validStock;
                    bizStockDetailLists.add(stockDetail);
                    outstockplanPlatform.setOutRepositoryNo(stockDetail.getRepositoryNo());// 平台仓库编号
                    outstockplanPlatform.setPlanOutstocknum(validStock);// 计划出库数量applyNum
                    outstockplanPlatform.setOutOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);// 平台code
                    outstockplanPlatform.setStockId(stockDetail.getId());// 库存编号id
                    outstockplanPlatform.setCostPrice(in.getCostPrice());// 成本价
                    outstockplanDetails.add(outstockplanPlatform);
                    if (planInstocknum <= 0) {
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
