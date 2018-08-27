package com.ccbuluo.business.custmanager.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询可调拨库存列表
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "QueryPendingMaterialsDTO", description = "查询待领取物料列表")
public class QueryPendingMaterialsDTO {
    private Long id;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "productName", value = "商品名称")
    private String productName;
    /**
     * 分类或类型的名称
     */
    @ApiModelProperty(name = "productCategoryname", value = "分类或类型的名称")
    private String productCategoryname;
    /**
     * 供应商
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商code")
    private String supplierNo;
    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名称")
    private String supplierName;
    /**
     * 商品数量
     */
    @ApiModelProperty(name = "productNum", value = "商品数量")
    private Long productNum;
    /**
     * 领取状态
     */
    @ApiModelProperty(name = "completeStatus", value = "领取状态")
    private String completeStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

}