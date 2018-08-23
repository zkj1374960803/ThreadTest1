package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

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
    private PlatformProxyApplyHandleStrategy platformAllocateApplyHandleService;
    @Resource
    private PurchaseApplyHandleStrategy purchaseApplyHandleService;
    @Resource
    private SameLevelApplyHandleStrategy serviceAllocateApplyHandleService;
    @Resource
    private PlatformDirectApplyHandleStrategy directAllocateApplyHandleService;
    @Resource
    private RefundApplyHandleStrategy refundApplyHandleStrategy;
    @Resource
    private BarterApplyHandleStrategy barterApplyHandleStrategy;


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
                case PLATFORMALLOCATE:    // 平台调拨
                    handle = platformAllocateApplyHandleService;
                    break;
                case SAMELEVEL:    // 平级调拨（服务间的调拨）
                    handle = serviceAllocateApplyHandleService;
                    break;
                case DIRECTALLOCATE:    // 直调
                    handle = directAllocateApplyHandleService;
                    break;
                case BARTER:    // 商品换货
                    handle = barterApplyHandleStrategy;
                    break;
                case REFUND:    //  退货
                    handle = refundApplyHandleStrategy;
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
     * 入库之后回调事件
     *
     * @param applyNo 申请单code
     * @return StatusDto
     * @author weijb
     * @date 2018-08-18 17:47:52
     */
    public StatusDto platformInstockCallback(String applyNo) {
        // 根据申请单获取申请单详情
        BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
        AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
        ApplyHandleStrategy handle = null;
        switch (typeEnum) {
            case PURCHASE:    // 采购
                handle = purchaseApplyHandleService;
                break;
            case PLATFORMALLOCATE:    // 平台调拨
                handle = platformAllocateApplyHandleService;
                break;
            case SAMELEVEL:    // 平级调拨（服务间的调拨没有回调）
                handle = serviceAllocateApplyHandleService;
                break;
            case DIRECTALLOCATE:    // 直调
                handle = directAllocateApplyHandleService;
                break;
            case BARTER:    // 商品换货
                handle = barterApplyHandleStrategy;
                break;
            case REFUND:    //  退货
                handle = refundApplyHandleStrategy;
                break;
            default:
                logger.error(typeEnum.toString() + "出现了未知回调类型！");
                break;
        }
        return handle.platformInstockCallback(ba);
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
                if (!ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.PENDING.name()) || !ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name())) {
                    BizAllocateApply.ApplyStatusEnum statusEnum = BizAllocateApply.ApplyStatusEnum.valueOf(ba.getApplyStatus());
                    throw new CommonException("0", statusEnum.getKey() + " 不可以撤销！");
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
                case PLATFORMALLOCATE:    // 平台调拨
                    handle = platformAllocateApplyHandleService;
                    break;
                case SAMELEVEL:    // 平级调拨（服务间的调拨）
                    handle = serviceAllocateApplyHandleService;
                    break;
                case DIRECTALLOCATE:    // 直调
                    handle = directAllocateApplyHandleService;
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
}
