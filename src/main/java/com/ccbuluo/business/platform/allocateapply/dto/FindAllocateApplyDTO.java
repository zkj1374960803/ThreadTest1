package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 查询申请单详情
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "FindAllocateApplyDTO", description = "查询申请单详情")
public class FindAllocateApplyDTO {

    /**
     * 申请单的编号
     */
    @ApiModelProperty(name = "applyNo", value = "申请单的编号", hidden = true)
    private String applyNo;

    /**
     * 待处理、申请撤销、等待付款、等待发货、（平台待出入库只用在平台端）、等待收货、确认收货、申请完成
     */
    @ApiModelProperty(name = "applyStatus", value = "10待处理、15申请撤销、20等待付款、30等待发货、40（平台待出入库只用在平台端）、50等待收货、60确认收货、70申请完成")
    private String applyStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 申请人的uuid
     */
    @ApiModelProperty(name = "applyerName", value = "申请人姓名")
    private String applyerName;

    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)")
    private String processType;

    /**
     * 出库的组织架构
     */
    @ApiModelProperty(name = "outstockOrgno", value = "出库的组织架构(采购类型时，不必填)")
    private String outstockOrgno;
    /**
     * 出库的组织架构
     */
    @ApiModelProperty(name = "outstockOrgName", value = "出库的组织架构名称")
    private String outstockOrgName;
    /**
     * 入库组织架构
     */
    @ApiModelProperty(name = "instockOrgno", value = "入库组织架构(采购类型时，不必填)")
    private String instockOrgno;
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "入库仓库编号")
    private String inRepositoryNo;
    /**
     * 入库组织架构
     */
    @ApiModelProperty(name = "instockOrgName", value = "入库的组织架构名称")
    private String instockOrgName;
    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "storehouseName", value = "仓库名称")
    private String storehouseName;
    /**
     * 仓库地址
     */
    @ApiModelProperty(name = "storehouseAddress", value = "仓库地址")
    private String storehouseAddress;


    /**
     * 申请商品详情列表
     */
    @ApiModelProperty(name = "ueryAllocateapplyDetailDTO", value = "申请商品详情列表")
    private List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTO;
    /**
     * 申请来源机构
     */
    @ApiModelProperty(name = "applyorgNo", value = "申请来源(申请详情用不到，处理详情需要)")
    private String applyorgNo;

    /**
     * 申请来源机构名称
     */
    @ApiModelProperty(name = "applyorgName", value = "申请来源机构名称(申请详情用不到，处理详情需要)")
    private String applyorgName;
    @ApiModelProperty(name = "orgType", value = "申请来源(服务中心SERVICECENTER 客户经理CUSTMANAGER 售后平台PLATFORM)")
    private String orgType;
    /**
     * 申请处理机构类型
     */
    @ApiModelProperty(name = "processOrgtype", value = "申请处理机构类型")
    private String processOrgtype;

    /**
     * 申请处理机构编号
     */
    @ApiModelProperty(name = "processOrgno", value = "申请处理机构编号")
    private String processOrgno;

    /**
     * 申请的类型：平级调拨、平台调拨
     */
    @ApiModelProperty(name = "processOrgno", value = "申请的类型：平级调拨、平台调拨")
    private String applyType;

    /**
     * 出库人
     */
    @ApiModelProperty(name = "outstockOperatorName", value = "出库人")
    private String outstockOperatorName;

    /**
     *  出库时间
     */
    @ApiModelProperty(name = "outstockTime", value = "出库时间")
    private Date outstockTime;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "instockTime", value = "入库时间", hidden = true)
    private Date instockTime;

    /**
     * 入库人
     */
    @ApiModelProperty(name = "instockOperator", value = "入库人", hidden = true)
    private String instockOperatorName;

    /**
     * 物流单号
     */
    @ApiModelProperty(name = "transportorderNo", value = "物流单号", hidden = true)
    private String transportorderNo;

    @ApiModelProperty(name = "refundAddress", value = "收货地址")
    private String refundAddress;
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "costTotalPrice", value = "成本总价")
    private BigDecimal costTotalPrice;

    @ApiModelProperty(name = "sellTotalPrice", value = "销售总价")
    private BigDecimal sellTotalPrice;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRefundAddress() {
        return refundAddress;
    }

    public void setRefundAddress(String refundAddress) {
        this.refundAddress = refundAddress;
    }


    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getProcessOrgtype() {
        return processOrgtype;
    }

    public void setProcessOrgtype(String processOrgtype) {
        this.processOrgtype = processOrgtype;
    }

    public String getProcessOrgno() {
        return processOrgno;
    }

    public void setProcessOrgno(String processOrgno) {
        this.processOrgno = processOrgno;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyorgName() {
        return applyorgName;
    }

    public void setApplyorgName(String applyorgName) {
        this.applyorgName = applyorgName;
    }

    public String getApplyorgNo() {
        return applyorgNo;
    }

    public void setApplyorgNo(String applyorgNo) {
        this.applyorgNo = applyorgNo;
    }

    public List<QueryAllocateapplyDetailDTO> getQueryAllocateapplyDetailDTO() {
        return queryAllocateapplyDetailDTO;
    }

    public void setQueryAllocateapplyDetailDTO(List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTO) {
        this.queryAllocateapplyDetailDTO = queryAllocateapplyDetailDTO;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public Long getApplyTime() {
        return createTime.getTime();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public String getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public String getOutstockOrgName() {
        return outstockOrgName;
    }

    public void setOutstockOrgName(String outstockOrgName) {
        this.outstockOrgName = outstockOrgName;
    }

    public String getInstockOrgno() {
        return instockOrgno;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public String getInstockOrgName() {
        return instockOrgName;
    }

    public void setInstockOrgName(String instockOrgName) {
        this.instockOrgName = instockOrgName;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public String getStorehouseAddress() {
        return storehouseAddress;
    }

    public void setStorehouseAddress(String storehouseAddress) {
        this.storehouseAddress = storehouseAddress;
    }

    public String getOutstockOperatorName() {
        return outstockOperatorName;
    }

    public void setOutstockOperatorName(String outstockOperatorName) {
        this.outstockOperatorName = outstockOperatorName;
    }

    public Date getOutstockTime() {
        return outstockTime;
    }

    public void setOutstockTime(Date outstockTime) {
        this.outstockTime = outstockTime;
    }

    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public String getInstockOperatorName() {
        return instockOperatorName;
    }

    public void setInstockOperatorName(String instockOperatorName) {
        this.instockOperatorName = instockOperatorName;
    }

    public String getTransportorderNo() {
        return transportorderNo;
    }

    public void setTransportorderNo(String transportorderNo) {
        this.transportorderNo = transportorderNo;
    }

    public BigDecimal getCostTotalPrice() {
        return costTotalPrice;
    }

    public void setCostTotalPrice(BigDecimal costTotalPrice) {
        this.costTotalPrice = costTotalPrice;
    }

    public BigDecimal getSellTotalPrice() {
        return sellTotalPrice;
    }

    public void setSellTotalPrice(BigDecimal sellTotalPrice) {
        this.sellTotalPrice = sellTotalPrice;
    }
}