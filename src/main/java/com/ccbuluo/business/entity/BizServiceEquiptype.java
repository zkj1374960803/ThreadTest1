package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 物料的类型实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料的类型实体", description = "物料的类型")
public class BizServiceEquiptype extends AftersaleCommonEntity{
    /**
     * 类型的名称
     */
    @ApiModelProperty(name = "typeNeme", value = "类型的名称", required = true)
    private String typeNeme;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setTypeNeme(String typeNeme) {
        this.typeNeme = typeNeme;
    }

    public String getTypeNeme() {
        return this.typeNeme;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}