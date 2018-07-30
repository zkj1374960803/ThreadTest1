package com.ccbuluo.business.platform.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户消息中间DTO
 * @author zhangkangjian
 * @date 2018-07-28 15:01:42
 */
@ApiModel(value = "MessageUserDTO", description = "用户消息中间DTO")
public class MessageUserDTO {
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    @ApiModelProperty(name = "messageId", value = "消息id")
    private Long messageId;
    @ApiModelProperty(name = "receiveUuid", value = "接收人id")
    private String receiveUuid;
    @ApiModelProperty(name = "receiveName", value = "接收人姓名")
    private String receiveName;
    @ApiModelProperty(name = "status", value = "消息状态（0已读，1未读）")
    private Integer status = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getReceiveUuid() {
        return receiveUuid;
    }

    public void setReceiveUuid(String receiveUuid) {
        this.receiveUuid = receiveUuid;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
