package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 车辆停放的位置实体
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@ApiModel(value = "车辆停放的位置实体", description = "车辆停放的位置")
public class BizCarPosition {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 车辆的vin码
     */
    @ApiModelProperty(name = "carVin", value = "车辆的vin码")
    private String carVin;
    /**
     * 详细地址
     */
    @ApiModelProperty(name = "detailAddress", value = "详细地址")
    private String detailAddress;
    /**
     * 省份的编号
     */
    @ApiModelProperty(name = "provinceCode", value = "省份的编号")
    private String provinceCode;
    /**
     * 省份的名字
     */
    @ApiModelProperty(name = "provinceName", value = "省份的名字")
    private String provinceName;
    /**
     * 城市的编号
     */
    @ApiModelProperty(name = "cityCode", value = "城市的编号")
    private String cityCode;
    /**
     * 城市的名字
     */
    @ApiModelProperty(name = "cityName", value = "城市的名字")
    private String cityName;
    /**
     * 区的编号
     */
    @ApiModelProperty(name = "areaCode", value = "区的编号")
    private String areaCode;
    /**
     * 区的名字
     */
    @ApiModelProperty(name = "areaName", value = "区的名字")
    private String areaName;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人")
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private Integer deleteFlag;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getCarVin() {
        return this.carVin;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress() {
        return this.detailAddress;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}