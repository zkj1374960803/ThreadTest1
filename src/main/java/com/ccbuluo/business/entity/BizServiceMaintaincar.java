package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 客服经理 上门维修 使用的维修车 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "BizServiceMaintaincar", description = "客服经理 上门维修 使用的维修车 实体表")
public class BizServiceMaintaincar extends AftersaleCommonEntity{
    /**
     * 维修车辆编号
     */
    @ApiModelProperty(name = "mendCode", value = "维修车辆编号")
    private String mendCode;
    /**
     * 车架号
     */
    @ValidateLength(min = 0, max = 17,message = "车架号长度不合法")
    @ApiModelProperty(name = "vinNumber", value = "车架号",required = true)
    private String vinNumber;
    /**
     * 维修车的分配状态(1：已分配，0：未分配)
     */
    @ApiModelProperty(name = "carStatus", value = "维修车的分配状态(1：已分配，0：未分配)")
    private Long carStatus;
    /**
     * 车辆品牌的id
     */
    @ValidateNotNull(message = "所属品牌不能为空")
    @ApiModelProperty(name = "carbrandId", value = "车辆品牌的id",required = true)
    private Long carbrandId;
    /**
     * 车系的id
     */
    @ValidateNotNull(message = "所属车系不能为空")
    @ApiModelProperty(name = "carseriesId", value = "车系的id",required = true)
    private Long carseriesId;
    /**
     * 车型的id
     */
    @ValidateNotNull(message = "所属车型不能为空")
    @ApiModelProperty(name = "carmodelId", value = "车型的id",required = true)
    private Long carmodelId;
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
     * 北斗设备编号
     */
    @ValidateLength(min = 0, max = 8,message = "车辆北斗ID长度不合法")
    @ApiModelProperty(name = "beidouNumber", value = "北斗设备编号",required = true)
    private String beidouNumber;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 所属品牌name
     */
    @ApiModelProperty(name = "carbrandName", value = "所属品牌名字")
    private String carbrandName;
    /**
     * 所属车系name
     */
    @ApiModelProperty(name = "carseriesName", value = "所属车系名字")
    private String carseriesName;
    /**
     * 所属车型name
     */
    @ApiModelProperty(name = "carmodelName", value = "所属车型名字")
    private String carmodelName;

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

    public String getCusmanagerUuid() {
        return cusmanagerUuid;
    }

    public void setCusmanagerUuid(String cusmanagerUuid) {
        this.cusmanagerUuid = cusmanagerUuid;
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

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }
}