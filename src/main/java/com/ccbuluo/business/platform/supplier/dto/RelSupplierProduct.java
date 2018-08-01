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
    @ApiModelProperty(name = "供应商code", value = "supplierCode")
    private String supplierCode;
    /**
     * 商品的code
     */
    @ApiModelProperty(name = "商品的code", value = "productCode")
    private String productCode;
    /**
     * 商品类型（注：PRODUCT零配件，EQUIPMENT物料）
     */
    @ApiModelProperty(name = "商品类型（注：PRODUCT零配件，EQUIPMENT物料）", value = "productType")
    private String productType;
    @ApiModelProperty(name = "创建人", value = "creator", hidden = true)
    private String creator;
    @ApiModelProperty(name = "创建时间", value = "createTime", hidden = true)
    private Date createTime;
    @ApiModelProperty(name = "操作人", value = "operator", hidden = true)
    private String operator;
    @ApiModelProperty(name = "操作时间", value = "operateTime", hidden = true)
    private Date operateTime;
    @ApiModelProperty(name = "删除标识", value = "deleteFlag", hidden = true)
    private Integer deleteFlag = 0;

    public enum ProductTypeEnum {
        PRODUCT("PRODUCT"),
        EQUIPMENT("EQUIPMENT");
        private String value;

        public String getValue() {
            return value;
        }

        ProductTypeEnum(String product) {
        }

        ProductTypeEnum() {
        }
    }



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
