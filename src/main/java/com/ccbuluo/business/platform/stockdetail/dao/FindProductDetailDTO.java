package com.ccbuluo.business.platform.stockdetail.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 查询库存的详情
 * @author zhangkangjian
 * @date 2018-08-20 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "FindProductDetailDTO", description = "查询库存的详情")
public class FindProductDetailDTO {

    private Long id;
    @ApiModelProperty(name = "validStock", value = "全部可用库存")
    private Long validStock = 0L;
    @ApiModelProperty(name = "transferInventory", value = "可调拨库存")
    private Long transferInventory = 0L;
    @ApiModelProperty(name = "occupyStock", value = "占用库存")
    private Long occupyStock = 0L;
    @ApiModelProperty(name = "totalStock", value = "总库存", hidden = true)
    private Long totalStock = 0L;
    @ApiModelProperty(name = "unit", value = "单位")
    private String unit;
    @ApiModelProperty(name = "totalAmount", value = "总价")
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidStock() {
        return validStock;
    }

    public void setValidStock(Long validStock) {
        this.validStock = validStock;
    }

    public Long getTransferInventory() {
        return transferInventory;
    }

    public void setTransferInventory(Long transferInventory) {
        this.transferInventory = transferInventory;
    }

    public Long getOccupyStock() {
        return occupyStock;
    }

    public void setOccupyStock(Long occupyStock) {
        this.occupyStock = occupyStock;
    }

    public Long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Long totalStock) {
        this.totalStock = totalStock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}