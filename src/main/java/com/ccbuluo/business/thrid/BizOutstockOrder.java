package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class BizOutstockOrder {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
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
     * 复核完成
     */
    @ApiModelProperty(name = "checked", value = "复核完成")
    private Long checked;
    /**
     * 复核完成时间
     */
    @ApiModelProperty(name = "checkedTime", value = "复核完成时间")
    private Date checkedTime;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人")
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private Long deleteFlag;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

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

    public void setChecked(Long checked) {
        this.checked = checked;
    }

    public Long getChecked() {
        return this.checked;
    }

    public void setCheckedTime(Date checkedTime) {
        this.checkedTime = checkedTime;
    }

    public Date getCheckedTime() {
        return this.checkedTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}