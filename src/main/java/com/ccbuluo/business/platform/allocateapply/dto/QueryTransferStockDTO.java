package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询可调拨库存列表
 * @author zhangkangjian
 * @date 2018-08-13 16:53:19
 */
@ApiModel(value = "QueryTransferStockDTO", description = "查询可调拨库存列表")
public class QueryTransferStockDTO {

    /**
     * 省
     */
    @ApiModelProperty(name = "provinceName", value = "省", required = true)
    private String provinceName;
    /**
     * 市
     */
    @ApiModelProperty(name = "cityName", value = "市", required = true)
    private String cityName;

    /**
     * 区
     */
    @ApiModelProperty(name = "areaName", value = "区", required = true)
    private String areaName;
    /**
     * 详细地址
     */
    @ApiModelProperty(name = "address", value = "详细地址", required = true)
    private String address;

    /**
     * 机构名称
     */
    @ApiModelProperty(name = "orgName", value = "机构名称", required = true)
    private String orgName;

    /**
     * 机构code
     */
    @ApiModelProperty(name = "orgCode", value = "机构code", required = true)
    private String orgCode;

    /**
     * 库存数量
     */
    @ApiModelProperty(name = "stockNum", value = "库存数量", required = true)
    private String stockNum;
    /**
     * 偏移量
     */
    @ApiModelProperty(name = "offset", value = "偏移量", hidden = true)
    private Integer offset;
    /**
     * 每页显示的数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示的数量", hidden = true)
    private Integer pageSize;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }
}
