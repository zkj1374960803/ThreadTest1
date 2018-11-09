package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 描述 新增维修单工时DTO
 * @author baoweiding
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "SaveMaintaintemDTO", description = "新增维修单工时DTO")
public class ProductDetailDTO extends AftersaleCommonEntity {
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "订单编号")
    private String serviceOrderno;
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
    private BigDecimal unitPrice = new BigDecimal(0);
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
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    @ApiModelProperty(name = "productCategoryname", value = "商品分类的code")
    private String productCategoryname;
    @ApiModelProperty(name = "productUnit", value = "商品的单位")
    private String productUnit;
    @ApiModelProperty(name = "carModelName", value = "适配车型")
    private String carModelName;


    public Double getTotalPrice() {
        if (null == unitPrice) {
            return null;
        }
        return unitPrice.doubleValue() * amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategoryname() {
        return productCategoryname;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

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
}
