package com.ccbuluo.business.platform.allocateapply.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 物料和零配件调拨的申请实体
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料和零配件调拨的申请实体", description = "物料和零配件调拨的申请")
public class BizAllocateApply {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 申请单的编号
     */
    @ApiModelProperty(name = "applyNo", value = "申请单的编号")
    private String applyNo;
    /**
     * 发起申请的机构编号
     */
    @ApiModelProperty(name = "applyorgNo", value = "发起申请的机构编号")
    private String applyorgNo;
    /**
     * 申请人的uuid
     */
    @ApiModelProperty(name = "applyer", value = "申请人的uuid")
    private String applyer;
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "instockOrgno", value = "入库仓库编号")
    private String instockOrgno;
    /**
     * 出库仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "出库仓库编号")
    private String inRepositoryNo;
    /**
     * 出库的机构的类型
     */
    @ApiModelProperty(name = "outstockOrgtype", value = "出库的机构的类型")
    private String outstockOrgtype;
    /**
     * 出库的组织架构
     */
    @ApiModelProperty(name = "outstockOrgno", value = "出库的组织架构")
    private String outstockOrgno;
    /**
     * 处理申请人的uuid
     */
    @ApiModelProperty(name = "applyProcessor", value = "处理申请人的uuid")
    private String applyProcessor;
    /**
     * 处理申请的时间
     */
    @ApiModelProperty(name = "processTime", value = "处理申请的时间")
    private Date processTime;
    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型：调拨、采购")
    private String processType;
    /**
     * 待处理、申请撤销、等待付款、等待发货、（平台待出入库只用在平台端）、等待收货、确认收货、申请完成
     */
    @ApiModelProperty(name = "applyStatus", value = "待处理、申请撤销、等待付款、等待发货、（平台待出入库只用在平台端）、等待收货、确认收货、申请完成")
    private String applyStatus;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人", hidden = true)
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人", hidden = true)
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间", hidden = true)
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识", hidden = true)
    private Long deleteFlag = 0L;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注", hidden = true)
    private String remark;
    /**
     * 申请商品详情列表
     */
    @ApiModelProperty(name = "allocateapplyDetailList", value = "申请商品详情列表")
    private List<BizAllocateapplyDetail> allocateapplyDetailList;

    public List<BizAllocateapplyDetail> getAllocateapplyDetailList() {
        return allocateapplyDetailList;
    }

    public void setAllocateapplyDetailList(List<BizAllocateapplyDetail> allocateapplyDetailList) {
        this.allocateapplyDetailList = allocateapplyDetailList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyNo() {
        return this.applyNo;
    }

    public void setApplyorgNo(String applyorgNo) {
        this.applyorgNo = applyorgNo;
    }

    public String getApplyorgNo() {
        return this.applyorgNo;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getApplyer() {
        return this.applyer;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public String getInstockOrgno() {
        return this.instockOrgno;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public String getInRepositoryNo() {
        return this.inRepositoryNo;
    }

    public void setOutstockOrgtype(String outstockOrgtype) {
        this.outstockOrgtype = outstockOrgtype;
    }

    public String getOutstockOrgtype() {
        return this.outstockOrgtype;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public String getOutstockOrgno() {
        return this.outstockOrgno;
    }

    public void setApplyProcessor(String applyProcessor) {
        this.applyProcessor = applyProcessor;
    }

    public String getApplyProcessor() {
        return this.applyProcessor;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Date getProcessTime() {
        return this.processTime;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessType() {
        return this.processType;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getApplyStatus() {
        return this.applyStatus;
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