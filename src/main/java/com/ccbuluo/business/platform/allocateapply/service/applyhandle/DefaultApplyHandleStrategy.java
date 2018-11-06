package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.CheckStockQuantityDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProductStockInfoDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    ProblemStockDetailDao problemStockDetailDao;

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
    public BigDecimal getSellTotal(List<AllocateapplyDetailBO> details){
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

    /**
     * 构建入库计划
     * @param ad 申请详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    protected BizInstockplanDetail buildBizInstockplanDetail(AllocateapplyDetailBO ad){
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
        inPlan.setCompleteStatus(StockPlanStatusEnum.DOING.toString());// 完成状态（计划执行中）
        inPlan.preInsert(userHolder.getLoggedUserId());
        inPlan.setStockType(ad.getStockType());// 库存类型
        inPlan.setRemark(ad.getRemark());// 备注
        // 卖方机构的编号
        inPlan.setSellerOrgno(ad.getOutstockOrgno());
        return inPlan;
    }

    private Map<String,Long> getProductStock(List<AllocateapplyDetailBO> details){
        Map<String, Long> map = new HashMap<String, Long>();
        for(AllocateapplyDetailBO ab : details){
            map.put(ab.getProductNo(),ab.getApplyNum());
        }
        return map;
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
        // 商品换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(ba.getApplyType())){
            sellerOrgno = ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 退货
        if(AllocateApplyTypeEnum.REFUND.toString().equals(ba.getApplyType())){
            sellerOrgno =  ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 平台换货
        if(AllocateApplyTypeEnum.PLATFORMBARTER.toString().equals(ba.getApplyType())){
            sellerOrgno =  ba.getApplyorgNo();//  发起申请的机构编号
        }
        // 平台退货
        if(AllocateApplyTypeEnum.PLATFORMREFUND.toString().equals(ba.getApplyType())){
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
                    bd.setOccupyStock(roo.getOccupyNum());// 占用的库存也要减去当时的占用记录
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
        List<BizStockDetail> list = distinctStockDetail(stockDetails,relOrdstockOccupies);
        return list;
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
     *  校验问题件库存是否满足
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-20 16:26:50
     */
    public void checkStock(String orgNo, List<AllocateapplyDetailBO> details){
        List<String> codeList = getProductList(details);
        List<StockBizStockDetailDTO> stockBizStockList = problemStockDetailDao.getStockBizStockList(orgNo,codeList);

        Map<String, List<StockBizStockDetailDTO>> collect = stockBizStockList.stream().collect(Collectors.groupingBy(StockBizStockDetailDTO::getProductNo));
        // 根据商品编号和库存类型合并
        List<AllocateapplyDetailBO> detailBOS = mergeStock(details);
        for(AllocateapplyDetailBO detail : detailBOS){
            List<StockBizStockDetailDTO> stockList = collect.get(detail.getProductNo());
            if(null != stockList && stockList.size() > 0){
                Long applyNum = 0L;
                // 问题件
                if(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name().equals(detail.getStockType())){
                    applyNum = stockList.get(0).getProblemStock();
                }
                // 正常件
                if(BizStockDetail.StockTypeEnum.VALIDSTOCK.name().equals(detail.getStockType())){
                    applyNum = stockList.get(0).getValidStock();
                }
                // 如果申请数量大于库存数量
                if(detail.getApplyNum().intValue() > applyNum.intValue()){
                    throw new CommonException("0", "库存不足！");
                }
            }else{
                throw new CommonException("0", "库存不足！");
            }
        }
    }
    /**
     *  根据商品编号和库存类型合并（一个单子有可能既有问题件又有正常件）
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-20 19:57:09
     */
    private List<AllocateapplyDetailBO> mergeStock(List<AllocateapplyDetailBO> details){
        List<AllocateapplyDetailBO> resultList =  new ArrayList<AllocateapplyDetailBO>();
        Map<String,Map<String,List<AllocateapplyDetailBO>>> amp = details.stream().collect(Collectors.groupingBy(AllocateapplyDetailBO::getProductNo,Collectors.groupingBy(AllocateapplyDetailBO::getStockType)));
        amp.forEach((key,value)->{
            value.forEach((key2,value2)->{
                AllocateapplyDetailBO detail = new AllocateapplyDetailBO();
                Long applyNum = 0L;
                for(AllocateapplyDetailBO adb : value2){
                    applyNum += adb.getApplyNum();
                }
                detail.setProductNo(key);
                detail.setStockType(key2);
                detail.setApplyNum(applyNum);
                resultList.add(detail);
            });
        });
        return resultList;
    }

}
