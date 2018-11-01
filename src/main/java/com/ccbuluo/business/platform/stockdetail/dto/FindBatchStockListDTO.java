package com.ccbuluo.business.platform.stockdetail.dto;

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
@ApiModel(value = "FindBatchStockListDTO", description = "查询可调拨库存列表")
public class FindBatchStockListDTO {

    private Long id;
    @ApiModelProperty(name = "createTime", value = "入库时间")
    private Date createTime;
    @ApiModelProperty(name = "tradeNo", value = "入库单号")
    private String tradeNo;
    @ApiModelProperty(name = "supplierNo", value = "供货商code")
    private String supplierNo;
    @ApiModelProperty(name = "supplierName", value = "供货商名称")
    private String supplierName;
    @ApiModelProperty(name = "instockNum", value = "入库数量")
    private Long instockNum;
    @ApiModelProperty(name = "costPrice", value = "单价")
    private BigDecimal costPrice;
    /**
     * 所在仓库
     */
    @ApiModelProperty(name = "storehouseName", value = "所在仓库名称")
    private String storehouseName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInStockTime() {
        return createTime.getTime();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public Long getInstockNum() {
        return instockNum;
    }

    public void setInstockNum(Long instockNum) {
        this.instockNum = instockNum;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

}