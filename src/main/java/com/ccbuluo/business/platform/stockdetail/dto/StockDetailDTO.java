package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 某个商品库存详情用
 * @author weijb
 * @date 2018-08-20 10:43:11
 * @version V1.0.0
 */
@ApiModel(value = "StockDetailDTO", description = "某个商品库存详情用")
public class StockDetailDTO extends IdEntity {

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
     * 商品分类
     */
    @ApiModelProperty(name = "productType", value = "商品分类")
    private String productType;
    /**
     * 商品数量（总数量）
     */
    @ApiModelProperty(name = "problemStock", value = "商品数量（总数量）")
    private Long productNum;
    /**
     * 问题件数量（总数量）
     */
    @ApiModelProperty(name = "problemStock", value = "商品数量（总数量）")
    private Long problemNum;

    /**
     * 问题件库存
     */
    @ApiModelProperty(name = "problemStock", value = "问题件库存数量")
    private Long problemStock;
    /**
     * 计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "计量单位")
    private String productUnit;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Long getProblemStock() {
        return problemStock;
    }

    public void setProblemStock(Long problemStock) {
        this.problemStock = problemStock;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getUnitName() {
        String unitName = "";
        if(StringUtils.isNotBlank(productUnit)){
            unitName = ProductUnitEnum.valueOf(productUnit).getLabel();
        }
        return unitName;
    }
}