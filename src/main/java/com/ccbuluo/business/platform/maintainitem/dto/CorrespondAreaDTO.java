package com.ccbuluo.business.platform.maintainitem.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务项目 各地区对基本定价的倍数实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务项目 各地区对基本定价的倍数实体(详情)", description = "服务项目 各地区对基本定价的倍数(详情)")
public class CorrespondAreaDTO {
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

}