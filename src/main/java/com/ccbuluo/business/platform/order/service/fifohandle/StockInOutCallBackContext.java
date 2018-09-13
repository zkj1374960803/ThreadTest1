package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.DocTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.order.service.typeassert.OrderTypeAsserterContext;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 出入库回调入口
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 16:32:16
 */
public class StockInOutCallBackContext {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderTypeAsserterContext orderTypeAsserterContext;
    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    private ServiceStockInOutCallBack serviceStockInOutCallBack;
    @Autowired
    PurchaseStockInOutCallBack PurchaseStockInOutCallBack;
    @Autowired
    SameLevelStockInOutCallBack sameLevelStockInOutCallBack;
    @Autowired
    RefundStockInOutCallBack refundStockInOutCallBack;
    @Autowired
    BarterStockInOutCallBack barterStockInOutCallBack;
    /**
     * 对单据做完入库后的回调方法
     * @param docNo 单据编号
     * @return
     * @author liupengfei
     * @date 2018-09-13 15:54:41
     */
    public StatusDto inStockCallBack(String docNo){
        try {
            DocTypeEnum docTypeEnum = orderTypeAsserterContext.getOrderType(docNo);
            StockInOutCallBack callBack = null;
            switch (docTypeEnum) {
                case APPLY_DOC:
                    // 申请单
                    // 根据申请单获取申请单详情
                    BizAllocateApply ba = bizAllocateApplyDao.getByNo(docNo);
                    if (null == ba) {
                        return StatusDto.buildFailureStatusDto("申请单不存在！");
                    }
                    BizAllocateApply.AllocateApplyTypeEnum typeEnum = BizAllocateApply.AllocateApplyTypeEnum.valueOf(ba.getApplyType());
                    switch (typeEnum) {
                        case PURCHASE:
                            // 采购
                            callBack = PurchaseStockInOutCallBack;
                            break;
                        case SAMELEVEL:
                            // 调拨
                            callBack = sameLevelStockInOutCallBack;
                            break;
                        case BARTER:
                            // 商品换货
                            callBack = barterStockInOutCallBack;
                            break;
                        case REFUND:
                            //  退货
                            callBack = refundStockInOutCallBack;
                            break;
                        default:
                            logger.error(typeEnum.toString() + "出现了未知处理类型！");
                            break;
                    }
                    break;
                case SERVICE_DOC:
                    // 售后服务单
                    callBack = serviceStockInOutCallBack;
                    break;
                default:
                    logger.error(docTypeEnum.toString() + "出现了未知处理类型！");
                    break;
            }
            return callBack.inStockCallBack(docNo);
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }


    /**
     * 对单据做了出库后的回调方法接口
     * @param docNo 单据编号
     * @return
     * @author liupengfei
     * @date 2018-09-13 15:55:42
     */
    public StatusDto outStockCallBack(String docNo){
        return null;
    }

}
