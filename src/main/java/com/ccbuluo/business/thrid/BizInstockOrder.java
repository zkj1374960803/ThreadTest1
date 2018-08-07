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
public class BizInstockOrder {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 入库单编号
     */
    @ApiModelProperty(name = "instockOrderno", value = "入库单编号")
    private String instockOrderno;
    /**
     * 可以使申请调拨单或其他类型可以出发入库的单据
     */
    @ApiModelProperty(name = "tradeDocno", value = "可以使申请调拨单或其他类型可以出发入库的单据")
    private String tradeDocno;
    /**
     * 要入货的仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "要入货的仓库编号")
    private String inRepositoryNo;
    /**
     * 入库人的uuid
     */
    @ApiModelProperty(name = "instockOperator", value = "入库人的uuid")
    private String instockOperator;
    /**
     * 入库类型
     */
    @ApiModelProperty(name = "instockType", value = "入库类型")
    private String instockType;
    /**
     * 入库时间
     */
    @ApiModelProperty(name = "instockTime", value = "入库时间")
    private Date instockTime;
    /**
     * 物流单号
     */
    @ApiModelProperty(name = "transportorderNo", value = "物流单号")
    private String transportorderNo;
    /**
     * 复核完成
     */
    @ApiModelProperty(name = "checked", value = "复核完成")
    private String checked;
    /**
     * 复核完成时间
     */
    @ApiModelProperty(name = "ceeckedTime", value = "复核完成时间")
    private Date ceeckedTime;
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

    public void setInstockOrderno(String instockOrderno) {
        this.instockOrderno = instockOrderno;
    }

    public String getInstockOrderno() {
        return this.instockOrderno;
    }

    public void setTradeDocno(String tradeDocno) {
        this.tradeDocno = tradeDocno;
    }

    public String getTradeDocno() {
        return this.tradeDocno;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public String getInRepositoryNo() {
        return this.inRepositoryNo;
    }

    public void setInstockOperator(String instockOperator) {
        this.instockOperator = instockOperator;
    }

    public String getInstockOperator() {
        return this.instockOperator;
    }

    public void setInstockType(String instockType) {
        this.instockType = instockType;
    }

    public String getInstockType() {
        return this.instockType;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public Date getInstockTime() {
        return this.instockTime;
    }

    public void setTransportorderNo(String transportorderNo) {
        this.transportorderNo = transportorderNo;
    }

    public String getTransportorderNo() {
        return this.transportorderNo;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getChecked() {
        return this.checked;
    }

    public void setCeeckedTime(Date ceeckedTime) {
        this.ceeckedTime = ceeckedTime;
    }

    public Date getCeeckedTime() {
        return this.ceeckedTime;
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