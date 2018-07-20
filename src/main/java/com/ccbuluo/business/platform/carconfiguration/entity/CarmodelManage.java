package com.ccbuluo.business.platform.carconfiguration.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型管理表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "车型管理表实体CarmodelManage", description = "车型管理表")
public class CarmodelManage extends CarCommonEntity {


    /**
     * 车型名称
     */
    @ApiModelProperty(name = "carmodelName", value = "车型名称")
    private String carmodelName;
    /**
     * 所属品牌，引用自品牌管理表的id字段
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌，引用自品牌管理表的id字段")
    private Long carbrandId;
    /**
     * 所属车系，引用自车系管理表的id字段
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系，引用自车系管理表的id字段")
    private Long carseriesId;
    /**
     * 车型标题
     */
    @ApiModelProperty(name = "modelTitle", value = "车型标题")
    private String modelTitle;
    /**
     * 车型主图
     */
    @ApiModelProperty(name = "modelMasterImage", value = "车型主图")
    private String modelMasterImage;
    /**
     * 车型图片,副图，用英文分号分隔。
     */
    @ApiModelProperty(name = "modelImage", value = "车型图片,副图，用英文分号分隔。")
    private String modelImage;
    /**
     * 车型编号,  C+6位自增编号，例如：C000001
     */
    @ApiModelProperty(name = "carmodelNumber", value = "车型编号,  C+6位自增编号，例如：C000001")
    private String carmodelNumber;
    /**
     * 车型状态，0为未启用，1为启用，默认为1
     */
    @ApiModelProperty(name = "carmodelStatus", value = "车型状态，0为未启用，1为启用，默认为1")
    private Integer carmodelStatus;
    /**
     * 车辆类型： 81:微型车 83:商务车 84:越野车 89:面包车 90:微型客车 96:小型车 98:中型车 99:中大型车 100:豪华车 103:电动车(常用) 112:电动单车 113:单车
     */
    @ApiModelProperty(name = "carType", value = "车辆类型： 81:微型车 83:商务车 84:越野车 89:面包车 90:微型客车 96:小型车 98:中型车 99:中大型车 100:豪华车 103:电动车(常用) 112:电动单车 113:单车")
    private Integer carType;

    public String getCarmodelNumber() {
        return carmodelNumber;
    }

    public void setCarmodelNumber(String carmodelNumber) {
        this.carmodelNumber = carmodelNumber;
    }

    public Long getCarbrandId() {
        return carbrandId;
    }

    public void setCarbrandId(Long carbrandId) {
        this.carbrandId = carbrandId;
    }

    public Long getCarseriesId() {
        return carseriesId;
    }

    public void setCarseriesId(Long carseriesId) {
        this.carseriesId = carseriesId;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
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

    public Integer getCarmodelStatus() {
        return carmodelStatus;
    }

    public void setCarmodelStatus(Integer carmodelStatus) {
        this.carmodelStatus = carmodelStatus;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }
}
