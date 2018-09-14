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
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        platformInstockCallback(docNo);
//        // 调拨入库之后要更改申请方入库计划状态
//        bizInstockplanDetailDao.updateCompleteStatus(docNo);
        // 更改申请单状态
        inOutCallBackService.updateApplyStatus(docNo,inRepositoryNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    @Override
    public StatusDto outStockCallBack(String docNo,String outRepositoryNo) {
        // 调拨入库之后要更改申请方入库计划状态
        bizInstockplanDetailDao.updateCompleteStatus(docNo);
        // 更改申请单状态
        inOutCallBackService.updateApplyOrderStatus(docNo);
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
        BizStockDetail bizStockDetail = new BizStockDetail();
        for(BizInstockplanDetail in : instockplanDetails){
            Optional<BizStockDetail> stockFilter = stockDetails.stream() .filter(stockDetail -> in.getId().equals(stockDetail.getInstockPlanid())) .findFirst();
            if (stockFilter.isPresent()) {
                bizStockDetail = stockFilter.get();
            }
            BizOutstockplanDetail outstockplanPlatform = new BizOutstockplanDetail();
            outstockplanPlatform = buildBizOutstockplanDetail(in);
            Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> in.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
            if (applyFilter.isPresent()) {
                outstockplanPlatform.setApplyDetailId(applyFilter.get().getId());//申请单详单id
            }
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
        outPlan.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());
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
