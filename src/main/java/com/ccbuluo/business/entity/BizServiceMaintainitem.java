package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修服务项实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "维修服务项实体", description = "维修服务项")
public class BizServiceMaintainitem extends AftersaleCommonEntity{
    /**
     * 服务项目的编码
     */
    @ApiModelProperty(name = "maintainitemCode", value = "服务项目的编码")
    private String maintainitemCode;
    /**
     * 服务项目名称
     */
    @ApiModelProperty(name = "maintainitemNeme", value = "服务项目名称")
    private String maintainitemNeme;
    /**
     * 单价
     */
    @ApiModelProperty(name = "unitPrice", value = "单价")
    private BigDecimal unitPrice;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;


    public void setMaintainitemCode(String maintainitemCode) {
        this.maintainitemCode = maintainitemCode;
    }

    public String getMaintainitemCode() {
        return this.maintainitemCode;
    }

    public void setMaintainitemNeme(String maintainitemNeme) {
        this.maintainitemNeme = maintainitemNeme;
    }

    public String getMaintainitemNeme() {
        return this.maintainitemNeme;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}