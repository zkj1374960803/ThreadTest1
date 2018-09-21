package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class SearchCarcoreInfoDTO {
    /**
     * 主键，自增
     */
    @ApiModelProperty(name = "id", value = "主键，自增")
    private Long id;
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
    /**
     * 所属品牌id
     */
    @ApiModelProperty(name = "carbrandName", value = "所属品牌名字")
    private String carbrandName;
    /**
     * 所属车系id
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系id",required = true)
    private Long carseriesId;
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
    /**
     * 车主（在租赁系统中的门店名称）
     */
    @ApiModelProperty(name = "storeName", value = "车主（在租赁系统中的门店名称）")
    private String storeName;
    /**
     * 客户经理名称
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名称")
    private String cusmanagerName;
    /**
     * 车辆状态
     */
    private Integer carStatus;
    /**
     * 车辆租赁状态（是否已分配门店）
     */
    @ApiModelProperty(name = "storeAssigned", value = "车辆租赁状态（是否已分配门店）")
    private Integer storeAssigned;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getVinNumber() {
        return this.vinNumber;
    }

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public Long getCarseriesId() {
        return carseriesId;
    }

    public void setCarseriesId(Long carseriesId) {
        this.carseriesId = carseriesId;
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

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCusmanagerName() {
        return cusmanagerName;
    }

    public void setCusmanagerName(String cusmanagerName) {
        this.cusmanagerName = cusmanagerName;
    }

    public Integer getStoreAssigned() {
        return storeAssigned;
    }

    public void setStoreAssigned(Integer storeAssigned) {
        this.storeAssigned = storeAssigned;
    }
}