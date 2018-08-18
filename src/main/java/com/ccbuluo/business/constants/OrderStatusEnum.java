package com.ccbuluo.business.constants;

/**
 * 交易单 状态
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-18 15:15:33
 */
public enum OrderStatusEnum {
    PAYMENTWAITING("等待支付"),PAYMENTCOMPLETION("支付完成");

    OrderStatusEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
