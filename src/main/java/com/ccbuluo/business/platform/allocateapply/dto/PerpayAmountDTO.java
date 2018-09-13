package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zhangkangjian
 * @date 2018-09-13 16:11:10
 */
@ApiModel(value = "PerpayAmountDTO", description = "预付款DTO")
public class PerpayAmountDTO {
    @ApiModelProperty(name = "supplierCode", value = "供应商code")
    private String supplierCode;
    @ApiModelProperty(name = "perpayAmount", value = "预付款金额")
    private BigDecimal perpayAmount;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public BigDecimal getPerpayAmount() {
        return perpayAmount;
    }

    public void setPerpayAmount(BigDecimal perpayAmount) {
        this.perpayAmount = perpayAmount;
    }
}
