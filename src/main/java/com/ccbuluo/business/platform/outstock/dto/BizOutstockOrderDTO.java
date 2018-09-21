package com.ccbuluo.business.platform.outstock.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 出库单实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "出库单实体", description = "出库单实体")
public class BizOutstockOrderDTO extends AftersaleCommonEntity{
    /**
     * 出库单编号
     */
    @ApiModelProperty(name = "outstockorderNo", value = "出库单编号")
    private String outstockorderNo;
    /**
     * 仓库编号
     */
    @ApiModelProperty(name = "outRepositoryNo", value = "仓库编号")
    private String outRepositoryNo;
    /**
     * 出库人
     */
    @ApiModelProperty(name = "outstockOperator", value = "出库人")
    private String outstockOperator;
    /**
     * 出库单所属机构
     */
    @ApiModelProperty(name = "outstockOrgno", value = "出库单所属机构")
    private String outstockOrgno;
    /**
     * 交易单据号，可以是调拨申请单、维修销售单
     */
    @ApiModelProperty(name = "tradeDocno", value = "交易单据号，可以是调拨申请单、维修销售单")
    private String tradeDocno;
    /**
     * 出库类型
     */
    @ApiModelProperty(name = "outstockType", value = "出库类型")
    private String outstockType;
    /**
     * 出库时间
     */
    @ApiModelProperty(name = "outstockTime", value = "出库时间")
    private Date outstockTime;
    /**
     * 物流单号
     */
    @ApiModelProperty(name = "transportorderNo", value = "物流单号")
    private String transportorderNo;
    /**
     * 出库人
     */
    @ApiModelProperty(name = "outstockOperatorName", value = "出库人")
    private String outstockOperatorName;
    /**
     * 出库单详单
     */
    @ApiModelProperty(name = "outstockorderDetailDTOList", value = "出库单详单")
    private List<OutstockorderDetailDTO> outstockorderDetailDTOList;
    /**
     * 成本总价
     */
    @ApiModelProperty(name = "actualPrice", value = "成本总价")
    private BigDecimal costTotalPrice;
    /**
     * 销售总价
     */
    @ApiModelProperty(name = "actualPrice", value = "销售总价")
    private BigDecimal actualTotalPrice;


    public void setOutstockorderNo(String outstockorderNo) {
        this.outstockorderNo = outstockorderNo;
    }

    public String getOutstockorderNo() {
        return this.outstockorderNo;
    }

    public void setOutRepositoryNo(String outRepositoryNo) {
        this.outRepositoryNo = outRepositoryNo;
    }

    public String getOutRepositoryNo() {
        return this.outRepositoryNo;
    }

    public void setOutstockOperator(String outstockOperator) {
        this.outstockOperator = outstockOperator;
    }

    public String getOutstockOperator() {
        return this.outstockOperator;
    }

    public void setTradeDocno(String tradeDocno) {
        this.tradeDocno = tradeDocno;
    }

    public String getTradeDocno() {
        return this.tradeDocno;
    }

    public void setOutstockType(String outstockType) {
        this.outstockType = outstockType;
    }

    public String getOutstockType() {
        return this.outstockType;
    }

    public void setOutstockTime(Date outstockTime) {
        this.outstockTime = outstockTime;
    }

    public Date getOutstockTime() {
        return this.outstockTime;
    }

    public void setTransportorderNo(String transportorderNo) {
        this.transportorderNo = transportorderNo;
    }

    public String getTransportorderNo() {
        return this.transportorderNo;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public List<OutstockorderDetailDTO> getOutstockorderDetailDTOList() {
        return outstockorderDetailDTOList;
    }

    public void setOutstockorderDetailDTOList(List<OutstockorderDetailDTO> outstockorderDetailDTOList) {
        this.outstockorderDetailDTOList = outstockorderDetailDTOList;
    }

    public String getOutstockOperatorName() {
        return outstockOperatorName;
    }

    public void setOutstockOperatorName(String outstockOperatorName) {
        this.outstockOperatorName = outstockOperatorName;
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