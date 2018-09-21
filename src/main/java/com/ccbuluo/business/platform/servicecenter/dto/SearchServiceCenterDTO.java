package com.ccbuluo.business.platform.servicecenter.dto;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 保存用的服务中心实体DTO
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务中心实体(查询详情用)", description = "服务中心实体(查询详情用)")
public class SearchServiceCenterDTO extends IdEntity {
    /**
     * 服务中心名称
     */
    @ApiModelProperty(name = "serviceCenterName", value = "服务中心名称")
    private String serviceCenterName;
    /**
     * 服务中心状态
     */
    @ApiModelProperty(name = "serviceCenterStatus", value = "服务中心状态")
    private Integer serviceCenterStatus;
    /**
     * 服务中心编号
     */
    @ApiModelProperty(name = "serviceCenterCode", value = "服务中心编号")
    private String serviceCenterCode;
    /**
     * 服务中心电话
     */
    @ApiModelProperty(name = "principalPhone", value = "服务中心电话")
    private String principalPhone;
    /**
     * 上班时间
     */
    @ApiModelProperty(name = "signTime", value = "上班时间")
    private String signTime;
    /**
     * 下班时间
     */
    @ApiModelProperty(name = "signoutTime", value = "下班时间")
    private String signoutTime;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
    /**
     * 详情地址
     */
    @ApiModelProperty(name = "address", value = "详情地址")
    private String address;
    /**
     * 标签
     */
    @ApiModelProperty(name = "labels", value = "标签")
    private List<BizServiceLabel> labels;
    /**
     * 仓库
     */
    @ApiModelProperty(name = "storehouseList", value = "仓库")
    private List<BizServiceStorehouse> storehouseList;

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public Integer getServiceCenterStatus() {
        return serviceCenterStatus;
    }

    public void setServiceCenterStatus(Integer serviceCenterStatus) {
        this.serviceCenterStatus = serviceCenterStatus;
    }

    public String getServiceCenterCode() {
        return serviceCenterCode;
    }

    public void setServiceCenterCode(String serviceCenterCode) {
        this.serviceCenterCode = serviceCenterCode;
    }

    public String getPrincipalPhone() {
        return principalPhone;
    }

    public void setPrincipalPhone(String principalPhone) {
        this.principalPhone = principalPhone;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSignoutTime() {
        return signoutTime;
    }

    public void setSignoutTime(String signoutTime) {
        this.signoutTime = signoutTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<BizServiceLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<BizServiceLabel> labels) {
        this.labels = labels;
    }

    public List<BizServiceStorehouse> getStorehouseList() {
        return storehouseList;
    }

    public void setStorehouseList(List<BizServiceStorehouse> storehouseList) {
        this.storehouseList = storehouseList;
    }
}