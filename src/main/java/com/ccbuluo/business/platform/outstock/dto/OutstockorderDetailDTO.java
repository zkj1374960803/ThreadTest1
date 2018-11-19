package com.ccbuluo.business.platform.outstock.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 出库单详情实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "出库单详情实体", description = "出库单详情实体")
public class OutstockorderDetailDTO extends AftersaleCommonEntity{
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
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "productName", value = "商品名称")
    private String productName;
    /**
     * 商品类型(物料、零配件)
     */
    @ApiModelProperty(name = "productType", value = "商品类型(物料、零配件)")
    private String productType;
    /**
     * 商品分类名称,多级逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,多级逗号隔开")
    private String productCategoryname;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商编号")
    private String supplierNo;
    /**
     * 供应商名字
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名字")
    private String supplierName;
    /**
     * 出库数量
     */
    @ApiModelProperty(name = "outstockNum", value = "出库数量")
    private Long outstockNum;
    /**
     * 出库仓库
     */
    @ApiModelProperty(name = "outRepositoryNo", value = "出库仓库")
    private String outRepositoryNo;
    /**
     * 出库仓库
     */
    @ApiModelProperty(name = "outRepositoryName", value = "出库仓库")
    private String outRepositoryName;
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
     * 成本总价
     */
    @ApiModelProperty(name = "costTotalPrice", value = "成本总价")
    private BigDecimal costTotalPrice;
    /**
     * 销售总价
     */
    @ApiModelProperty(name = "actualTotalPrice", value = "销售总价")
    private BigDecimal actualTotalPrice;
    private String unitName;
    @ApiModelProperty(name = "carpartsImage",value = "零配件图片在服务端的相对路径",required = true
    )
    private String carpartsImage;
    @ApiModelProperty(name = "carpartsMarkno",value = "零配件代码",required = true
    )
    private String carpartsMarkno;
    @ApiModelProperty(name = "usedAmount",value = "单车使用配件的个数",required = true
    )
    private Long usedAmount;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

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

    public String getOutRepositoryNo() {
        return outRepositoryNo;
    }

    public void setOutRepositoryNo(String outRepositoryNo) {
        this.outRepositoryNo = outRepositoryNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getOutRepositoryName() {
        return outRepositoryName;
    }

    public void setOutRepositoryName(String outRepositoryName) {
        this.outRepositoryName = outRepositoryName;
    }

    public BigDecimal getCostTotalPrice() {
        return costTotalPrice;
    }

    public void setCostTotalPrice(BigDecimal costTotalPrice) {
        this.costTotalPrice = costTotalPrice;
    }

    public BigDecimal getActualTotalPrice() {
        return actualTotalPrice;
    }

    public void setActualTotalPrice(BigDecimal actualTotalPrice) {
        this.actualTotalPrice = actualTotalPrice;
    }


}