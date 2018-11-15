package com.ccbuluo.business.platform.adjust.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 盘库时列表展示用dto
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-14 16:54:57
 */
@ApiModel(value = "盘库时列表展示用实体", description = "盘库时列表展示用实体")
public class StockAdjustListDTO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品类型
     */
    @ApiModelProperty(name = "productType", value = "商品类型")
    private String productType;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "productName", value = "商品名称")
    private String productName;
    /**
     * 商品分类名称
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称")
    private String productCategoryname;
    /**
     * 应有数量
     */
    @ApiModelProperty(name = "dueNum", value = "应有数量")
    private Integer dueNum;
    /**
     * 实有数量
     */
    @ApiModelProperty(name = "actualNum", value = "实有数量")
    private Integer actualNum;
    /**
     * 差异数量
     */
    @ApiModelProperty(name = "differenceNum", value = "差异数量")
    private Integer differenceNum;

    @ApiModelProperty(name = "carpartsMarkno", value = "零配件代码", required = true)
    private String carpartsMarkno;
    @ApiModelProperty(name = "carpartsImage", value = "零配件图片", required = true)
    private String carpartsImage;


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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getDueNum() {
        return dueNum;
    }

    public void setDueNum(Integer dueNum) {
        this.dueNum = dueNum;
    }

    public Integer getActualNum() {
        return actualNum;
    }

    public void setActualNum(Integer actualNum) {
        this.actualNum = actualNum;
    }

    public String getProductCategoryname() {
        return productCategoryname;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public Integer getDifferenceNum() {
        return differenceNum;
    }

    public void setDifferenceNum(Integer differenceNum) {
        this.differenceNum = differenceNum;
    }

    public String getCarpartsMarkno() {
        return carpartsMarkno;
    }

    public void setCarpartsMarkno(String carpartsMarkno) {
        this.carpartsMarkno = carpartsMarkno;
    }

    public String getCarpartsImage() {
        return carpartsImage;
    }

    public void setCarpartsImage(String carpartsImage) {
        this.carpartsImage = carpartsImage;
    }
}
