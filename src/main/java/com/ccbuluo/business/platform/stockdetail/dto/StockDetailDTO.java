package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 某个商品库存详情用
 * @author weijb
 * @date 2018-08-20 10:43:11
 * @version V1.0.0
 */
@ApiModel(value = "StockDetailDTO", description = "某个商品库存详情用")
public class StockDetailDTO extends IdEntity {

    /**
     * 交易批次号,可以是调拨申请的单号或维修销售的服务单号
     */
    @ApiModelProperty(name = "tradeNo", value = "交易批次号,可以是调拨申请的单号或维修销售的服务单号")
    private String tradeNo;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "instockTime", value = "入库时间")
    private Date instockTime;

    /**
     * 供应商code
     */
    @ApiModelProperty(name = "supplierCode", value = "供应商code")
    private String supplierNo;

    /**
     * 问题件库存
     */
    @ApiModelProperty(name = "problemStock", value = "问题件库存数量")
    private Long problemStock;

    /**
     * 供应商名字
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名字")
    private String supplierName;

    /**
     * 计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "计量单位")
    private String productUnit;

    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 机构名称
     */
    @ApiModelProperty(name = "orgName", value = "机构名称")
    private String orgName;
    /**
     * 机构编号
     */
    @ApiModelProperty(name = "orgNo", value = "机构编号")
    private String orgNo;
    /**
     * 机构类型
     */
    @ApiModelProperty(name = "orgType", value = "机构类型")
    private String orgType;

    /**
     * 成本价格
     */
    @ApiModelProperty(name = "costPrice", value = "成本价格")
    private BigDecimal costPrice;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getOrgNoAndSupplierNo() {
        return orgNo + supplierNo;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
}