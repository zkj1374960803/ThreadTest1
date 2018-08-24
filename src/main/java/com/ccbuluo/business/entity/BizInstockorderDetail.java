package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单详情实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "入库单详情实体", description = "入库单详情实体")
public class BizInstockorderDetail extends AftersaleCommonEntity{
    /**
     * 入库单编号
     */
    @ApiModelProperty(name = "instockOrderno", value = "入库单编号")
    private String instockOrderno;
    /**
     * 入库计划id
     */
    @ApiModelProperty(name = "instockPlanid", value = "入库计划id")
    private Long instockPlanid;
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
     * 商品类型
     */
    @ApiModelProperty(name = "productType", value = "商品类型")
    private String productType;
    /**
     * 商品分类名称,分类全路径，多个逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,分类全路径，多个逗号隔开")
    private String productCategoryname;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierNo", value = "")
    private String supplierNo;
    /**
     * 入库数量
     */
    @ApiModelProperty(name = "instockNum", value = "入库数量")
    private Long instockNum;
    /**
     * 问题件数量
     */
    @ApiModelProperty(name = "problemNum", value = "问题件数量")
    private Long problemNum;
    /**
     * 库存类型
     */
    @ApiModelProperty(name = "stockType", value = "库存类型")
    private String stockType;
    /**
     * 计量单位
     */
    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice;
    /**
     * 库存明细id
     */
    @ApiModelProperty(name = "stockId", value = "库存明细id")
    private Long stockId;
    private String remark;

    public void setInstockOrderno(String instockOrderno) {
        this.instockOrderno = instockOrderno;
    }

    public String getInstockOrderno() {
        return this.instockOrderno;
    }

    public void setInstockPlanid(Long instockPlanid) {
        this.instockPlanid = instockPlanid;
    }

    public Long getInstockPlanid() {
        return this.instockPlanid;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return this.productNo;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductCategoryname() {
        return this.productCategoryname;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierNo() {
        return this.supplierNo;
    }

    public void setInstockNum(Long instockNum) {
        this.instockNum = instockNum;
    }

    public Long getInstockNum() {
        return this.instockNum;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getProblemNum() {
        return problemNum;
    }

    public void setProblemNum(Long problemNum) {
        this.problemNum = problemNum;
    }
}