package com.ccbuluo.business.platform.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 创建消息dto
 * @author zhangkangjian
 * @date 2018-07-28 14:17:44
 */
@ApiModel(value = "CreateMessageDTO", description = "发送消息dto")
public class CreateMessageDTO {
    /**
     * 接收人uuid
     */
    @ApiModelProperty(name = "receiveUuid", value = "接收人uuid")
    private List<String> receiveUuid;
    /**
     * 接收人姓名
     */
    @ApiModelProperty(name = "receiveName", value = "接收人姓名")
    private List<String> receiveName;
    /**
     * 消息的内容
     */
    @ApiModelProperty(name = "messageContent", value = "消息的内容")
    private String messageContent;
    /**
     * 消息的类型
     */
    @ApiModelProperty(name = "messageType", value = "消息的类型")
    private String messageType;
    /**
     * 发送人uuid
     */
    @ApiModelProperty(name = "sendUuid", value = "发送人uuid")
    private String sendUuid;
    /**
     * 发送人姓名
     */
    @ApiModelProperty(name = "sendName", value = "发送人姓名")
    private String sendName;
    /**
     * 详细的状态（0未发布，1发布,2撤回）
     */
    @ApiModelProperty(name = "status", value = "详细的状态（0未发布，1发布,2撤回）")
    private String status;
    @ApiModelProperty(name = "createTime", value = "创建时间")
    protected Date createTime;
    @ApiModelProperty(name = "creator", value = "创建人")
    protected String creator;
    @ApiModelProperty(name = "operateTime", value = "操作时间")
    protected Date operateTime;
    @ApiModelProperty(name = "operator", value = "操作人")
    protected String operator;
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    protected Integer deleteFlag = 0;
    @ApiModelProperty(name = "offset", value = "偏移量")
    protected Integer offset = 0;
    @ApiModelProperty(name = "pageSize", value = "每页显示的条数")
    protected Integer pageSize = 10;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getReceiveUuid() {
        return receiveUuid;
    }

    public void setReceiveUuid(List<String> receiveUuid) {
        this.receiveUuid = receiveUuid;
    }

    public List<String> getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(List<String> receiveName) {
        this.receiveName = receiveName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSendUuid() {
        return sendUuid;
    }

    public void setSendUuid(String sendUuid) {
        this.sendUuid = sendUuid;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
