package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 记录维修任务中使用的工时和零配件的详情实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "记录维修任务中使用的工时和零配件的详情实体", description = "记录维修任务中使用的工时和零配件的详情")
public class BizServiceorderDetail extends AftersaleCommonEntity{
    /**
     * 服务订单的单号
     */
    @ApiModelProperty(name = "orderNo", value = "服务订单的单号")
    private String orderNo;
    /**
     * 服务、零配件的编号
     */
    @ApiModelProperty(name = "productNo", value = "服务、零配件的编号")
    private String productNo;
    /**
     * 商品的类型：工时、零配件
     */
    @ApiModelProperty(name = "productType", value = "商品的类型：工时、零配件")
    private String productType;
    /**
     * 商品的单价
     */
    @ApiModelProperty(name = "unitPrice", value = "商品的单价")
    private BigDecimal unitPrice;
    /**
     * 使用的数量
     */
    @ApiModelProperty(name = "amount", value = "使用的数量")
    private Long amount;
    /**
     * 保修类型：在保、过保
     */
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

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return this.productNo;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getWarrantyType() {
        return this.warrantyType;
    }

    public void setServiceOrgno(String serviceOrgno) {
        this.serviceOrgno = serviceOrgno;
    }

    public String getServiceOrgno() {
        return this.serviceOrgno;
    }

    public void setServiceOrgname(String serviceOrgname) {
        this.serviceOrgname = serviceOrgname;
    }

    public String getServiceOrgname() {
        return this.serviceOrgname;
    }

    public void setServiceUserid(String serviceUserid) {
        this.serviceUserid = serviceUserid;
    }

    public String getServiceUserid() {
        return this.serviceUserid;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
    }

    public String getServiceUsername() {
        return this.serviceUsername;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}