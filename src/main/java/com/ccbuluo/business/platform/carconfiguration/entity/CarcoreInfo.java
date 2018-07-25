package com.ccbuluo.business.platform.carconfiguration.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "CarcoreInfo", description = "车辆管理 实体表")
public class CarcoreInfo extends CarCommonEntity {
    /**
     * 主键，自增
     */
    private Long id;
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
    /**
     * 所属品牌id
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌id",required = true)
    private Long carbrandId;
    /**
     * 所属车系id
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系id",required = true)
    private Long carseriesId;
    /**
     * 所属车型id
     */
    @ApiModelProperty(name = "carmodelId", value = "所属车型id",required = true)
    private Long carmodelId;
    /**
     * 出厂时间
     */
    @ApiModelProperty(name = "produceTime", value = "出厂日期")
    private Date produceTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 车辆状态
     */
    private Integer carStatus;

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

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getEngineNumber() {
        return this.engineNumber;
    }

    public void setBeidouNumber(String beidouNumber) {
        this.beidouNumber = beidouNumber;
    }

    public String getBeidouNumber() {
        return this.beidouNumber;
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

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public Date getProduceTime() {
        return this.produceTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }
}