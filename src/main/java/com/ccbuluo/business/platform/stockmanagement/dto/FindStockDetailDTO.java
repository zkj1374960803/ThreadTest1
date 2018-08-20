package com.ccbuluo.business.platform.stockmanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询可调拨库存列表
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "FindStockDetailDTO", description = "查询调拨库存详情")
public class FindStockDetailDTO {
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
     * 计量单位
     */
    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;
    /**
     * 商品类型(物料、零配件)
     */
    @ApiModelProperty(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)")
    private String productType;
    /**
     * 总库存
     */
    @ApiModelProperty(name = "totalStock", value = "可用库存")
    private Integer totalStock;
    @ApiModelProperty(name = "normalPiece", value = "正产件")
    private FindProductDetailDTO normalPiece;
    @ApiModelProperty(name = "problemPiece", value = "问题件")
    private FindProductDetailDTO problemPiece;
    @ApiModelProperty(name = "damagedPiece", value = "损坏件")
    private FindProductDetailDTO damagedPiece;


    public FindProductDetailDTO getNormalPiece() {
        return normalPiece;
    }

    public void setNormalPiece(FindProductDetailDTO normalPiece) {
        this.normalPiece = normalPiece;
    }

    public FindProductDetailDTO getProblemPiece() {
        return problemPiece;
    }

    public void setProblemPiece(FindProductDetailDTO problemPiece) {
        this.problemPiece = problemPiece;
    }

    public FindProductDetailDTO getDamagedPiece() {
        return damagedPiece;
    }

    public void setDamagedPiece(FindProductDetailDTO damagedPiece) {
        this.damagedPiece = damagedPiece;
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

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}