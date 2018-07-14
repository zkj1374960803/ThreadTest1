package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车辆主要信息DTO
 * @author chaoshuai
 * @date 2018-06-20 17:48:50
 */
@ApiModel(value = "车辆主要信息DTOCarDTO", description = "车辆、车型、车辆实况")
public class CarDTO {
    @ApiModelProperty(name = "id", value = "车辆id")
    private Long id; // 车辆id
    @ApiModelProperty(name = "plateNumber", value = "车牌号")
    private String plateNumber; // 车牌号
    @ApiModelProperty(name = "carNumber", value = "车辆编号")
    private String carNumber; // 车辆编号
    @ApiModelProperty(name = "carmodelId", value = "车型id")
    private Long carmodelId; // 车型id
    @ApiModelProperty(name = "carmodelName", value = "车型名称")
    private String carmodelName; // 车型名称
    @ApiModelProperty(name = "modelMasterImage", value = "主图")
    private String modelMasterImage; // 主图
    @ApiModelProperty(name = "modelImage", value = "图片")
    private String modelImage; // 图片
    @ApiModelProperty(name = "leaseStatus", value = "租赁状态")
    private String leaseStatus; // 租赁状态
    @ApiModelProperty(name = "storeId", value = "门店id")
    private Long storeId; // 门店id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Long getCarmodelId() {
        return carmodelId;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getModelMasterImage() {
        return modelMasterImage;
    }

    public void setModelMasterImage(String modelMasterImage) {
        this.modelMasterImage = modelMasterImage;
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }

    public String getLeaseStatus() {
        return leaseStatus;
    }

    public void setLeaseStatus(String leaseStatus) {
        this.leaseStatus = leaseStatus;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
