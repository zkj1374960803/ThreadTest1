package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 批量更新用（根据code）
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "UpdateCarcoreInfoDTO", description = "批量更新用（根据code）")
public class UpdateCarcoreInfoDTO {
    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carNumber", value = "车辆编号")
    private String carNumber;

    /**
     * 客户经理的id
     */
    @ApiModelProperty(name = "cusmanagerUuid", value = "客户经理的id")
    private String cusmanagerUuid;
    /**
     * 客户经理名称
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名称")
    private String cusmanagerName;
    /**
     * 车辆状态
     */
    private Integer carStatus;

    List<UpdateCarcoreInfoDTO> selfList;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCusmanagerUuid() {
        return cusmanagerUuid;
    }

    public void setCusmanagerUuid(String cusmanagerUuid) {
        this.cusmanagerUuid = cusmanagerUuid;
    }

    public String getCusmanagerName() {
        return cusmanagerName;
    }

    public void setCusmanagerName(String cusmanagerName) {
        this.cusmanagerName = cusmanagerName;
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public List<UpdateCarcoreInfoDTO> getSelfList() {
        return selfList;
    }

    public void setSelfList(List<UpdateCarcoreInfoDTO> selfList) {
        this.selfList = selfList;
    }
}