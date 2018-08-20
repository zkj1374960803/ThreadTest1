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
    @ApiModelProperty(name = "applyType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)")
    private String applyType;

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

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
}