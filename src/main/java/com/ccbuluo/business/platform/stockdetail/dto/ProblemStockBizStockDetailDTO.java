package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 查询问题库存详情展示用
 * @author weijb
 * @date 2018-08-23 15:43:11
 * @version V1.0.0
 */
@ApiModel(value = "ProblemStockBizStockDetailDTO", description = "查询问题库存详情展示用")
public class ProblemStockBizStockDetailDTO extends IdEntity {

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
     * 商品分类名称,多级逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,多级逗号隔开")
    private String productCategoryname;
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
    /**
     * 某商品问题件详情
     */
    @ApiModelProperty(name = "problemDetailList", value = "某商品问题件详情")
    private List<StockDetailDTO> problemDetailList;

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

    public String getProductCategoryname() {
        return productCategoryname;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
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

    public List<StockDetailDTO> getProblemDetailList() {
        return problemDetailList;
    }

    public void setProblemDetailList(List<StockDetailDTO> problemDetailList) {
        this.problemDetailList = problemDetailList;
    }
}