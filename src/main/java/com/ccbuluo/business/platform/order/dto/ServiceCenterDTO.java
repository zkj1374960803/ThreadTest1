package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.BizCarPosition;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 服务中心DTO
 * @author liuduo
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "ServiceCenterDTO", description = "服务中心DTO")
public class ServiceCenterDTO extends AftersaleCommonEntity {
    /**
     * 服务中心或客户经理名字
     */
    @ApiModelProperty(name = "orgName", value = "服务中心或客户经理名字")
    private String orgName;
    /**
     * 联系人
     */
    @ApiModelProperty(name = "contact", value = "联系人")
    private String contact;
    /**
     * 联系人手机号
     */
    @ApiModelProperty(name = "contactPhone", value = "联系人手机号")
    private String contactPhone;
    /**
     * 地址
     */
    @ApiModelProperty(name = "address", value = "地址")
    private String address;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
