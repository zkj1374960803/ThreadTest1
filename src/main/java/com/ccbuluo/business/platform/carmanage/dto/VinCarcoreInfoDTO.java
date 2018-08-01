package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 根据vin获取车辆信息
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "VinCarcoreInfoDTO", description = "根据vin获取车辆信息")
public class VinCarcoreInfoDTO {
    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carNumber", value = "车辆编号")
    private String carNumber;
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号",required = true)
    private String vinNumber;
    /**
     * 发动机号
     */
    @ApiModelProperty(name = "engineNumber", value = "发动机号",required = true)
    private String engineNumber;
    /**
     * 车辆北斗ID
     */
    @ApiModelProperty(name = "beidouNumber", value = "车辆北斗ID")
    private String beidouNumber;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

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
}