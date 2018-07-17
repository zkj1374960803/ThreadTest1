package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 客服经理 上门维修 使用的维修车 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "客服经理 上门维修 使用的维修车 实体表实体", description = "客服经理 上门维修 使用的维修车 实体表")
public class BizServiceMaintaincar extends AftersaleCommonEntity{
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
    /**
     * 维修车的分配状态(1：已分配，0：未分配)
     */
    @ApiModelProperty(name = "carStatus", value = "维修车的分配状态(1：已分配，0：未分配)")
    private Long carStatus;
    /**
     * 车辆品牌的id
     */
    @ApiModelProperty(name = "carbrandId", value = "车辆品牌的id")
    private Long carbrandId;
    /**
     * 车系的id
     */
    @ApiModelProperty(name = "carseriesId", value = "车系的id")
    private Long carseriesId;
    /**
     * 车型的id
     */
    @ApiModelProperty(name = "carmodelId", value = "车型的id")
    private Long carmodelId;
    /**
     * 客户经理的id
     */
    @ApiModelProperty(name = "cusmanagerId", value = "客户经理的id")
    private String cusmanagerId;
    /**
     * 客户经理名称
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名称")
    private String cusmanagerName;
    /**
     * 北斗设备编号
     */
    @ApiModelProperty(name = "beidouNumber", value = "北斗设备编号")
    private String beidouNumber;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setMendCode(String mendCode) {
        this.mendCode = mendCode;
    }

    public String getMendCode() {
        return this.mendCode;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getVinNumber() {
        return this.vinNumber;
    }

    public void setCarStatus(Long carStatus) {
        this.carStatus = carStatus;
    }

    public Long getCarStatus() {
        return this.carStatus;
    }

    public void setCarbrandId(Long carbrandId) {
        this.carbrandId = carbrandId;
    }

    public Long getCarbrandId() {
        return this.carbrandId;
    }

    public void setCarseriesId(Long carseriesId) {
        this.carseriesId = carseriesId;
    }

    public Long getCarseriesId() {
        return this.carseriesId;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public Long getCarmodelId() {
        return this.carmodelId;
    }

    public void setCusmanagerId(String cusmanagerId) {
        this.cusmanagerId = cusmanagerId;
    }

    public String getCusmanagerId() {
        return this.cusmanagerId;
    }

    public void setCusmanagerName(String cusmanagerName) {
        this.cusmanagerName = cusmanagerName;
    }

    public String getCusmanagerName() {
        return this.cusmanagerName;
    }

    public void setBeidouNumber(String beidouNumber) {
        this.beidouNumber = beidouNumber;
    }

    public String getBeidouNumber() {
        return this.beidouNumber;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}