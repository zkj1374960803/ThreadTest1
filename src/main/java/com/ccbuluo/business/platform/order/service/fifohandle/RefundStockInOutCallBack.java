package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 退货出入库回调
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:42:28
 */
@Service
public class RefundStockInOutCallBack implements StockInOutCallBack{

    @Autowired
    InOutCallBackService inOutCallBackService;
    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Autowired
    private BizStockDetailDao bizStockDetailDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;

    @Override
    public StatusDto inStockCallBack(String docNo,String inRepositoryNo) {
        // 平台还要出库
//        platformInstockCallback(docNo);
        // 更改申请单状态
        inOutCallBackService.updateApplyStatus(docNo,inRepositoryNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    @Override
    public StatusDto outStockCallBack(String docNo,String outRepositoryNo) {
        inOutCallBackService.updateApplyOrderStatus(docNo,outRepositoryNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }


    /**
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private List<BizOutstockplanDetail> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, List<RelOrdstockOccupy> relOrdstockOccupies){
        List<BizOutstockplanDetail> outList = new ArrayList<BizOutstockplanDetail>();
        // 平台方机构出库计划
        outstockplanSeller(outList,relOrdstockOccupies,stockDetails,details);
        return outList;
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
     * 构建占用库存和订单占用库存关系(不管是正常申请和退换货申请，每个申请单都有可能存在正常件和问题件)
     * @param details 申请单详情
     * @param stockDetails 库存列表
     * @param applyType 申请类型
     * @param relOrdstockOccupies 订单占用库存关系
     * @author weijb
     * @date 2018-08-08 17:55:41
     */
    private List<BizStockDetail>  buildStockAndRelOrdEntity(List<AllocateapplyDetailBO> details, List<BizStockDetail> stockDetails, String applyType, List<RelOrdstockOccupy> relOrdstockOccupies){
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
        List<BizStockDetail> list = distinctStockDetail(stockDetails,relOrdstockOccupies);
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
     * 卖方机构出库（机构2）
     * @param outList 出库计划list
     * @param relOrdstockOccupies 库存和占用库存关系
     * @param stockDetails 库存
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private void outstockplanSeller(List<BizOutstockplanDetail> outList, List<RelOrdstockOccupy> relOrdstockOccupies, List<BizStockDetail> stockDetails,List<AllocateapplyDetailBO> details){
        // 卖方机构出库计划
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            BizOutstockplanDetail outstockplanSeller = new BizOutstockplanDetail();
            for(BizStockDetail stockDetail : stockDetails){
                // 关系库存批次id和库存批次id相等
                if(ro.getStockId().intValue() == stockDetail.getId().intValue()){
                    Optional<AllocateapplyDetailBO> applyFilter = details.stream() .filter(applyDetail -> stockDetail.getProductNo().equals(applyDetail.getProductNo())) .findFirst();
                    if (applyFilter.isPresent()) {
                        AllocateapplyDetailBO applyDetail = applyFilter.get();
                        outstockplanSeller = buildBizOutstockplanDetail(applyDetail,stockDetail);
                        // 平台机构编号
                        outstockplanSeller.setOutOrgno(stockDetail.getOrgNo());
                    }
                    // 库存类型(在创建占用关系的时候赋值)
                    outstockplanSeller.setStockType(ro.getStockType());
                    // 计划出库数量applyNum
                    outstockplanSeller.setPlanOutstocknum(ro.getOccupyNum());
                    // 卖方仓库编号（根据机构和商品编号查询的库存）
                    outstockplanSeller.setOutRepositoryNo(stockDetail.getRepositoryNo());
                    // 库存编号id
                    outstockplanSeller.setStockId(stockDetail.getId());
                    // 成本价
                    outstockplanSeller.setCostPrice(stockDetail.getCostPrice());
                    // 交易类型
                    outstockplanSeller.setOutstockType(OutstockTypeEnum.REFUND.toString());
                    // 未执行
                    outstockplanSeller.setPlanStatus(StockPlanStatusEnum.DOING.toString());
                    outList.add(outstockplanSeller);
                    continue;
                }
            }
        }
    }

    /**
     * 构建出库计划
     * @param ad 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    protected BizOutstockplanDetail buildBizOutstockplanDetail(AllocateapplyDetailBO ad,BizStockDetail bd){
        BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
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
}
