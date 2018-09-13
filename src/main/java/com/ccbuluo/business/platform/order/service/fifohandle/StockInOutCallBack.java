package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.http.StatusDto;

/**
 * 对单据做了出入库后的回调方法接口
 * @author liupengfei
 * @date 2018-09-13 15:51:40
 */
public interface StockInOutCallBack {

    /**
     * 对单据做完入库后的回调方法
     * @param docNo 单据编号
     * @return
     * @author liupengfei
     * @date 2018-09-13 15:54:41
     */
    StatusDto inStockCallBack(String docNo);


    /**
     * 对单据做了出库后的回调方法接口
     * @param docNo 单据编号
     * @return
     * @author liupengfei
     * @date 2018-09-13 15:55:42
     */
    StatusDto outStockCallBack(String docNo);
}
