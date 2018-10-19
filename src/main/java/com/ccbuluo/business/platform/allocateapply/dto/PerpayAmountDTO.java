package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
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
    @ValidateNotBlank
    private String supplierCode;
    @ApiModelProperty(name = "supplierName", value = "供应商名称", hidden = true)
    @ValidateNotBlank
    private String supplierName;
    @ValidateNotNull
    @ApiModelProperty(name = "perpayAmount", value = "预付款金额")
    private BigDecimal perpayAmount;
    @ApiModelProperty(name = "totalPrice", value = "订单中所有商品的总价")
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

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
