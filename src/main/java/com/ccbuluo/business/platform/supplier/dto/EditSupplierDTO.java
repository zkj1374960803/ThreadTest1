package com.ccbuluo.business.platform.supplier.dto;

import com.ccbuluo.core.annotation.validate.*;
import com.ccbuluo.core.validate.Group;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 编辑供应商
 * @author zhangkangjian
 * @date 2018-07-03 16:32:34
 */
@ApiModel(value = "EditSupplierDTO", description = "编辑供应商DTO")
public class EditSupplierDTO {
    /**
     * 供应商id
     */
    @ValidateNotBlank(groups = Group.Update.class)
    @ApiModelProperty(name = "id", value = "供应商id", required = true)
    private Long id;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商编号", hidden = true)
    private String supplierCode;
    /**
     * 供应商名称
     */
    @ValidateLength(min = 1, max = 50, groups = Group.Update.class, message = "供应商名称长度必须在1~50之间")
    @ApiModelProperty(name = "supplierName", value = "供应商名称", required = true)
    private String supplierName;
    /**
     * 联系人
     */
    @ValidateLength(min = 1, max = 10, groups = Group.Update.class, message = "联系人名称长度必须在1~10之间")
    @ApiModelProperty(name = "linkman", value = "联系人", required = true)
    private String linkman;
    /**
     * 联系电话
     */
    @ValidatePhone(groups = Group.Update.class, message = "供应商联系电话格式不对")
    @ApiModelProperty(name = "supplierPhone", value = "联系电话", required = true)
    private String supplierPhone;
    /**
     * 详细地址
     */
    @ValidateLength(min = 1, max = 50, groups = Group.Update.class, message = "详细地址长度必须在1~50之间")
    @ApiModelProperty(name = "supplierAddress", value = "详细地址", required = true)
    private String supplierAddress;
    /**
     * 供应商的使用状态：1启用/0停用
     */
    @ApiModelProperty(name = "supplierStatus", value = "供应商的使用状态：1启用/0停用", hidden = true)
    private Long supplierStatus = 1L;
    /**
     * 供应商性质
     */
    @ValidatePattern(regexp = {"STATERUN","PRIVATE", "JOINTVENTURE"}, excludeNull = true, groups = Group.Update.class, message = "供应商性质不合法")
    @ApiModelProperty(name = "supplierNature", value = "供应商性质 国营(STATERUN) 民营（PRIVATE）合资（JOINTVENTURE）")
    private String supplierNature;
    /**
     * 成立时间
     */
    @ValidateDateRange(pattern = "yyyy-MM-dd", excludeNull = true, groups = Group.Update.class, message = "成立时间格式不正确")
    @ApiModelProperty(name = "establishTime", value = "成立时间 Sting类型 例：2018-07-02")
    private Date establishTime;
    /**
     * 省
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "省名称不能为空")
    @ApiModelProperty(name = "provinceName", value = "省", required = true)
    private String provinceName;
    /**
     *
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "省code不能为空")
    @ApiModelProperty(name = "provinceCode", value = "省code", required = true)
    private String provinceCode;
    /**
     * 市
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "市名称不能为空")
    @ApiModelProperty(name = "cityName", value = "市", required = true)
    private String cityName;
    /**
     *
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "市code不能为空")
    @ApiModelProperty(name = "cityCode", value = "市code", required = true)
    private String cityCode;
    /**
     * 区
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "区名称不能为空")
    @ApiModelProperty(name = "areaName", value = "区", required = true)
    private String areaName;
    /**
     *
     */
    @ValidateNotBlank(groups = Group.Update.class, message = "区code不能为空")
    @ApiModelProperty(name = "areaCode", value = "区code", required = true)
    private String areaCode;
    /**
     * 主营产品
     */
    @ValidateLength(max = 100, groups = Group.Update.class, message = "主营产品名称长度必须在1~100之间")
    @ApiModelProperty(name = "majorProduct", value = "主营产品", required = true)
    private String majorProduct;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人", hidden = true)
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间", hidden = true)
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识", hidden = true)
    private Long deleteFlag = 0L;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEstablishTime() {
        return this.establishTime;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Long getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
