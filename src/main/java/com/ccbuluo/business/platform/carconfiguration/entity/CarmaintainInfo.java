package com.ccbuluo.business.platform.carconfiguration.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆保养表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "车辆保养表实体CarmaintainInfo", description = "车辆保养表")
public class CarmaintainInfo extends CarCommonEntity {

    /**
     * 车辆基本信息id，引用车辆基本信息表的id字段
     */
    @ApiModelProperty(name = "carcoreId", value = "车辆基本信息id，引用车辆基本信息表的id字段")
    private Long carcoreId;
    /**
     * 间隔保养公里数
     */
    @ApiModelProperty(name = "intervalKm", value = "间隔保养公里数")
    private Integer intervalKm;
    /**
     * 车辆当前里程数
     */
    @ApiModelProperty(name = "carMileage", value = "车辆当前里程数")
    private Integer carMileage;
    /**
     * 下次保养时间
     */
    @ApiModelProperty(name = "nextMaintenanceTime", value = "下次保养时间")
    private Date nextMaintenanceTime;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
    /**
     * 到多少公里后需要下次保养
     */
    @ApiModelProperty(name = "nextMaintenanceMileage", value = "到多少公里后需要下次保养")
    private Integer nextMaintenanceMileage;
    /**
     * 保养类型
     */
    @ApiModelProperty(name = "maintainType", value = "保养类型")
    private String  maintainType;
    /**
     * 保养内容描述
     */
    @ApiModelProperty(name = "maintainContent", value = "保养内容描述")
    private String maintainContent;
    /**
     * 保养费用
     */
    @ApiModelProperty(name = "maintainCost", value = "保养费用")
    private BigDecimal maintainCost;
    /**
     * 费用结算状态
     */
    @ApiModelProperty(name = "costPayStatus", value = "费用结算状态")
    private String costPayStatus;
    /**
     * 售后系统中的服务单号
     */
    @ApiModelProperty(name = "serviceOrderNo", value = "售后系统中的服务单号")
    private String  serviceOrderNo;
    /**
     * 客户经理
     */
    @ApiModelProperty(name = "serviceManager", value = "客户经理")
    private String serviceManager;
    /**
     * 上传的保养照片的url，json格式
     */
    @ApiModelProperty(name = "maintainImages", value = "上传的保养照片的url")
    private String maintainImages;
    /**
     * 保养记录编号
     */
    @ApiModelProperty(name = "maintainNo", value = "保养记录编号")
    private String maintainNo;
    /**
     * 门店id
     */
    @ApiModelProperty(name = "ownerStoreId", value = "门店id")
    private Long ownerStoreId;


    public Long getCarcoreId() {
        return carcoreId;
    }

    public void setCarcoreId(Long carcoreId) {
        this.carcoreId = carcoreId;
    }

    public Integer getIntervalKm() {
        return intervalKm;
    }

    public void setIntervalKm(Integer intervalKm) {
        this.intervalKm = intervalKm;
    }

    public Integer getCarMileage() {
        return carMileage;
    }

    public void setCarMileage(Integer carMileage) {
        this.carMileage = carMileage;
    }

    public Date getNextMaintenanceTime() {
        return nextMaintenanceTime;
    }

    public void setNextMaintenanceTime(Date nextMaintenanceTime) {
        this.nextMaintenanceTime = nextMaintenanceTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getNextMaintenanceMileage() {
        return nextMaintenanceMileage;
    }

    public void setNextMaintenanceMileage(Integer nextMaintenanceMileage) {
        this.nextMaintenanceMileage = nextMaintenanceMileage;
    }

    public String getMaintainType() {
        return maintainType;
    }

    public void setMaintainType(String maintainType) {
        this.maintainType = maintainType;
    }

    public String getMaintainContent() {
        return maintainContent;
    }

    public void setMaintainContent(String maintainContent) {
        this.maintainContent = maintainContent;
    }

    public BigDecimal getMaintainCost() {
        return maintainCost;
    }

    public void setMaintainCost(BigDecimal maintainCost) {
        this.maintainCost = maintainCost;
    }

    public String getCostPayStatus() {
        return costPayStatus;
    }

    public void setCostPayStatus(String costPayStatus) {
        this.costPayStatus = costPayStatus;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    public String getMaintainImages() {
        return maintainImages;
    }

    public void setMaintainImages(String maintainImages) {
        this.maintainImages = maintainImages;
    }

    public String getMaintainNo() {
        return maintainNo;
    }

    public void setMaintainNo(String maintainNo) {
        this.maintainNo = maintainNo;
    }

    public Long getOwnerStoreId() {
        return ownerStoreId;
    }

    public void setOwnerStoreId(Long ownerStoreId) {
        this.ownerStoreId = ownerStoreId;
    }

    public enum CostPayStatusEnum {
        UNPAY("未结算"),PAYED("已结算");
        private String label;

        CostPayStatusEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
