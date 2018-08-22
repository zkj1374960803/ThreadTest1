package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleStrategy;
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
import java.util.ArrayList;
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

    /**
     *  支付完成调用接口
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @Override
    public StatusDto paymentCompletion(String applyNo){
        BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
        if(null == ba){
            return StatusDto.buildFailureStatusDto("申请单不存在！");
        }
        BizAllocateApply.AllocateApplyTypeEnum typeEnum = BizAllocateApply.AllocateApplyTypeEnum.valueOf(ba.getApplyType());
        String status = "";
        switch (typeEnum){
            case PURCHASE:    // 采购
                status = BizAllocateApply.ApplyStatusEnum.WAITDELIVERY.name();// 等待发货
                break;
            case PLATFORMALLOCATE:    // 平台调拨
                status = "";
                break;
            case SAMELEVEL:    // 平级调拨（服务间的调拨）
                status = "";
                break;
            case DIRECTALLOCATE:    // 直调
                status = "";
                break;
            case BARTER:    // 商品换货
                status = "";
                break;
            case REFUND:    //  退货
                status = "";
                break;
            default:
                logger.error(typeEnum.toString()+"出现了未知处理类型！");
                break;
        }
        //更新申请单状态(已撤销)
        bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
        return StatusDto.buildSuccessStatusDto("支付成功！");
    }
}
