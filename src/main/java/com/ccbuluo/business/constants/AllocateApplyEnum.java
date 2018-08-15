package com.ccbuluo.business.constants;

/**
 * 调拨申请状态
 * @author weijb
 * @date 2018-08-14 18:41:05
 */
public enum AllocateApplyEnum {
    PURCHASE("平台采购"),PLATFORMALLOCATE("平台调拨"),SERVICEALLOCATE("平级调拨"),DIRECTALLOCATE("平级直调"),
    BARTER("商品退换"),REFUND("退款");

    AllocateApplyEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
