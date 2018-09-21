package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询客户经理列表DTO
 * @author zhangkangjian
 * @date 2018-07-18 16:34:02
 */
@ApiModel(value = "QueryCustManagerListDTO", description = "查询客户经理列表DTO")
public class QueryCustManagerListDTO {
    @ApiModelProperty(name = "name", value = "用户姓名")
    private String name;
    @ApiModelProperty(name = "vinNumber", value = "绑定维修车（vinNumber）")
    private String vinNumber;
    @ApiModelProperty(name = "officePhone", value = "办公电话")
    private String officePhone;
    @ApiModelProperty(name = "serviceCenter", value = "所属服务中心")
    private String serviceCenter;
    @ApiModelProperty(name = "serviceCenterName", value = "所属服务中心")
    private String serviceCenterName;
    @ApiModelProperty(name = "inRepositoryNo", value = "仓库编号")
    private String inRepositoryNo;
    @ApiModelProperty(name = "offset", value = "偏移量")
    private Integer offset;
    @ApiModelProperty(name = "pageSize", value = "每页显示的条数")
    private Integer pageSize;
    @ApiModelProperty(name = "useruuid", value = "uuid")
    private String useruuid;
    @ApiModelProperty(name = "orgCode", value = "机构code")
    private String orgCode;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public String getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }
}
