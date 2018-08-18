package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

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
    public int applyHandle(String applyNo){
        try {
            // todo 魏俊标 把if换成switch
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                return 0;
            }
            // 采购
            if(AllocateApplyTypeEnum.PURCHASE.toString().equals(ba.getApplyType())){
                return purchaseApplyHandleService.applyHandle(ba);
            }
            // 平台调拨
            if(AllocateApplyTypeEnum.PLATFORMALLOCATE.toString().equals(ba.getApplyType())){
                return platformAllocateApplyHandleService.applyHandle(ba);
            }
            // 平级调拨（服务间的调拨）
            if(AllocateApplyTypeEnum.SAMELEVEL.toString().equals(ba.getApplyType())){
                return serviceAllocateApplyHandleService.applyHandle(ba);
            }
            // 平级直调
            if(AllocateApplyTypeEnum.DIRECTALLOCATE.toString().equals(ba.getApplyType())){
                directAllocateApplyHandleService.applyHandle(ba);
            }
            // 商品退换
            if(AllocateApplyTypeEnum.BARTER.toString().equals(ba.getApplyType())){
                barterApplyHandleStrategy.applyHandle(ba);
            }
            // 退款
            if(AllocateApplyTypeEnum.REFUND.toString().equals(ba.getApplyType())){
                refundApplyHandleStrategy.applyHandle(ba);
            }
            return 0;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }

    /**
     *  入库之后回调事件
     * @param applyNo 申请单code
     * @return
     */
    public StatusDto instockAfterCallBack(String applyNo){
        // 根据申请单获取申请单详情
        BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
        // 采购
        if(AllocateApplyTypeEnum.PURCHASE.toString().equals(ba.getApplyType())){
            return purchaseApplyHandleService.instockAfterCallBack(ba);
        }
        return null;
    }
}
