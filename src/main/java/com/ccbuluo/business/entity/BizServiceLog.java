package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@ApiModel(value = "实体", description = "")
public class BizServiceLog {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 功能所属的模块编号：服务(SERVICE)、进销存(ERP)、车辆管理(CAR)、基础信息(BASIC)
     */
    @ApiModelProperty(name = "model", value = "功能所属的模块编号：服务(SERVICE)、进销存(ERP)、车辆管理(CAR)、基础信息(BASIC)")
    private String model;
    /**
     * 功能的编号
     */
    @ApiModelProperty(name = "action", value = "功能的编号")
    private String action;
    /**
     * 日志的内容
     */
    @ApiModelProperty(name = "logContent", value = "日志的内容")
    private String logContent;
    /**
     * 触发功能的人所属机构的编号
     */
    @ApiModelProperty(name = "ownerOrgno", value = "触发功能的人所属机构的编号")
    private String ownerOrgno;
    /**
     * 触发功能的人所属机构的名称
     */
    @ApiModelProperty(name = "ownerOrgname", value = "触发功能的人所属机构的名称")
    private String ownerOrgname;
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

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return this.model;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getLogContent() {
        return this.logContent;
    }

    public void setOwnerOrgno(String ownerOrgno) {
        this.ownerOrgno = ownerOrgno;
    }

    public String getOwnerOrgno() {
        return this.ownerOrgno;
    }

    public void setOwnerOrgname(String ownerOrgname) {
        this.ownerOrgname = ownerOrgname;
    }

    public String getOwnerOrgname() {
        return this.ownerOrgname;
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