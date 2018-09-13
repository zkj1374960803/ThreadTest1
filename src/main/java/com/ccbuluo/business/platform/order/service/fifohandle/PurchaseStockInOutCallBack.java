package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 采购出入库回调
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:37:30
 */
@Service
public class PurchaseStockInOutCallBack implements StockInOutCallBack{

    @Autowired
    InOutCallBackService inOutCallBackService;

    @Override
    public StatusDto inStockCallBack(String docNo) {
        // 更改申请单状态
        inOutCallBackService.updateApplyStatus(docNo);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    @Override
    public StatusDto outStockCallBack(String docNo) {
        return null;
    }
}
