package com.ccbuluo.business.platform.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询消息列表dto
 * @author zhangkangjian
 * @date 2018-07-28 14:36:51
 */
@ApiModel(value = "FindMessagerListDTO", description = "查询消息列表dto")
public class FindMessagerListDTO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 消息的内容
     */
    @ApiModelProperty(name = "messageContent", value = "消息的内容")
    private String messageContent;
    /**
     * 发送人的uuid
     */
    @ApiModelProperty(name = "sendUuid", value = "发送人的uuid")
    private String sendUuid;
    /**
     * 发送人姓名
     */
    @ApiModelProperty(name = "sendName", value = "发送人姓名")
    private String sendName;
    /**
     * 消息的状态
     */
    @ApiModelProperty(name = "status", value = "消息的状态")
    private Integer status;
    /**
     * 消息的类型
     */
    @ApiModelProperty(name = "messageType", value = "消息的类型")
    private String messageType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
