package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
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
        if(ba.getApplyType().equals(BizAllocateApply.AllocateApplyTypeEnum.BARTER.name()) || ba.getApplyType().equals(BizAllocateApply.AllocateApplyTypeEnum.REFUND.name())){// 退换货
            throw new CommonException("0", "退换货不可以撤销！");
        }
        //更新申请单状态(已撤销)
        bizAllocateApplyDao.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.CANCEL.name());
        return StatusDto.buildSuccessStatusDto("支付成功！");
    }
}
