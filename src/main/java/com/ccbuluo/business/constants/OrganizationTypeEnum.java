package com.ccbuluo.business.constants;

/**
 * 机构类型枚举类
 * @author liupengfei
 * @date 2018-08-14 14:26:35
 */
public enum OrganizationTypeEnum {
    SERVICECENTER("服务中心"),
    CUSTMANAGER("客户经理"),
    SERVICEPLATFORM("售后平台"),
    RENTALSTORE("租赁门店");


    OrganizationTypeEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
