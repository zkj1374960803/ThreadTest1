package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询可调拨库存列表
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "FindStockListDTO", description = "查询可调拨库存列表")
public class FindStockListDTO{

    private Long id;
    /**
     * 供应商code
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商code", hidden = true)
    private String supplierNo;
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
     * 计量单位
     */
    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;
    /**
     * 商品类型(物料、零配件)
     */
    @ApiModelProperty(name = "productType", value = "商品类型(物料、零配件)", hidden = true)
    private String productType;
    /**
     * 可用库存
     */
    @ApiModelProperty(name = "total", value = "可用库存")
    private Integer total;
    /**
     * 分类的code
     */
    @ApiModelProperty(name = "categoryCode", value = "分类的code", hidden = true)
    private Integer categoryCode;

    /**
     * 偏移量
     */
    @ApiModelProperty(name = "offset", value = "偏移量", hidden = true)
    private Integer offset;
    /**
     * 每页显示的数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示的数量", hidden = true)
    private Integer pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getProductNo() {
        return productNo;
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
}