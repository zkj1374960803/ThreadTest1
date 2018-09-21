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
    /**
     * 客户经理uuid
     */
    @ApiModelProperty(name = "cusmanagerUuid", value = "客户经理uuid")
    private String cusmanagerUuid;
    /**
     * 客户经理名字
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名字")
    private String cusmanagerName;
    /**
     * 租赁系统中是否分配了门店
     */
    @ApiModelProperty(name = "storeAssigned", value = "租赁系统中是否分配了门店")
    private Integer storeAssigned;
    /**
     * 门店的机构编码
     */
    @ApiModelProperty(name = "storeCode", value = "门店的机构编码")
    private String storeCode;
    /**
     * 门店机构的名称
     */
    @ApiModelProperty(name = "storeName", value = "门店机构的名称")
    private String storeName;


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

    public Integer getStoreAssigned() {
        return storeAssigned;
    }

    public void setStoreAssigned(Integer storeAssigned) {
        this.storeAssigned = storeAssigned;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}