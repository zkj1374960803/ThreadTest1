package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.constants.OrderStatusEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleStrategy;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 支付功能
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-22 16:33:00
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Autowired
    BizAllocateTradeorderDao bizAllocateTradeorderDao;

    /**
     *  支付完成调用接口
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @Override
    public StatusDto paymentCompletion(String applyNo){
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                throw new CommonException("0", "无效的申请单！");
            }
            // 只有等待付款的状态才可以支付
            if ( !ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name())) {
                throw new CommonException("0", "只有待付款的申请才可以支付！");
            }
            String status = BizAllocateApply.ApplyStatusEnum.WAITDELIVERY.name();// 等待发货
            // 支付成功之后，如果是采购，则状态为平台待入库
            if(ba.getApplyType().equals(BizAllocateApply.AllocateApplyTypeEnum.PURCHASE.name())){
                status = BizAllocateApply.ApplyStatusEnum.INSTORE.name();// 等待平台入库
            }
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            String payer = ba.getInstockOrgno();// 买入方(支付方)
            String receive = ba.getOutstockOrgno();//卖出方(接收方)
            BigDecimal sellTotal = getSellTotal(details);
            // 如果支付成功
            if(1 == 1){
                //更新申请单状态
                bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
                // 更新订单状态
                bizAllocateTradeorderDao.updateTradeorderStatus(applyNo,OrderStatusEnum.PAYMENTCOMPLETION.name());
            }else{
                return StatusDto.buildFailureStatusDto("支付失败！");
            }
            return StatusDto.buildSuccessStatusDto("支付成功！");

        } catch (Exception e) {
            logger.error("支付失败！", e);
            throw e;
        }
    }
    /**
     *  根据申请单获取总价格
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-29 15:02:58
     */
    @Override
    public FindAllocateApplyDTO getTotalPrice(String applyNo){
        FindAllocateApplyDTO allocate = new FindAllocateApplyDTO();
        // 根据申请单获取申请单详情
        List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
        BigDecimal sellTotal = getSellTotal(details);
        BigDecimal costTotal = getCostTotal(details);
        allocate.setCreateTime(new Date());
        allocate.setSellTotalPrice(sellTotal);
        allocate.setCostTotalPrice(costTotal);
        return allocate;
    }
    /**
     *  计算订单总价（销售价）
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
     *  计算订单总价(成本价)
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getCostTotal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal costPrice = BigDecimal.ZERO;
        BigDecimal appNum = BigDecimal.ZERO;
        for(AllocateapplyDetailBO bd : details){
            if(null != bd.getCostPrice()){
                //单价
                costPrice = bd.getCostPrice();
                // 数量
                appNum = BigDecimal.valueOf(bd.getApplyNum());
            }
            bigDecimal = bigDecimal.add(costPrice.multiply(appNum));
        }
        return bigDecimal;
    }
}
