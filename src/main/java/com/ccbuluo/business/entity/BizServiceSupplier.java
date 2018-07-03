package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class BizServiceSupplier {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商编号")
    private String supplierCode;
    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名称")
    private String supplierName;
    /**
     * 联系人
     */
    @ApiModelProperty(name = "linkman", value = "联系人")
    private String linkman;
    /**
     * 联系电话
     */
    @ApiModelProperty(name = "supplierPhone", value = "联系电话")
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
     * 供应商性质
     */
    @ApiModelProperty(name = "supplierNature", value = "供应商性质")
    private String supplierNature;
    /**
     * 成立时间
     */
    @ApiModelProperty(name = "establishTime", value = "成立时间")
    private Date establishTime;
    /**
     * 
     */
    @ApiModelProperty(name = "provinceName", value = "")
    private String provinceName;
    /**
     * 
     */
    @ApiModelProperty(name = "provinceCode", value = "")
    private String provinceCode;
    /**
     * 
     */
    @ApiModelProperty(name = "cityName", value = "")
    private String cityName;
    /**
     * 
     */
    @ApiModelProperty(name = "cityCode", value = "")
    private String cityCode;
    /**
     * 
     */
    @ApiModelProperty(name = "areaName", value = "")
    private String areaName;
    /**
     * 
     */
    @ApiModelProperty(name = "areaCode", value = "")
    private String areaCode;
    /**
     * 主营产品
     */
    @ApiModelProperty(name = "majorProduct", value = "主营产品")
    private String majorProduct;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人")
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Date operateTime;
    /**
     * 删除标示
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标示")
    private Long deleteFlag;

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

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

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