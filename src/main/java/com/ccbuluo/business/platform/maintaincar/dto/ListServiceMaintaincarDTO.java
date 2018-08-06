package com.ccbuluo.business.platform.maintaincar.dto;

import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 维修车辆用于下拉框展示
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "ListServiceMaintaincarDTO", description = "维修车辆用于下拉框展示")
public class ListServiceMaintaincarDTO {
    /**
     * 维修车辆编号
     */
    @ApiModelProperty(name = "mendCode", value = "维修车辆编号")
    private String mendCode;
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号")
    private String vinNumber;

    public String getMendCode() {
        return mendCode;
    }

    public void setMendCode(String mendCode) {
        this.mendCode = mendCode;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }
}