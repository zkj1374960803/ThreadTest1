package com.ccbuluo.business.constants;

/**
 * 申请单类型
 * @author weijb
 * @date 2018-08-09 16:00:05
 */
public enum ApplyTypeEnum {
    APPLYORDER("申请单"),SERVICEORDER("服务单（维修单）");

    ApplyTypeEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
