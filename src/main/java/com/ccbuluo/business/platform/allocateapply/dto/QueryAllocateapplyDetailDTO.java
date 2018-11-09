package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询申请详单DTO
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "QueryAllocateapplyDetailDTO", description = "查询申请详单DTO")
public class QueryAllocateapplyDetailDTO {


    private Long id;
    /**
     * 调拨申请单编号
     */
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号", hidden = true)
    private String applyNo;
    /**
     * 商品的编号
     */
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    /**
     * 商品的名称
     */
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    /**
     * 商品类型（注：FITTINGS零配件，EQUIPMENT物料）
     */
    @ApiModelProperty(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）")
    private String productType;
    /**
     * 做冗余，分类整个路径的名字，多级用逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "做冗余，分类整个路径的名字，多级用逗号隔开")
    private String productCategoryname;
    /**
     * 申请的商品数量
     */
    @ApiModelProperty(name = "applyNum", value = "申请的商品数量")
    private Long applyNum = 0L;
    /**
     * 计量单位，用作展示冗余
     */
    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;
    /**
     * 销售单价
     */
    @ApiModelProperty(name = "sellPrice", value = "销售单价")
    private BigDecimal sellPrice = new BigDecimal(0);
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice = new BigDecimal(0);
    /**
     * 当集团采购时，可以设置供应商，将调拨申请单作为采购单
     */
    @ApiModelProperty(name = "supplierNo", value = "当集团采购时，可以设置供应商，将调拨申请单作为采购单")
    private String supplierNo;
    /**
     * 供应商的名称
     */
    @ApiModelProperty(name = "supplierName", value = "供应商的名称")
    private String supplierName;
    /**
     * 供应商的地址
     */
    @ApiModelProperty(name = "address", value = "供应商的地址")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalPrice(){
        if(sellPrice != null){
            return sellPrice.doubleValue() * applyNum;
        }else if(costPrice != null){
            return costPrice.doubleValue() * applyNum;
        }
        return 0D;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyNo() {
        return this.applyNo;
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

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductCategoryname() {
        return this.productCategoryname;
    }

    public void setApplyNum(Long applyNum) {
        this.applyNum = applyNum;
    }

    public Long getApplyNum() {
        return this.applyNum;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {

        return this.unit;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellPrice() {
        return this.sellPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierNo() {
        return this.supplierNo;
    }


}