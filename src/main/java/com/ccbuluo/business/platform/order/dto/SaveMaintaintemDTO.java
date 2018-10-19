package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.core.annotation.validate.ValidateMin;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 新增维修单工时DTO
 * @author baoweiding
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "SaveMaintaintemDTO", description = "新增维修单工时DTO")
public class SaveMaintaintemDTO extends AftersaleCommonEntity {
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "订单编号")
    private String serviceOrderno;
    /**
     * 服务、零配件的编号
     */
    @ValidateNotBlank(message = "工时编号不能为空")
    @ApiModelProperty(name = "productNo", value = "服务、零配件的编号")
    private String productNo;
    /**
     * 商品的类型：工时、零配件
     */
    @ValidateNotBlank(message = "商品类型(工时)不能为空")
    @ApiModelProperty(name = "productType", value = "商品的类型：工时、零配件")
    private String productType;
    /**
     * 商品的单价
     */
    @ValidateNotNull(message = "商品的单价不能为空")
    @ApiModelProperty(name = "unitPrice", value = "商品的单价")
    private BigDecimal unitPrice;
    /**
     * 使用的数量
     */
    @ValidateMin(value = 0, message = "数量不能小于0")
    @ValidateNotNull(message = "使用的数量不能为空")
    @ApiModelProperty(name = "amount", value = "使用的数量")
    private Long amount;
    /**
     * 保修类型：在保、过保
     */
    @ValidateNotBlank(message = "保修类型不能为空")
    @ApiModelProperty(name = "warrantyType", value = "保修类型：在保、过保")
    private String warrantyType;
    /**
     * 提供维修保养服务的机构编号
     */
    @ApiModelProperty(name = "serviceOrgno", value = "提供维修保养服务的机构编号")
    private String serviceOrgno;
    /**
     * 提供维修保养服务的机构的名称
     */
    @ApiModelProperty(name = "serviceOrgname", value = "提供维修保养服务的机构的名称")
    private String serviceOrgname;
    /**
     * 提供维修服务的员工用户的uuid
     */
    @ApiModelProperty(name = "serviceUserid", value = "提供维修服务的员工用户的uuid")
    private String serviceUserid;
    /**
     * 提供维修服务的员工用户的姓名
     */
    @ApiModelProperty(name = "serviceUsername", value = "提供维修服务的员工用户的姓名")
    private String serviceUsername;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public String getProductNo() {
        return productNo;
    }

    public String getServiceOrderno() {
        return serviceOrderno;
    }

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getServiceOrgno() {
        return serviceOrgno;
    }

    public void setServiceOrgno(String serviceOrgno) {
        this.serviceOrgno = serviceOrgno;
    }

    public String getServiceOrgname() {
        return serviceOrgname;
    }

    public void setServiceOrgname(String serviceOrgname) {
        this.serviceOrgname = serviceOrgname;
    }

    public String getServiceUserid() {
        return serviceUserid;
    }

    public void setServiceUserid(String serviceUserid) {
        this.serviceUserid = serviceUserid;
    }

    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
