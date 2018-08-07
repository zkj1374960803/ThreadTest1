package com.ccbuluo.business.thrid;

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
public class BizServiceStorehouse {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 仓库编号
     */
    @ApiModelProperty(name = "storehouseCode", value = "仓库编号")
    private String storehouseCode;
    /**
     * 名称
     */
    @ApiModelProperty(name = "storehouseName", value = "名称")
    private String storehouseName;
    /**
     * 仓库面积单位平米
     */
    @ApiModelProperty(name = "storehouseAcreage", value = "仓库面积单位平米")
    private Double storehouseAcreage;
    /**
     * 所属服务中心code（组织结构code）
     */
    @ApiModelProperty(name = "servicecenterCode", value = "所属服务中心code（组织结构code）")
    private String servicecenterCode;
    /**
     * 启用、停用
     */
    @ApiModelProperty(name = "storehouseStatus", value = "启用、停用")
    private Long storehouseStatus;
    /**
     * 
     */
    @ApiModelProperty(name = "storehouseAddress", value = "")
    private String storehouseAddress;
    /**
     * 
     */
    @ApiModelProperty(name = "longitude", value = "")
    private String longitude;
    /**
     * 
     */
    @ApiModelProperty(name = "latitude", value = "")
    private String latitude;
    /**
     * 
     */
    @ApiModelProperty(name = "provinceCode", value = "")
    private String provinceCode;
    /**
     * 
     */
    @ApiModelProperty(name = "provinceName", value = "")
    private String provinceName;
    /**
     * 
     */
    @ApiModelProperty(name = "cityCode", value = "")
    private String cityCode;
    /**
     * 
     */
    @ApiModelProperty(name = "cityName", value = "")
    private String cityName;
    /**
     * 
     */
    @ApiModelProperty(name = "areaCode", value = "")
    private String areaCode;
    /**
     * 
     */
    @ApiModelProperty(name = "areaName", value = "")
    private String areaName;
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

    public void setStorehouseCode(String storehouseCode) {
        this.storehouseCode = storehouseCode;
    }

    public String getStorehouseCode() {
        return this.storehouseCode;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public String getStorehouseName() {
        return this.storehouseName;
    }

    public void setStorehouseAcreage(Double storehouseAcreage) {
        this.storehouseAcreage = storehouseAcreage;
    }

    public Double getStorehouseAcreage() {
        return this.storehouseAcreage;
    }

    public void setServicecenterCode(String servicecenterCode) {
        this.servicecenterCode = servicecenterCode;
    }

    public String getServicecenterCode() {
        return this.servicecenterCode;
    }

    public void setStorehouseStatus(Long storehouseStatus) {
        this.storehouseStatus = storehouseStatus;
    }

    public Long getStorehouseStatus() {
        return this.storehouseStatus;
    }

    public void setStorehouseAddress(String storehouseAddress) {
        this.storehouseAddress = storehouseAddress;
    }

    public String getStorehouseAddress() {
        return this.storehouseAddress;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
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