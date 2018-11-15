package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品库存列表展示用
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "StockBizStockDetailDTO", description = "商品库存列表展示用")
public class StockBizStockDetailDTO extends IdEntity {

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
     * 问题件库存
     */
    @ApiModelProperty(name = "problemStock", value = "问题件库存数量")
    private Long problemStock;
    /**
     * 可用库存
     */
    @ApiModelProperty(name = "validStock", value = "可用库存")
    private Long validStock;
    /**
     * 计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "计量单位")
    private String productUnit;
    /**
     * 商品分类名称，多级逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称，多级逗号隔开")
    private String productCategoryname;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "instockTime", value = "入库时间", hidden = true)
    private Date instockTime;
    /**
     * 出库时间
     */
    @ApiModelProperty(name = "outstockTime", value = "出库时间", hidden = true)
    private Date outstockTime;

    /**
     * 供应商code
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商code", hidden = true)
    private String supplierNo;

    /**
     * 供应商name
     */
    @ApiModelProperty(name = "supplierName", value = "供应商name", hidden = true)
    private String supplierName;
    /**
     * 成本价格
     */
    @ApiModelProperty(name = "costPrice", value = "成本价格")
    private BigDecimal costPrice;
    @ApiModelProperty(name = "carpartsImage", value = "零配件图片在服务端的相对路径", required = true)
    private String carpartsImage;

    @ApiModelProperty(name = "carpartsMarkno", value = "零配件代码", required = true)
    private String carpartsMarkno;
    @ApiModelProperty(name = "usedAmount", value = "单车使用配件的个数", required = true)
    private Long usedAmount;

    public String getCarpartsImage() {
        return carpartsImage;
    }

    public void setCarpartsImage(String carpartsImage) {
        this.carpartsImage = carpartsImage;
    }

    public String getCarpartsMarkno() {
        return carpartsMarkno;
    }

    public void setCarpartsMarkno(String carpartsMarkno) {
        this.carpartsMarkno = carpartsMarkno;
    }

    public Long getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
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

    public String getProductCategoryname() {
        return productCategoryname;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

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

    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public Date getOutstockTime() {
        return outstockTime;
    }

    public void setOutstockTime(Date outstockTime) {
        this.outstockTime = outstockTime;
    }

    public Long getValidStock() {
        return validStock;
    }

    public void setValidStock(Long validStock) {
        this.validStock = validStock;
    }
}