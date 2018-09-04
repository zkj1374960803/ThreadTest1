package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 服务单派发给相关人的派发记录实体
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@ApiModel(value = "服务单派发给相关人的派发记录实体", description = "服务单派发给相关人的派发记录")
public class BizServiceDispatch {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 售后服务单的服务单号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "售后服务单的服务单号")
    private String serviceOrderno;
    /**
     * 是否是当前处理人
     */
    @ApiModelProperty(name = "currentFlag", value = "是否是当前处理人")
    private Integer currentFlag;
    /**
     * 前置的任务派发的id
     */
    @ApiModelProperty(name = "previousId", value = "前置的任务派发的id")
    private Long previousId;
    /**
     * 负责人所在机构的类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)
     */
    @ApiModelProperty(name = "processorOrgtype", value = "负责人所在机构的类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)")
    private String processorOrgtype;
    /**
     * 负责人所在机构的编号
     */
    @ApiModelProperty(name = "processorOrgno", value = "负责人所在机构的编号")
    private String processorOrgno;
    /**
     * 负责人的uuid
     */
    @ApiModelProperty(name = "processorUuid", value = "负责人的uuid")
    private String processorUuid;
    /**
     * 是否确认接收了该任务
     */
    @ApiModelProperty(name = "confirmed", value = "是否确认接收了该任务")
    private Integer confirmed;
    /**
     * 确认接收任务的时间
     */
    @ApiModelProperty(name = "confirmTime", value = "确认接收任务的时间")
    private Date confirmTime;
    /**
     * 转派给的机构的编号
     */
    @ApiModelProperty(name = "replaceOrgno", value = "转派给的机构的编号")
    private String replaceOrgno;
    /**
     * 转派给的机构的类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)
     */
    @ApiModelProperty(name = "replaceOrgtype", value = "转派给的机构的类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)")
    private String replaceOrgtype;
    /**
     * 售后服务任务转派给的人的uuid
     */
    @ApiModelProperty(name = "replaceUserid", value = "售后服务任务转派给的人的uuid")
    private String replaceUserid;
    /**
     * 售后服务任务转派给别人的时间
     */
    @ApiModelProperty(name = "dispatchTime", value = "售后服务任务转派给别人的时间")
    private Date dispatchTime;
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
    private Integer deleteFlag;
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

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
    }

    public String getServiceOrderno() {
        return this.serviceOrderno;
    }

    public void setCurrentFlag(Integer currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Integer getCurrentFlag() {
        return this.currentFlag;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    public Long getPreviousId() {
        return this.previousId;
    }

    public void setProcessorOrgtype(String processorOrgtype) {
        this.processorOrgtype = processorOrgtype;
    }

    public String getProcessorOrgtype() {
        return this.processorOrgtype;
    }

    public void setProcessorOrgno(String processorOrgno) {
        this.processorOrgno = processorOrgno;
    }

    public String getProcessorOrgno() {
        return this.processorOrgno;
    }

    public void setProcessorUuid(String processorUuid) {
        this.processorUuid = processorUuid;
    }

    public String getProcessorUuid() {
        return this.processorUuid;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getConfirmed() {
        return this.confirmed;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() {
        return this.confirmTime;
    }

    public void setReplaceOrgno(String replaceOrgno) {
        this.replaceOrgno = replaceOrgno;
    }

    public String getReplaceOrgno() {
        return this.replaceOrgno;
    }

    public void setReplaceOrgtype(String replaceOrgtype) {
        this.replaceOrgtype = replaceOrgtype;
    }

    public String getReplaceOrgtype() {
        return this.replaceOrgtype;
    }

    public void setReplaceUserid(String replaceUserid) {
        this.replaceUserid = replaceUserid;
    }

    public String getReplaceUserid() {
        return this.replaceUserid;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public Date getDispatchTime() {
        return this.dispatchTime;
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

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}