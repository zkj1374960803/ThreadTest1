package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.ValidateMin;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库单详情实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "出库单详情实体", description = "出库单详情实体")
public class BizOutstockorderDetail extends AftersaleCommonEntity{
    /**
     * 出库单编号
     */
    @ApiModelProperty(name = "outstockOrderno", value = "出库单编号")
    private String outstockOrderno;
    /**
     * 出库计划id
     */
    @ApiModelProperty(name = "outstockPlanid", value = "出库计划id")
    private Long outstockPlanid;
    /**
     * 商品编号
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品名称
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "productName", value = "商品名称")
    private String productName;
    /**
     * 商品类型(物料、零配件)
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "productType", value = "商品类型(物料、零配件)")
    private String productType;
    /**
     * 商品分类名称,多级逗号隔开
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,多级逗号隔开")
    private String productCategoryname;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商编号")
    private String supplierNo;
    /**
     * 出库数量
     */
    @ValidateMin(0)
    @ValidateNotNull(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "outstockNum", value = "出库数量")
    private Long outstockNum;
    /**
     * 库存类型
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "stockType", value = "库存类型")
    private String stockType;
    /**
     * 计量单位,冗余展示用的汉字
     */
    @ApiModelProperty(name = "unit", value = "计量单位,冗余展示用的汉字")
    private String unit;
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice;
    /**
     * 销售单价
     */
    @ApiModelProperty(name = "actualPrice", value = "销售单价")
    private BigDecimal actualPrice;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setOutstockOrderno(String outstockOrderno) {
        this.outstockOrderno = outstockOrderno;
    }

    public String getOutstockOrderno() {
        return this.outstockOrderno;
    }

    public void setOutstockPlanid(Long outstockPlanid) {
        this.outstockPlanid = outstockPlanid;
    }

    public Long getOutstockPlanid() {
        return this.outstockPlanid;
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

    public void setOutstockNum(Long outstockNum) {
        this.outstockNum = outstockNum;
    }

    public Long getOutstockNum() {
        return this.outstockNum;
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

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getActualPrice() {
        return this.actualPrice;
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
}