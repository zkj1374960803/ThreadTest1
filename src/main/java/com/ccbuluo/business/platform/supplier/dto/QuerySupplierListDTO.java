package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询供应商DTO
 * @author zhangkangjian
 * @date 2018-07-04 09:43:48
 */
@ApiModel(value = "QuerySupplierListDTO", description = "查询供应商DTO")
public class QuerySupplierListDTO {
    @ApiModelProperty(name = "provinceName", value = "省")
   private String provinceName;
    @ApiModelProperty(name = "cityName", value = "市")
   private String cityName;
    @ApiModelProperty(name = "areaName", value = "区")
   private String  areaName;
    @ApiModelProperty(name = "supplierStatus", value = "供应商的使用状态：-1全部/1启用/0停用")
   private Integer  supplierStatus;
    @ApiModelProperty(name = "keyword", value = "供应商名称/联系人/联系方式")
   private String  keyword;
    @ApiModelProperty(name = "offset", value = "起始数", required = true)
   private Integer  offset;
    @ApiModelProperty(name = "pageSize", value = "每页的数量", required = true)
   private Integer  pageSize;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(Integer supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
