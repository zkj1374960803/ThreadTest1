package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.http.StatusDto;

/**
 * 退货出入库回调
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:42:28
 */
public class RefundStockInOutCallBack implements StockInOutCallBack{
    @Override
    public StatusDto inStockCallBack(String docNo) {
        // 退货没有回调
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    @Override
    public StatusDto outStockCallBack(String docNo) {
        return null;
    }
}
