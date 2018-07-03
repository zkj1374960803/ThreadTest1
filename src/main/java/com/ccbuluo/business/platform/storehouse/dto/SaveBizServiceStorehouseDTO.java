package com.ccbuluo.business.platform.storehouse.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "仓库实体", description = "仓库实体")
public class SaveBizServiceStorehouseDTO extends IdEntity {
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
    @ApiModelProperty(name = "provinceName", value = "")
    private String provinceName;
    /**
     * 
     */
    @ApiModelProperty(name = "cityName", value = "")
    private String cityName;
    /**
     * 
     */
    @ApiModelProperty(name = "areaName", value = "")
    private String areaName;



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


}