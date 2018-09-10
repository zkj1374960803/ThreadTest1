package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 日志实体
 * @author zhangkangjian
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@ApiModel(value = "BizServiceLog", description = "日志实体")
public class BizServiceLog extends AftersaleCommonEntity{

    /**
     * 功能所属的模块编号：服务(SERVICE)、进销存(ERP)、车辆管理(CAR)、基础信息(BASIC)
     */
    @ApiModelProperty(name = "model", value = "功能所属的模块编号：服务(SERVICE)、进销存(ERP)、车辆管理(CAR)、基础信息(BASIC)")
    private String model;

    /**
     * 操作的主体的类型
     */
    @ApiModelProperty(name = "subjectType", value = "操作的主体的类型")
    private String subjectType;

    /**
     * 主角对象的主键id值
     */
    @ApiModelProperty(name = "subjectKeyvalue", value = "主角对象的主键id值")
    private String subjectKeyvalue;
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
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;


    public enum SubjectTypeEnum{
        ORDER("订单类型");

        SubjectTypeEnum(String label){
            this.label = label;
        }

        private String label;

        public String getLabel(){
            return label;
        }

    }

    public enum actionEnum{
        SAVE("增"),DELETE("删"),UPDATE("改"),SELECT("查");

        actionEnum(String label){
            this.label = label;
        }

        private String label;

        public String getLabel(){
            return label;
        }
    }

    public enum modelEnum{
        SERVICE("服务"),ERP("进销存"),CAR("车辆管理"),BASIC("基础信息");
        private String label;

        modelEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectKeyvalue() {
        return subjectKeyvalue;
    }

    public void setSubjectKeyvalue(String subjectKeyvalue) {
        this.subjectKeyvalue = subjectKeyvalue;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getOwnerOrgno() {
        return ownerOrgno;
    }

    public void setOwnerOrgno(String ownerOrgno) {
        this.ownerOrgno = ownerOrgno;
    }

    public String getOwnerOrgname() {
        return ownerOrgname;
    }

    public void setOwnerOrgname(String ownerOrgname) {
        this.ownerOrgname = ownerOrgname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}