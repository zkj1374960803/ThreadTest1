package com.ccbuluo.business.platform.supplier.dto;

import com.ccbuluo.core.annotation.validate.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 查询供应商关联商品关系
 * @author zhangkangjian
 * @date 2018-08-01 10:12:08
 */
@ApiModel(value = "QueryRelSupplierProduct", description = "查询供应商关联商品关系")
public class QueryRelSupplierProductDTO {
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
     * 类型名称
     */
    @ApiModelProperty(name = "categoryName", value = "类型名称")
    private String categoryName;

    /**
     * 商品的code
     */
    @ApiModelProperty(name = "productCode", value = "商品的code", hidden = true)
    private String productCode;
    /**
     * 供应商代码
     */
    @ApiModelProperty(name = "supplierMarkno", value = "供应商代码", hidden = true)
    private String supplierMarkno;
    /**
     * 零配件代码
     */
    @ApiModelProperty(name = "carpartsMarkno", value = "零配件代码", hidden = true)
    private String carpartsMarkno;
    /**
     * 商品的名称
     */
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    /**
     * 商品类型（注：PRODUCT零配件，EQUIPMENT物料）
     */
    @ValidatePattern(regexp = {"FITTINGS", "EQUIPMENT"}, excludeNull = true)
    @ApiModelProperty(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）")
    private String productType;
    /**
     * 偏移量
     */
    @ApiModelProperty(name = "offset", value = "偏移量", hidden = true)
    private Integer offset = 0;
    /**
     * 每页显示的条数
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示的条数", hidden = true)
    private Integer pageSize = 10;

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

    public String getSupplierMarkno() {
        return supplierMarkno;
    }

    public void setSupplierMarkno(String supplierMarkno) {
        this.supplierMarkno = supplierMarkno;
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

    public String getCarpartsMarkno() {
        return carpartsMarkno;
    }

    public void setCarpartsMarkno(String carpartsMarkno) {
        this.carpartsMarkno = carpartsMarkno;
    }
}
