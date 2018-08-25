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
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            // 只有申请提交和等待付款的状态才可以撤销
            if ( !ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name())) {
                throw new CommonException("0", "只有待付款的申请才可以支付！");
            }
            String status = BizAllocateApply.ApplyStatusEnum.WAITDELIVERY.name();// 等待发货
            // 支付成功之后，如果是采购，则状态为平台待入库
            if(applyNo.equals(BizAllocateApply.AllocateApplyTypeEnum.PURCHASE.name())){
                status = BizAllocateApply.ApplyStatusEnum.INSTORE.name();// 等待平台入库
            }
            //更新申请单状态
            bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
            return StatusDto.buildSuccessStatusDto("支付成功！");

        } catch (Exception e) {
            logger.error("支付失败！", e);
            throw e;
        }
    }
}
