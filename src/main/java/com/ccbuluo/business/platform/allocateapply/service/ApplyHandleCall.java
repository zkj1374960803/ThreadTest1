package com.ccbuluo.business.platform.allocateapply.service;

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
    private AllocateApplyHandleService allocateApplyHandleService;
    @Resource
    private PurchaseApplyHandleService purchaseApplyHandleService;


    @Transactional(rollbackFor = Exception.class)
    public int applyHandle(String applyNo){
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                return 0;
            }
            // 采购
            if(Constants.PROCESS_TYPE_PURCHASE.equals(ba.getProcessType())){
                return purchaseApplyHandleService.applyHandle(applyNo);
            }
            // 调拨
            if(Constants.PROCESS_TYPE_TRANSFER.equals(ba.getProcessType())){
                return allocateApplyHandleService.applyHandle(applyNo);
            }
            // 直调
            if(true){

            }
            return 0;
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }
}
