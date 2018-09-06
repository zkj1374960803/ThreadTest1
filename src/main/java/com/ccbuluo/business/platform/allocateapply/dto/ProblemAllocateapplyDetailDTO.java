package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 问题件申请
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "ProblemAllocateapplyDetailDTO", description = "问题件申请")
public class ProblemAllocateapplyDetailDTO {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 调拨申请单编号
     */
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号")
    private String applyNo;
    /**
     * 申请类型
     */
    @ApiModelProperty(name = "applyType", value = "申请类型（PLATFORM，SAMELEVEL）")
    private String applyType;
    /**
     * 申请状态
     */
    @ApiModelProperty(name = "applyStatus", value = "申请状态")
    private String applyStatus;
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
     * 出库人
     */
    @ApiModelProperty(name = "outstockOperator", value = "出库人", hidden = true)
    private String outstockOperator;

    /**
     * 入库人
     */
    @ApiModelProperty(name = "instockOperator", value = "入库人", hidden = true)
    private String instockOperator;

    /**
     * 物流单号
     */
    @ApiModelProperty(name = "transportorderNo", value = "物流单号", hidden = true)
    private String transportorderNo;

    /**
     * 出库数量
     */
    @ApiModelProperty(name = "outstockNum", value = "出库数量", hidden = true)
    private Long outstockNum;
    /**
     * 入库数量
     */
    @ApiModelProperty(name = "instockNum", value = "入库数量", hidden = true)
    private Long instockNum;

    /**
     * 付款总额
     */
    @ApiModelProperty(name = "totalPrice", value = "付款总额", hidden = true)
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
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

    public String getOutstockOperator() {
        return outstockOperator;
    }

    public void setOutstockOperator(String outstockOperator) {
        this.outstockOperator = outstockOperator;
    }

    public String getInstockOperator() {
        return instockOperator;
    }

    public void setInstockOperator(String instockOperator) {
        this.instockOperator = instockOperator;
    }

    public String getTransportorderNo() {
        return transportorderNo;
    }

    public void setTransportorderNo(String transportorderNo) {
        this.transportorderNo = transportorderNo;
    }

    public Long getOutstockNum() {
        return outstockNum;
    }

    public void setOutstockNum(Long outstockNum) {
        this.outstockNum = outstockNum;
    }

    public Long getInstockNum() {
        return instockNum;
    }

    public void setInstockNum(Long instockNum) {
        this.instockNum = instockNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}