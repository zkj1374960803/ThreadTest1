package com.ccbuluo.business.platform.maintaincar.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客服经理 上门维修 使用的维修车 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "SearchBizServiceMaintaincarDTO", description = "客服经理 上门维修 使用的维修车 实体表")
public class SearchBizServiceMaintaincarDTO extends IdEntity {
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
     * 客户经理名称
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名称")
    private String cusmanagerName;
    /**
     * 所属品牌id
     */
    @ApiModelProperty(name = "carbrandName", value = "所属品牌名字")
    private String carbrandName;
    /**
     * 所属车系id
     */
    @ApiModelProperty(name = "carseriesName", value = "所属车系名字")
    private String carseriesName;
    /**
     * 所属车型id
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

    public void setCusmanagerName(String cusmanagerName) {
        this.cusmanagerName = cusmanagerName;
    }

    public String getCusmanagerName() {
        return this.cusmanagerName;
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