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
@ApiModel(value = "仓库实体(查询列表用)", description = "仓库实体(查询列表用)")
public class SearchStorehouseListDTO extends IdEntity {
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
     * 服务中心名称
     */
    @ApiModelProperty(name = "storehouseStatus", value = "服务中心名称")
    private String serviceCenterName;
    /**
     * 地址
     */
    @ApiModelProperty(name = "storehouseAddress", value = "地址")
    private String storehouseAddress;


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

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public String getStorehouseAddress() {
        return storehouseAddress;
    }

    public void setStorehouseAddress(String storehouseAddress) {
        this.storehouseAddress = storehouseAddress;
    }
}