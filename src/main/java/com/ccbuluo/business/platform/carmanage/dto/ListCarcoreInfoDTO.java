package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 下拉框列表用
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "ListCarcoreInfoDTO", description = "下拉框列表用")
public class ListCarcoreInfoDTO {
    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carNumber", value = "车辆编号")
    private String carNumber;
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号")
    private String vinNumber;

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
}