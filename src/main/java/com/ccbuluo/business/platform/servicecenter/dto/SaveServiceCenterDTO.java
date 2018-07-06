package com.ccbuluo.business.platform.servicecenter.dto;

import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 保存用的服务中心实体DTO
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务中心实体(保存用)", description = "服务中心实体(保存用)")
public class SaveServiceCenterDTO{
    /**
     * 服务中心名称
     */
    @ApiModelProperty(name = "serviceCenterName", value = "服务中心名称")
    private String serviceCenterName;
    /**
     * 省
     */
    @ApiModelProperty(name = "province", value = "省")
    private String province;
    /**
     * 省code
     */
    @ApiModelProperty(name = "provinceCode", value = "省code")
    private Double provinceCode;
    /**
     * 市
     */
    @ApiModelProperty(name = "city", value = "市")
    private String city;
    /**
     * 市code
     */
    @ApiModelProperty(name = "cityCode", value = "市code")
    private Long cityCode;
    /**
     * 区
     */
    @ApiModelProperty(name = "area", value = "区")
    private String area;
    /**
     * 区code
     */
    @ApiModelProperty(name = "areaCode", value = "区code")
    private String areaCode;
    /**
     * 经度
     */
    @ApiModelProperty(name = "longitude", value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(name = "latitude", value = "纬度")
    private String latitude;
    /**
     * 详情地址
     */
    @ApiModelProperty(name = "address", value = "详情地址")
    private String address;
    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "storehouseName", value = "仓库名称")
    private String storehouseName;
    /**
     * 仓库状态
     */
    @ApiModelProperty(name = "storehouseStatus", value = "仓库状态")
    private Integer storehouseStatus;
    /**
     * 仓库面积
     */
    @ApiModelProperty(name = "storehouseAcreage", value = "仓库面积")
    private Double storehouseAcreage;
    /**
     * 标签ids
     */
    @ApiModelProperty(name = "labelIds", value = "标签ids")
    private String labelIds;

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Double provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCityCode() {
        return cityCode;
    }

    public void setCityCode(Long cityCode) {
        this.cityCode = cityCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public Integer getStorehouseStatus() {
        return storehouseStatus;
    }

    public void setStorehouseStatus(Integer storehouseStatus) {
        this.storehouseStatus = storehouseStatus;
    }

    public Double getStorehouseAcreage() {
        return storehouseAcreage;
    }

    public void setStorehouseAcreage(Double storehouseAcreage) {
        this.storehouseAcreage = storehouseAcreage;
    }

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }
}