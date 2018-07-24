package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 服务项目 各地区对基本定价的倍数实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务项目 各地区对基本定价的倍数实体", description = "服务项目 各地区对基本定价的倍数")
public class BizServiceMultipleprice extends AftersaleCommonEntity{
    /**
     * 服务项目的编码
     */
    @ApiModelProperty(name = "maintainitemCode", value = "服务项目的编码")
    private String maintainitemCode;
    /**
     * 倍数
     */
    @ApiModelProperty(name = "multiple", value = "倍数")
    private Double multiple;
    /**
     * 省code
     */
    @ApiModelProperty(name = "provinceCode", value = "省code")
    private String provinceCode;
    /**
     * 省名字
     */
    @ApiModelProperty(name = "provinceName", value = "省名字")
    private String provinceName;
    /**
     * 市code
     */
    @ApiModelProperty(name = "cityCode", value = "市code")
    private String cityCode;
    /**
     * 市名字
     */
    @ApiModelProperty(name = "cityName", value = "市名字")
    private String cityName;
    /**
     * 备注，100字
     */
    @ApiModelProperty(name = "remark", value = "备注，100字")
    private String remark;

    public void setMaintainitemCode(String maintainitemCode) {
        this.maintainitemCode = maintainitemCode;
    }

    public String getMaintainitemCode() {
        return this.maintainitemCode;
    }

    public void setMultiple(Double multiple) {
        this.multiple = multiple;
    }

    public Double getMultiple() {
        return this.multiple;
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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}