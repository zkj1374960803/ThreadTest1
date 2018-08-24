package com.ccbuluo.business.entity;

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
@ApiModel(value = "BizAllocateApply", description = "物料和零配件调拨的申请")
public class BizAllocateApply {

    @ApiModelProperty(name = "processOrgtype", value = "申请处理机构类型", hidden = true)
    private String processOrgtype;

    @ApiModelProperty(name = "processOrgno", value = "申请处理机构编号", hidden = true)
    private String processOrgno;

    @ApiModelProperty(name = "applyType", value = "申请的类型：SAMELEVEL平级调拨、PLATFORM平台调拨", hidden = true)
    private String applyType;

    @ApiModelProperty(name = "applyerName", value = "申请人的姓名", hidden = true)
    private String applyerName;

    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "", hidden = true)
    private Long id;
    /**
     * 申请单的编号
     */
    @ApiModelProperty(name = "applyNo", value = "申请单的编号", hidden = true)
    private String applyNo;
    /**
     * 发起申请的机构编号
     */
    @ApiModelProperty(name = "applyorgNo", value = "发起申请的机构编号", hidden = true)
    private String applyorgNo;
    /**
     * 申请人的uuid
     */
    @ApiModelProperty(name = "applyer", value = "申请人的uuid", hidden = true)
    private String applyer;
    /**
     * 入库组织架构编号
     */
    @ApiModelProperty(name = "instockOrgno", value = "入库组织架构编号", hidden = true)
    private String instockOrgno;
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "入库仓库编号")
    private String inRepositoryNo;
    /**
     * 出库的机构的类型
     */
    @ApiModelProperty(name = "outstockOrgtype", value = "出库的机构的类型(采购类型，不必填)", hidden = true)
    private String outstockOrgtype;
    /**
     * 出库的组织架构
     */
    @ApiModelProperty(name = "outstockOrgno", value = "出库的组织架构(采购类型时，不必填)")
    private String outstockOrgno;
    /**
     * 处理申请人的uuid
     */
    @ApiModelProperty(name = "applyProcessor", value = "处理申请人的uuid", hidden = true)
    private String applyProcessor;
    /**
     * 处理申请的时间
     */
    @ApiModelProperty(name = "processTime", value = "处理申请的时间", hidden = true)
    private Date processTime;
    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)")
    private String processType;
    /**
     * 待处理、申请撤销、等待付款、等待发货、（平台待出入库只用在平台端）、等待收货、确认收货、申请完成
     */
    @ApiModelProperty(name = "applyStatus", value = "", hidden = true)
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

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public void setAllocateapplyDetailList(List<BizAllocateapplyDetail> allocateapplyDetailList) {
        this.allocateapplyDetailList = allocateapplyDetailList;
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


    /**
     * 调拨申请类型枚举
     * @author weijb
     * @date 2018-08-14 18:41:05
     */
    public enum AllocateApplyTypeEnum {
        PURCHASE("平台采购"),PLATFORMALLOCATE("平台调拨"), SAMELEVEL("平级调拨"),DIRECTALLOCATE("平台直发"),
        BARTER("调换"),REFUND("退款");

        AllocateApplyTypeEnum(String label){
            this.label = label;
        }

        private String label;

        public String getLabel(){
            return label;
        }
    }


    /**
     * 申请状态枚举
     *
     * @author zhangkangjian
     * @date 2018-08-10 14:01:49
     */
    public enum ApplyStatusEnum {
        PENDING("10", "申请提交"), CANCEL("15", "申请撤销"), WAITINGPAYMENT("20", "等待付款"),
        WAITDELIVERY("30", "等待发货"), OUTSTORE("40", "平台出库"), INSTORE("50", "平台入库"),
        WAITINGRECEIPT("60", "等待收货"), CONFIRMRECEIPT("70", "确认收货"), APPLICATIONCOMPLETED("80", "申请完成");
        // 成员变量
        private String key;
        private String value;

        // 构造方法
        private ApplyStatusEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

    }
    /**
     * 退换货申请状态枚举
     * @author zhangkangjian
     * @date 2018-08-10 14:01:49
     */
    public enum ReturnApplyStatusEnum {

        PRODRETURNED("10", "申请提交"),PLATFORMOUTBOUND("20", "等待出库"),WAITINGREFUND("30", "等待退款"),REFUNDCOMPLETED("40", "退款完成"),

        REPLACEWAITIN("50", "更换待入库"),REPLACECOMPLETED("60", "更换完成")
        ;
        // 成员变量
        private String key;
        private String value;

        // 构造方法
        private ReturnApplyStatusEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 处理类型
     * @author zhangkangjian
     * @date 2018-08-10 14:01:49
     */
    public enum ProcessTypeEnum {
        TRANSFER("调拨"),PURCHASE("采购"),BARTER("更换"),REFUND("退款");
        ProcessTypeEnum(String label){
            this.label = label;
        }
        private String label;
        public String getLabel(){
            return label;
        }
    }

}