package com.ccbuluo.business.platform.claimorder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询索赔单列表DTO
 * @author zhangkangjian
 * @date 2018-09-08 16:26:36
 */
@ApiModel(value = "QueryClaimorderListDTO", description = "查询索赔单列表")
public class QueryClaimorderListDTO {

    // 维修单的编号
    @ApiModelProperty(name = "claimOrdno", value = "维修单的编号")
    private  String claimOrdno;
    // 索赔单的状态
    @ApiModelProperty(name = "docStatus", value = "索赔单的状态")
    private  String docStatus;
    // 维修单的编号
    @ApiModelProperty(name = "serviceOrdno", value = "维修单的编号")
    private  String serviceOrdno;
    @ApiModelProperty(name = "carNo", value = "车牌号")
    private String carNo;
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private  Long createTime;
    @ApiModelProperty(name = "serviceOrgName", value = "服务中心名称")
    private  String serviceOrgName;
    @ApiModelProperty(name = "orgPhone", value = "服务中心电话")
    private  String orgPhone;
    @ApiModelProperty(name = "claimOrgno", value = "索赔机构的编号")
    private  String claimOrgno;

    public String getClaimOrgno() {
        return claimOrgno;
    }

    public void setClaimOrgno(String claimOrgno) {
        this.claimOrgno = claimOrgno;
    }

    public String getServiceOrgName() {
        return serviceOrgName;
    }

    public void setServiceOrgName(String serviceOrgName) {
        this.serviceOrgName = serviceOrgName;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    public String getClaimOrdno() {
        return claimOrdno;
    }

    public void setClaimOrdno(String claimOrdno) {
        this.claimOrdno = claimOrdno;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getServiceOrdno() {
        return serviceOrdno;
    }

    public void setServiceOrdno(String serviceOrdno) {
        this.serviceOrdno = serviceOrdno;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
