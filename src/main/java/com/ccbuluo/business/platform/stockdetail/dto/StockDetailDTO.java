package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

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
     * 计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "计量单位")
    private String productUnit;

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
}