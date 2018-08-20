package com.ccbuluo.business.constants;

/**
 * 出库类型枚举
 * @author liupengfei
 * @date 2018-08-16 17:03:05
 */
public enum OutstockTypeEnum {

    TRANSFER("调拨出库"),BARTER("换货出库"),REFUND("退货出库");

    OutstockTypeEnum(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
