package com.ccbuluo.business.constants;

/**
 *
 * @author liupengfei
 * @date 2018-07-03 17:27:05
 */
public enum DocCodePrefixEnum {
    SW("申请单号"),
    R("入库单号"),
    C("出库单号"),
    PK("盘库单号"),
    TH("退换单号"),
    JY("交易单号"),
    DD("订单号");


    DocCodePrefixEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
