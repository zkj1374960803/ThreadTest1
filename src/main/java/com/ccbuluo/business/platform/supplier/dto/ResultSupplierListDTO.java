package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 返回供应商列表DTO
 * @author zhangkangjian
 * @date 2018-07-04 09:55:25
 */
@ApiModel(value = "ResultSupplierListDTO", description = "返回供应商列表DTO")
public class ResultSupplierListDTO {
    /**
     * 供应商id
     */
    @ApiModelProperty(name = "id", value = "供应商id")
    private Long id;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商编号")
    private String supplierCode;
    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名称", required = true)
    private String supplierName;
    /**
     * 联系人
     */
    @ApiModelProperty(name = "linkman", value = "联系人", required = true)
    private String linkman;
    /**
     * 联系电话
     */
    @ApiModelProperty(name = "supplierPhone", value = "联系电话", required = true)
    private String supplierPhone;
    /**
     * 详细地址
     */
    @ApiModelProperty(name = "supplierAddress", value = "详细地址")
    private String supplierAddress;
    /**
     * 供应商的使用状态：1启用/0停用
     */
    @ApiModelProperty(name = "supplierStatus", value = "供应商的使用状态：1启用/0停用")
    private Long supplierStatus;

    /**
     * 省
     */
    @ApiModelProperty(name = "provinceName", value = "省", required = true)
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty(name = "cityName", value = "市", required = true)
    private String cityName;
    /**
     * 区
     */
    @ApiModelProperty(name = "areaName", value = "区", required = true)
    private String areaName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public Long getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(Long supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
