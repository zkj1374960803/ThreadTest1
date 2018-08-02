package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 保存供应商关联商品关系
 * @author zhangkangjian
 * @date 2018-08-01 10:12:08
 */
@ApiModel(value = "RelSupplierProduct", description = "保存供应商关联商品关系")
public class RelSupplierProduct {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", hidden = true)
    private Long id;
    /**
     * 供应商code
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商code")
    private String supplierCode;
    /**
     * 商品的code
     */
    @ApiModelProperty(name = "productCode", value = "商品的code")
    private String productCode;
    /**
     * 商品类型（注：PRODUCT零配件，EQUIPMENT物料）
     */
    @ApiModelProperty(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）")
    private String productType;
    @ApiModelProperty(name = "creator", value = "创建人", hidden = true)
    private String creator;
    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    private Date createTime;
    @ApiModelProperty(name = "operator", value = "操作人", hidden = true)
    private String operator;
    @ApiModelProperty(name = "operateTime", value = "操作时间", hidden = true)
    private Date operateTime;
    @ApiModelProperty(name = "deleteFlag", value = "删除标识", hidden = true)
    private Integer deleteFlag = 0;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
