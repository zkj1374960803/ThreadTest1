package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;

import java.util.List;

/**
 * 车型dto
 * @author chaoshuai
 * @date 2018-05-08 18:17:08
 */
public class CarmodelManageDTO {
    private Long id;
    private Long carbrandId; // 品牌id
    private Long carseriesId; // 车系id
    private String carbrandLogo; // 品牌logo
    private String modelTitle; // 车型标题
    private String modelMasterImage; // 主图
    private String modelImage; // 图片
    private Integer carmodelStatus; // 车型状态
    private Integer carCount; // 注册车辆数量
    private List<CarmodelConfiguration> configurations; // 车型配置参数
    private String carmodelName; // 车型名称
    private String carmodelNumber; // 车型编号,  C+6位自增编号，例如：C000001
    private String displacementGearbox; // 排量+变速箱
    //李照新增
    private Long storeCount ; // 有该车型门店数
    private Boolean suggestPriceSetting; // 建议租价设置

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCarbrandLogo() {
        return carbrandLogo;
    }

    public void setCarbrandLogo(String carbrandLogo) {
        this.carbrandLogo = carbrandLogo;
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

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public List<CarmodelConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<CarmodelConfiguration> configurations) {
        this.configurations = configurations;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getCarmodelNumber() {
        return carmodelNumber;
    }

    public void setCarmodelNumber(String carmodelNumber) {
        this.carmodelNumber = carmodelNumber;
    }

    public String getDisplacementGearbox() {
        return displacementGearbox;
    }

    public void setDisplacementGearbox(String displacementGearbox) {
        this.displacementGearbox = displacementGearbox;
    }

    public Long getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }

    public Boolean getSuggestPriceSetting() {
        return suggestPriceSetting;
    }

    public void setSuggestPriceSetting(Boolean suggestPriceSetting) {
        this.suggestPriceSetting = suggestPriceSetting;
    }
}
