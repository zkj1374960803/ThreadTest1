package com.ccbuluo.business.platform.supplier.dto;

import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.validate.Group;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 查询供应商详情DTO
 * @author zhangkangjian
 * @date 2018-07-04 10:37:29
 */
@ApiModel(value = "ResultFindSupplierDetailDTO", description = "查询供应商详情DTO")
public class ResultFindSupplierDetailDTO {

    /**
     * 供应商id
     */
    @ApiModelProperty(name = "id", value = "供应商id", hidden = true)
    private Long id;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商编号", hidden = true)
    private String supplierCode;
    /**
     * 供应商代码
     */
    @ApiModelProperty(name = "supplierMarkno", value = "供应商代码")
    private String supplierMarkno;
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
    private Long supplierStatus = 1L;
    /**
     * 供应商性质
     */
    @ApiModelProperty(name = "supplierNature", value = "供应商性质 国营(STATERUN) 民营（PRIVATE）合资（JOINTVENTURE）")
    private String supplierNature;
    /**
     * 成立时间
     */
    @JsonFormat(timezone = "GMT+8")
    @ApiModelProperty(name = "establishTime", value = "成立时间 Sting类型 例：2018-07-02")
    private Date establishTime;
    /**
     * 省
     */
    @ApiModelProperty(name = "provinceName", value = "省", required = true)
    private String provinceName;
    /**
     *
     */
    @ApiModelProperty(name = "provinceCode", value = "省code", required = true)
    private String provinceCode;
    /**
     * 市
     */
    @ApiModelProperty(name = "cityName", value = "市", required = true)
    private String cityName;
    /**
     *
     */
    @ApiModelProperty(name = "cityCode", value = "市code", required = true)
    private String cityCode;
    /**
     * 区
     */
    @ApiModelProperty(name = "areaName", value = "区", required = true)
    private String areaName;
    /**
     *
     */
    @ApiModelProperty(name = "areaCode", value = "区code", required = true)
    private String areaCode;
    /**
     * 主营产品
     */
    @ApiModelProperty(name = "majorProduct", value = "主营产品", required = true)
    private String majorProduct;

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

    public String getSupplierNature() {
        return supplierNature;
    }

    public void setSupplierNature(String supplierNature) {
        this.supplierNature = supplierNature;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getMajorProduct() {
        return majorProduct;
    }

    public void setMajorProduct(String majorProduct) {
        this.majorProduct = majorProduct;
    }

    public String getSupplierMarkno() {
        return supplierMarkno;
    }

    public void setSupplierMarkno(String supplierMarkno) {
        this.supplierMarkno = supplierMarkno;
    }
}
