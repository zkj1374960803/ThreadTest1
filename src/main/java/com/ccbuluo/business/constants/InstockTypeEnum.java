package com.ccbuluo.business.constants;

/**
 * 入库、出库类型
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 20:47:33
 */
public enum InstockTypeEnum {
    TRANSFER("调拨"),PURCHASE("采购");

    InstockTypeEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
