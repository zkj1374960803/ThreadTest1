package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 下拉框列表用
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "CusmanagerCarCountDTO", description = "客户经理拥有的车辆")
public class CusmanagerCarCountDTO {
    /**
     * 客户经理uuid
     */
    @ApiModelProperty(name = "cusmanagerUuid", value = "客户经理uuid")
    private String cusmanagerUuid;
    /**
     * 车辆数
     */
    @ApiModelProperty(name = "carNum", value = "车辆数")
    private Integer carNum;

    public String getCusmanagerUuid() {
        return cusmanagerUuid;
    }

    public void setCusmanagerUuid(String cusmanagerUuid) {
        this.cusmanagerUuid = cusmanagerUuid;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }
}