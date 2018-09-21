package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 下拉框列表用
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "CarcoreInfoDTO", description = "根据车牌号查询车辆信息")
public class CarcoreInfoDTO {
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号")
    private String vinNumber;
    /**
     * 发动机号
     */
    @ApiModelProperty(name = "engineNumber", value = "发动机号")
    private String engineNumber;
    /**
     * 车辆北斗id
     */
    @ApiModelProperty(name = "beidouNumber", value = "车辆北斗id")
    private String beidouNumber;
    /**
     * 品牌
     */
    @ApiModelProperty(name = "carbrandName", value = "品牌")
    private String carbrandName;
    /**
     * 车系
     */
    @ApiModelProperty(name = "carseriesName", value = "车系")
    private String carseriesName;
    /**
     * 车型id
     */
    @ApiModelProperty(name = "carmodelId", value = "车型id")
    private Long carmodelId;
    /**
     * 门店名字
     */
    @ApiModelProperty(name = "storeName", value = "门店名字")
    private String storeName;
    /**
     * 车型名字
     */
    @ApiModelProperty(name = "carmodelName", value = "车型名字")
    private String carmodelName;
    /**
     * 出厂时间
     */
    @ApiModelProperty(name = "produceTime", value = "出厂时间")
    private Date produceTime;

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getBeidouNumber() {
        return beidouNumber;
    }

    public void setBeidouNumber(String beidouNumber) {
        this.beidouNumber = beidouNumber;
    }

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public String getCarseriesName() {
        return carseriesName;
    }

    public void setCarseriesName(String carseriesName) {
        this.carseriesName = carseriesName;
    }

    public Long getCarmodelId() {
        return carmodelId;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }
}