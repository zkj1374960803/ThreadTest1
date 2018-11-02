package com.ccbuluo.business.constants;

/**
 * 申请单类型
 * @author weijb
 * @date 2018-08-09 16:00:05
 */
public enum CommonStatusEnum {
    ENABLE("启用"),DISABLE("禁用");

    CommonStatusEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
