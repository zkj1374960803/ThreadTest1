package com.ccbuluo.business.platform.order.service;


import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
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
            // 构建生成订单（机构1对平台）
            BizAllocateTradeorder bizAllocateTradeorder1 = buildOrderEntity(details);
            BizAllocateTradeorder bizAllocateTradeorder2 = buildOrderEntity(details);
            bizAllocateTradeorder1.setSellerOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从买方到平台"平台code"
            bizAllocateTradeorder2.setPurchaserOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从平台到卖方"平台code"
            List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
            bizAllocateTradeorder2.setSellerOrgno("");// (供应商不填为空)
            list.add(bizAllocateTradeorder1);// 从买方到平台
            list.add(bizAllocateTradeorder2);// 从平台到卖方
            //构建出库和入库计划
            //查询库存列表(平台的库存列表)
            List<BizStockDetail> stockDetails = getStockDetailList(BusinessPropertyHolder.TOP_SERVICECENTER, details);
            Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> pair = buildOutAndInstockplanDetail(details, stockDetails);
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            //保存出库计划
            //保存入库计划
            flag =1;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return flag;
    }

    /**
     *  构建出库和入库计划
     * @param details 申请单详情
     * @param stockDetails 库存详情列表
     * @return
     */
    private Pair<List<BizOutstockplanDetail>, List<BizInstockplanDetail>> buildOutAndInstockplanDetail(List<AllocateapplyDetailDTO> details, List<BizStockDetail> stockDetails){
        //申请单处理之后生成的出入库计划状态默认为：未生效
        return Pair.of(null, null);
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
            // 构建生成订单（机构1对平台）
            BizAllocateTradeorder bizAllocateTradeorder1 = buildOrderEntity(details);
            BizAllocateTradeorder bizAllocateTradeorder2 = buildOrderEntity(details);
            bizAllocateTradeorder1.setSellerOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从买方到平台"平台code"
            bizAllocateTradeorder2.setPurchaserOrgno(BusinessPropertyHolder.TOP_SERVICECENTER);// 从平台到卖方"平台code"
            List<BizAllocateTradeorder> list = new ArrayList<BizAllocateTradeorder>();
            list.add(bizAllocateTradeorder1);// 从买方到平台
            list.add(bizAllocateTradeorder2);// 从平台到卖方
            // 保存生成订单
            bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
            // 构建占用库存和订单占用库存关系
            //查询库存列表
            List<BizStockDetail> stockDetails = getStockDetailList(bizAllocateTradeorder2.getSellerOrgno(), details);
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

    //构建订单list用于批量保存
    private List<BizAllocateTradeorder> buildOrderEntityList(){
        return null;
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
