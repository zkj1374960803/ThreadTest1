package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 查询供应商关联商品关系
 * @author zhangkangjian
 * @date 2018-08-01 10:12:08
 */
@ApiModel(value = "QueryRelSupplierProduct", description = "查询供应商关联商品关系")
public class QueryRelSupplierProduct {
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
     * 类型名称
     */
    @ApiModelProperty(name = "类型名称", value = "categoryName")
    private String categoryName;

    /**
     * 商品的code
     */
    @ApiModelProperty(name = "商品的code", value = "productCode")
    private String productCode;
    /**
     * 商品的名称
     */
    @ApiModelProperty(name = "商品的名称", value = "productName")
    private String productName;
    /**
     * 商品类型（注：PRODUCT零配件，EQUIPMENT物料）
     */
    @ApiModelProperty(name = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", value = "productType")
    private String productType;
    /**
     * 偏移量
     */
    @ApiModelProperty(name = "偏移量", value = "offset", hidden = true)
    private Integer offset;
    /**
     * 每页显示的条数
     */
    @ApiModelProperty(name = "每页显示的条数", value = "pageSize", hidden = true)
    private Integer pageSize;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public enum ProductTypeEnum {
        FITTINGS("FITTINGS"),
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

}
