package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.http.StatusDto;
import org.springframework.stereotype.Service;

/**
 * 维修单出入库回调
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:37:47
 */
@Service
public class ServiceStockInOutCallBack implements StockInOutCallBack {

    @Override
    public StatusDto inStockCallBack(String docNo) {
        return null;
    }

    @Override
    public StatusDto outStockCallBack(String docNo) {
        return null;
    }
}
