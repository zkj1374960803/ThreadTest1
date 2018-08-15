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
}