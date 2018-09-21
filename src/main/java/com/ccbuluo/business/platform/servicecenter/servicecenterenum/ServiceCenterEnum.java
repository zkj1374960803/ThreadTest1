package com.ccbuluo.business.platform.servicecenter.servicecenterenum;

/**
 *
 * @author liupengfei
 * @date 2018-07-03 17:27:05
 */
public enum ServiceCenterEnum {
    SERVICECENTER("服务中心"),ADDED("创建服务中心附带添加的职场");


    ServiceCenterEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
