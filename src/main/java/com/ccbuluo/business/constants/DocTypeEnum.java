package com.ccbuluo.business.constants;

/**
 * 售后系统中会出发出入库的单据的状态
 * @author liupengfei
 * @date 2018-09-13 15:30:38
 */
public enum DocTypeEnum {

    APPLY_DOC("申请单"),
    SERVICE_DOC("售后服务单"),
    UNKNOWN("售后服务单");


    DocTypeEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
