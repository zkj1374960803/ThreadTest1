package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 平台退款后回调
 * @author liuduo
 * @version v1.0.0
 * @date 2018-10-31 15:04:04
 */
@Component
public class PlatformRefundStockInOutCallBack implements StockInOutCallBack{

    @Autowired
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Autowired
    private InOutCallBackService inOutCallBackService;

    @Override
    public StatusDto inStockCallBack(String docNo, String inRepositoryNo) {
        // 更改申请单状态
        inOutCallBackService.updateApplyStatus(docNo,inRepositoryNo);
        return null;
    }

    @Override
    public StatusDto outStockCallBack(String docNo, String inRepositoryNo) {
        // 平台申请退款后自动出库，出库后回调本方法，直接修改申请单类型
        // 更改申请方入库计划状态
        bizInstockplanDetailDao.updateCompleteStatus(docNo);
        // 更改申请单状态
        inOutCallBackService.updateApplyOrderStatus(docNo,inRepositoryNo);
        return null;
    }
}
