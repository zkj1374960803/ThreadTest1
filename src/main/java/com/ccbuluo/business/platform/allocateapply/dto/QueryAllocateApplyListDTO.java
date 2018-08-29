package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 查询申请单列表
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "QueryAllocateApplyListDTO", description = "查询申请单列表")
public class QueryAllocateApplyListDTO {

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
     * 申请人姓名
     */
    @ApiModelProperty(name = "applyerName", value = "申请人姓名")
    private String applyerName;

    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "applyType", value = "")
    private String applyType;

    /**
     * 申请来源
     */
    @ApiModelProperty(name = "orgType", value = "申请来源(SERVICECENTER(服务中心),CUSTMANAGER(客户经理)PLATFORM(售后平台);)")
    private String orgType;
    @ApiModelProperty(name = "orgName", value = "申请机构")
    private String orgName;
    @ApiModelProperty(name = "applyorgNo", value = "申请机构")
    private String applyorgNo;
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)")
    private String processType;


    @ApiModelProperty(name = "outstockOrgno", value = "出库机构code")
    private String outstockOrgno;

    @ApiModelProperty(name = "outstockOrgname", value = "出库机构name")
    private String outstockOrgname;

    @ApiModelProperty(name = "instockOrgno", value = "入库机构")
    private String instockOrgno;

    @ApiModelProperty(name = "processOrgno", value = "入库机构")
    private String processOrgno;

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
     * 仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "仓库编号", hidden = true)
    private Date inRepositoryNo;

    public String getProcessOrgno() {
        return processOrgno;
    }

    public void setProcessOrgno(String processOrgno) {
        this.processOrgno = processOrgno;
    }

    public String getInstockOrgno() {
        return instockOrgno;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public String getOutstockOrgname() {
        return outstockOrgname;
    }

    public void setOutstockOrgname(String outstockOrgname) {
        this.outstockOrgname = outstockOrgname;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getApplyorgNo() {
        return applyorgNo;
    }

    public void setApplyorgNo(String applyorgNo) {
        this.applyorgNo = applyorgNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
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

    public Long getApplyTime() {
        return createTime.getTime();
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
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

    public Date getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(Date inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }
}