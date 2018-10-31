package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询供应商信息（下拉框）
 * @author zhangkangjian
 * @date 2018-08-01 10:12:08
 */
@ApiModel(value = "QuerySupplierInfoDTO", description = "查询供应商信息（下拉框）")
public class QuerySupplierInfoDTO {
    /**
     * 供应商code
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商code")
    private String supplierCode;

    /**
     * 商品的名称
     */
    @ApiModelProperty(name = "supplierName", value = "供应商的名称")
    private String supplierName;

    /**
     * 供应商地址
     */
    @ApiModelProperty(name = "address", value = "供应商地址")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
