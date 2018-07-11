package com.ccbuluo.business.platform.servicecenter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询服务中心列表dto
 * @author liuduo
 * @date 2018-07-06 9:02:55
 */
@ApiModel(value = "服务中心参数(查询列表用)", description = "服务中心参数(查询列表用)")
public class SearchListDTO {
    @ApiModelProperty(name = "province", value = "省", example = "陕西省")
    private String province;//省
    @ApiModelProperty(name = "city", value = "市", example = "北京市")
    private String city;//市
    @ApiModelProperty(name = "area", value = "区", example = "海淀区")
    private String area;//区
    @ApiModelProperty(name = "keyword", value = "关键字")
    private String keyword;//关键字
    @ApiModelProperty(name = "status", value = "状态", example = "1")
    private String status;//状态
    @ApiModelProperty(name = "offset", value = "起始数", example = "0", required = true)
    private int offset;//起始数
    @ApiModelProperty(name = "pagesize", value = "每页数", example = "10", required = true)
    private int pagesize;//每页数

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
