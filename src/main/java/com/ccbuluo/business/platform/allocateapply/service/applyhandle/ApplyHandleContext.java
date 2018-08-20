package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.constants.OutstockTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
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
     *  申请处理
     * @param applyNo 申请单code
     * @return
     */
    public StatusDto applyHandle(String applyNo){
        try {
            // todo 魏俊标 把if换成switch
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                return StatusDto.buildFailureStatusDto("申请单不存在！");
            }
            AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
            switch (typeEnum){
                case PURCHASE:    // 采购
                    return purchaseApplyHandleService.applyHandle(ba);
                case PLATFORMALLOCATE:    // 平台调拨
                    return platformAllocateApplyHandleService.applyHandle(ba);
                case SAMELEVEL:    // 平级调拨（服务间的调拨）
                    return serviceAllocateApplyHandleService.applyHandle(ba);
                case DIRECTALLOCATE:    // 直调
                    directAllocateApplyHandleService.applyHandle(ba);
                case BARTER:    // 商品退换
                    barterApplyHandleStrategy.applyHandle(ba);
                case REFUND:    //  退款
                    refundApplyHandleStrategy.applyHandle(ba);
                default:
                    logger.error(typeEnum.toString()+"出现了未知处理类型！");
                    break;
            }
            return StatusDto.buildFailureStatusDto("申请处理失败！");
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }

    /**
     *  入库之后回调事件
     * @param applyNo 申请单code
     * @return StatusDto
     * @author weijb
     * @date 2018-08-18 17:47:52
     */
    public StatusDto platformInstockCallback(String applyNo){
        // 根据申请单获取申请单详情
        BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
        AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
        switch (typeEnum){
            case PURCHASE:    // 采购
                return purchaseApplyHandleService.platformInstockCallback(ba);
            case PLATFORMALLOCATE:    // 平台调拨
                return platformAllocateApplyHandleService.platformInstockCallback(ba);
            case SAMELEVEL:    // 平级调拨（服务间的调拨）
                break;
            case DIRECTALLOCATE:    // 直调
                break;
            case BARTER:    // 商品退换
                break;
            case REFUND:    //  退款
                break;
            default:
                logger.error(typeEnum.toString()+"出现了未知回调类型！");
                break;
        }
        return StatusDto.buildFailureStatusDto("出库计划生成失败！");
    }

    /**
     *  根据申请类型获取入库计划类型
     * @param applyType 申请类型
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-18 17:47:52
     */
    public String getInstockType(String applyType){
        String instockType = "";
        // 调拨
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            instockType = InstockTypeEnum.TRANSFER.toString();// 交易类型
        }
        // 采购
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(applyType)){
            instockType = InstockTypeEnum.PURCHASE.toString();// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) || AllocateApplyTypeEnum.REFUND.toString().equals(applyType) ){
            instockType = InstockTypeEnum.BARTER.toString();// 交易类型
        }
        return instockType;
    }

    /**
     *  根据申请类型获取出库计划类型
     * @param applyType 申请类型
     * @exception
     * @return
     * @author weijb
     * @date 2018-08-18 17:47:31
     */
    public String getOutstockType(String applyType){
        String outstockType = "";
        // 调拨出库
        if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(applyType) || AllocateApplyTypeEnum.SAMELEVEL.toString().equals(applyType) ){
            outstockType = OutstockTypeEnum.TRANSFER.toString();// 交易类型
        }
        // 换货
        if(AllocateApplyTypeEnum.BARTER.toString().equals(applyType) ){
            outstockType = OutstockTypeEnum.BARTER.toString();// 交易类型
        }
        // 退款
        if(AllocateApplyTypeEnum.REFUND.toString().equals(applyType) ){
            outstockType = OutstockTypeEnum.REFUND.toString();// 交易类型
        }
        return outstockType;
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    public StatusDto cancelApply(String applyNo){
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                return StatusDto.buildFailureStatusDto("申请单不存在！");
            }
            AllocateApplyTypeEnum typeEnum = AllocateApplyTypeEnum.valueOf(ba.getApplyType());
            switch (typeEnum){
                case PURCHASE:    // 采购
                    return purchaseApplyHandleService.cancelApply(applyNo);
                case PLATFORMALLOCATE:    // 平台调拨
                    return platformAllocateApplyHandleService.cancelApply(applyNo);
                case SAMELEVEL:    // 平级调拨（服务间的调拨）
                    return serviceAllocateApplyHandleService.cancelApply(applyNo);
                case DIRECTALLOCATE:    // 直调
                    directAllocateApplyHandleService.cancelApply(applyNo);
                case BARTER:    // 商品退换
                    barterApplyHandleStrategy.cancelApply(applyNo);
                case REFUND:    //  退款
                    refundApplyHandleStrategy.cancelApply(applyNo);
                default:
                    logger.error(typeEnum.toString()+"出现了未知撤销类型！");
                    break;
            }
            return StatusDto.buildFailureStatusDto("撤销处理失败！");
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
    }
}
