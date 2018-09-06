package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 服务中心业务实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务中心业务实体", description = "服务中心业务实体")
public class BizServiceCenter extends AftersaleCommonEntity{
    /**
     * 服务中心机构的编号
     */
    @ApiModelProperty(name = "orgCode", value = "服务中心机构的编号")
    private String orgCode;
    /**
     * 联系人的姓名
     */
    @ApiModelProperty(name = "contact", value = "联系人的姓名")
    private String contact;
    /**
     * 联系人的手机号
     */
    @ApiModelProperty(name = "contactPhone", value = "联系人的手机号")
    private String contactPhone;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;


    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}