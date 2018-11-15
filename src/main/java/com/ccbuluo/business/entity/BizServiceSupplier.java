package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.*;
import com.ccbuluo.core.validate.Group;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.junit.validator.ValidateWith;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 供应商实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "BizServiceSupplier", description = "供应商实体")
public class BizServiceSupplier {
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
    @ValidateLength(min = 1, max = 20, groups = Group.Add.class, message = "供应商代码长度必须在1~20之间")
    @ApiModelProperty(name = "supplierMarkno", value = "供应商代码")
    private String supplierMarkno;
    /**
     * 供应商名称
     */
    @ValidateLength(min = 1, max = 50, groups = Group.Add.class, message = "供应商名称长度必须在1~50之间")
    @ApiModelProperty(name = "supplierName", value = "供应商名称", required = true)
    private String supplierName;
    /**
     * 联系人
     */
    @ValidateLength(min = 1, max = 10, groups = Group.Add.class, message = "联系人名称长度必须在1~10之间")
    @ApiModelProperty(name = "linkman", value = "联系人", required = true)
    private String linkman;
    /**
     * 联系电话
     */
    @ValidatePhone(groups = Group.Add.class, message = "供应商联系电话格式不对")
    @ApiModelProperty(name = "supplierPhone", value = "联系电话", required = true)
    private String supplierPhone;
    /**
     * 详细地址
     */
    @ValidateLength(min = 1, max = 50, groups = Group.Add.class, message = "详细地址长度必须在1~50之间")
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
    @ValidatePattern(regexp = {"STATERUN","PRIVATE", "JOINTVENTURE", ""}, excludeNull = true, groups = Group.Add.class, message = "供应商性质不合法")
    @ApiModelProperty(name = "supplierNature", value = "供应商性质 国营(STATERUN) 民营（PRIVATE）合资（JOINTVENTURE）")
    private String supplierNature;
    /**
     * 成立时间
     */
    @ValidateDateRange(pattern = "yyyy-MM-dd", excludeNull = true, groups = Group.Add.class, message = "成立时间格式不正确")
    @ApiModelProperty(name = "establishTime", value = "成立时间 Sting类型 例：2018-07-02")
    private Date establishTime;
    /**
     * 省
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "省名称不能为空")
    @ApiModelProperty(name = "provinceName", value = "省", required = true)
    private String provinceName;
    /**
     * 
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "省code不能为空")
    @ApiModelProperty(name = "provinceCode", value = "省code", required = true)
    private String provinceCode;
    /**
     * 市
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "市名称不能为空")
    @ApiModelProperty(name = "cityName", value = "市", required = true)
    private String cityName;
    /**
     * 
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "市code不能为空")
    @ApiModelProperty(name = "cityCode", value = "市code", required = true)
    private String cityCode;
    /**
     * 区
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "区名称不能为空")
    @ApiModelProperty(name = "areaName", value = "区", required = true)
    private String areaName;
    /**
     * 
     */
    @ValidateNotBlank(groups = Group.Add.class, message = "区code不能为空")
    @ApiModelProperty(name = "areaCode", value = "区code", required = true)
    private String areaCode;
    /**
     * 主营产品
     */
    @ValidateLength(max = 100, groups = Group.Add.class, message = "主营产品名称长度必须在1~100之间")
    @ApiModelProperty(name = "majorProduct", value = "主营产品", required = true)
    private String majorProduct;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人", hidden = true)
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    private Date createTime;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierCode() {
        return this.supplierCode;
    }

    public String getSupplierMarkno() {
        return supplierMarkno;
    }

    public void setSupplierMarkno(String supplierMarkno) {
        this.supplierMarkno = supplierMarkno;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkman() {
        return this.linkman;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierPhone() {
        return this.supplierPhone;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierAddress() {
        return this.supplierAddress;
    }

    public void setSupplierStatus(Long supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public Long getSupplierStatus() {
        return this.supplierStatus;
    }

    public void setSupplierNature(String supplierNature) {
        this.supplierNature = supplierNature;
    }

    public String getSupplierNature() {
        return this.supplierNature;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEstablishTime() {
        return this.establishTime;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setMajorProduct(String majorProduct) {
        this.majorProduct = majorProduct;
    }

    public String getMajorProduct() {
        return this.majorProduct;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }


}