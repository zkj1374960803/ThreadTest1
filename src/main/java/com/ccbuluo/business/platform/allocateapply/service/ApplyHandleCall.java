package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.AllocateApplyEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
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
public class ApplyHandleCall {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private PlatformAllocateApplyHandleService platformAllocateApplyHandleService;
    @Resource
    private PurchaseApplyHandleService purchaseApplyHandleService;
    @Resource
    ServiceAllocateApplyHandleService serviceAllocateApplyHandleService;
    @Resource
    DirectAllocateApplyHandleService directAllocateApplyHandleService;


    @Transactional(rollbackFor = Exception.class)
    public int applyHandle(String applyNo){
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                return 0;
            }
            // 采购
            if(AllocateApplyEnum.PURCHASE.toString().equals(ba.getApplyType())){
                return purchaseApplyHandleService.applyHandle(applyNo,ba.getApplyType());
            }
            // 平台调拨
            if(AllocateApplyEnum.PLATFORMALLOCATE.toString().equals(ba.getApplyType())){
                return platformAllocateApplyHandleService.applyHandle(applyNo,ba.getApplyType());
            }
            // 平级调拨（服务间的调拨）
            if(AllocateApplyEnum.SERVICEALLOCATE.toString().equals(ba.getApplyType())){
                return serviceAllocateApplyHandleService.applyHandle(applyNo,ba.getApplyType());
            }
            // 平级直调
            if(AllocateApplyEnum.DIRECTALLOCATE.toString().equals(ba.getApplyType())){
                directAllocateApplyHandleService.applyHandle(applyNo,ba.getApplyType());
            }
            return 0;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }
}
