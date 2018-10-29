package com.ccbuluo.business.platform.storehouse.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.annotation.validate.ValidateLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "仓库实体(保存用)", description = "仓库实体(保存用)")
public class SaveBizServiceStorehouseDTO extends IdEntity {
    /**
     * 名称
     */
    @ValidateLength(min = 0, max = 10, message = "仓库名称长度不能超过10个字")
    @ValidateNotBlank(message = "名称不能为空")
    @ApiModelProperty(name = "storehouseName", value = "名称", required = true)
    private String storehouseName;
    /**
     * 仓库面积单位平米
     */
    @ValidateNotNull(message = "面积不能为空")
    @ApiModelProperty(name = "storehouseAcreage", value = "仓库面积单位平米", required = true)
    private Double storehouseAcreage;
    /**
     * 所属服务中心code（组织结构code）
     */
    @ValidateNotBlank(message = "所属服务中心不能为空")
    @ApiModelProperty(name = "servicecenterCode", value = "所属服务中心code（组织结构code）", required = true)
    private String servicecenterCode;
    /**
     * 启用、停用
     */
    @ValidateNotNull(message = "状态不能为空")
    @ApiModelProperty(name = "storehouseStatus", value = "启用、停用", required = true)
    private Long storehouseStatus;
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
    /**
     * 经度
     */
    @ApiModelProperty(name = "longitude", value = "经度", required = true)
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(name = "latitude", value = "纬度", required = true)
    private String latitude;
    /**
     * 详细地址
     */
    @ValidateLength(min = 0, max = 100, message = "地址不能超过100个字")
    @ValidateNotBlank(message = "详细地址不能为空")
    @ApiModelProperty(name = "storehouseAddress", value = "详细地址", required = true)
    private String storehouseAddress;



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


    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return this.provinceName;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }


    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
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

    public String getStorehouseAddress() {
        return storehouseAddress;
    }

    public void setStorehouseAddress(String storehouseAddress) {
        this.storehouseAddress = storehouseAddress;
    }
}