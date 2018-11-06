package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 申请处理入口
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 19:26:33
 */
@Service
public class ApplyHandleContext {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private PurchaseApplyHandleStrategy purchaseApplyHandleService;
    @Resource
    private SameLevelApplyHandleStrategy serviceAllocateApplyHandleService;
    @Resource
    private RefundApplyHandleStrategy refundApplyHandleStrategy;
    @Resource
    private BarterApplyHandleStrategy barterApplyHandleStrategy;
    @Resource
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private PlatformApplyHandleStrategy platformApplyHandleStrategy;

    /**
     * 申请处理
     *
     * @param applyNo 申请单code
     * @return StatusDto
     * @author weijb
     * @date 2018-08-18 17:47:52
     */
    public StatusDto applyHandle(String applyNo) {
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if (null == ba) {
                return StatusDto.buildFailureStatusDto("申请单不存在！");
            }
            AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
            ApplyHandleStrategy handle = null;
            switch (typeEnum) {
                case PURCHASE:    // 采购
                    handle = purchaseApplyHandleService;
                    break;
                case SAMELEVEL:    // 调拨
                    handle = serviceAllocateApplyHandleService;
                    break;
                case BARTER:    // 商品换货
                    handle = barterApplyHandleStrategy;
                    break;
                case REFUND:    //  退货
                    handle = refundApplyHandleStrategy;
                    break;
                case PLATFORMBARTER:    //  平台调换
                    handle = platformApplyHandleStrategy;
                    break;
                case PLATFORMREFUND:    //  平台退货
                    handle = platformApplyHandleStrategy;
                    break;
                default:
                    logger.error(typeEnum.toString() + "出现了未知处理类型！");
                    break;
            }
            return handle.applyHandle(ba);
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }

    /**
     * 根据申请类型获取入库计划类型
     *
     * @param applyType 申请类型
     * @return
     * @throws
     * @author weijb
     * @date 2018-08-18 17:47:52
     */
    public String getInstockType(String applyType) {
        String instockType = "";
        // 调拨
        if (AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType)) {
            instockType = InstockTypeEnum.TRANSFER.toString();// 交易类型
        }
        // 采购
        if (AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)) {
            instockType = InstockTypeEnum.PURCHASE.toString();// 交易类型
        }
        // 换货
        if (AllocateApplyTypeEnum.BARTER.toString().equals(applyType) || AllocateApplyTypeEnum.REFUND.toString().equals(applyType)) {
            instockType = InstockTypeEnum.BARTER.toString();// 交易类型
        }
        return instockType;
    }

    /**
     * 根据申请类型获取出库计划类型
     *
     * @param applyType 申请类型
     * @return
     * @throws
     * @author weijb
     * @date 2018-08-18 17:47:31
     */
    public String getOutstockType(String applyType) {
        String outstockType = "";
        // 调拨出库
        if (AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType)
            || AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)) {
            outstockType = OutstockTypeEnum.TRANSFER.toString();// 交易类型
        }
        // 换货
        if (AllocateApplyTypeEnum.BARTER.toString().equals(applyType)) {
            outstockType = OutstockTypeEnum.BARTER.toString();// 交易类型
        }
        // 退货
        if (AllocateApplyTypeEnum.REFUND.toString().equals(applyType)) {
            outstockType = OutstockTypeEnum.REFUND.toString();// 交易类型
        }
        return outstockType;
    }

    /**
     * 申请撤销
     *
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public StatusDto cancelApply(String applyNo) {
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if (ba.getApplyType().equals(AllocateApplyTypeEnum.BARTER.name()) || ba.getApplyType().equals(AllocateApplyTypeEnum.REFUND.name())) {// 退换货
                throw new CommonException("0", "退换货不可以撤销！");
            } else {
                // 只有申请提交和等待付款的状态才可以撤销
                if (!ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.PENDING.name()) && !ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name())) {
                    BizAllocateApply.ApplyStatusEnum statusEnum = BizAllocateApply.ApplyStatusEnum.valueOf(ba.getApplyStatus());
                    throw new CommonException("0", statusEnum.getValue() + "的申请不可以撤销！");
                }
            }
            if (null == ba) {
                return StatusDto.buildFailureStatusDto("申请单不存在！");
            }
            AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
            ApplyHandleStrategy handle = null;
            switch (typeEnum) {
                case PURCHASE:    // 采购
                    handle = purchaseApplyHandleService;
                    break;
                case SAMELEVEL:    // 平级调拨（服务间的调拨）
                    handle = serviceAllocateApplyHandleService;
                    break;
                case BARTER:    // 商品换货
                    handle = barterApplyHandleStrategy;
                    break;
                case REFUND:    //  退货
                    handle = refundApplyHandleStrategy;
                    break;
                default:
                    logger.error(typeEnum.toString() + "出现了未知撤销类型！");
                    break;
            }
            return handle.cancelApply(applyNo);
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
    }

    /**
     *  更新交易单信息
     *
     * @param applyNo 申请单code
     * @param totalPrice 商品总价
     * @return StatusDto
     * @author weijb
     * @date 2018-08-29 11:18:52
     */
    public StatusDto updateTradeorderInfo(String applyNo, BigDecimal totalPrice) {
        try {
            // 保存生成订单
            bizAllocateTradeorderDao.updateTradeorderInfo(applyNo,totalPrice);
        } catch (Exception e) {
            logger.error("更新失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("更新成功！");
    }
}
