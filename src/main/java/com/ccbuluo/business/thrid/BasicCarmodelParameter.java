package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 车型参数配置表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "车型参数配置表实体", description = "车型参数配置表")
public class BasicCarmodelParameter {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 参数名称
     */
    @ApiModelProperty(name = "parameterName", value = "参数名称")
    private String parameterName;
    /**
     * 参数值类型，枚举类型，目前支持 文本(TEXT)、单选属性(SINGLE_SELECTION)、多选属性(MULTI_SELECTION)
     */
    @ApiModelProperty(name = "valueType", value = "参数值类型，枚举类型，目前支持 文本(TEXT)、单选属性(SINGLE_SELECTION)、多选属性(MULTI_SELECTION)")
    private String valueType;
    /**
     * 可选值列表，json
     */
    @ApiModelProperty(name = "optionalList", value = "可选值列表，json")
    private String optionalList;
    /**
     * 是否手动新增,1为可以手动新增，0为不可以，默认为0
     */
    @ApiModelProperty(name = "manualAddFlag", value = "是否手动新增,1为可以手动新增，0为不可以，默认为0")
    private Long manualAddFlag;
    /**
     * 是否支持必填,1为必填，0为非必填，默认值为非必填
     */
    @ApiModelProperty(name = "requiredFlag", value = "是否支持必填,1为必填，0为非必填，默认值为非必填")
    private Long requiredFlag;
    /**
     * 排序编号
     */
    @ApiModelProperty(name = "sortNumber", value = "排序编号")
    private Long sortNumber;
    /**
     * 参数标签id
     */
    @ApiModelProperty(name = "carmodelLabelId", value = "参数标签id")
    private Long carmodelLabelId;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 创建用户,引用用户表的用户名
     */
    @ApiModelProperty(name = "creator", value = "创建用户,引用用户表的用户名")
    private String creator;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "operateTime", value = "修改时间")
    private Date operateTime;
    /**
     * 修改用户，引用用户表的用户名
     */
    @ApiModelProperty(name = "operator", value = "修改用户，引用用户表的用户名")
    private String operator;
    /**
     * 删除标识，缺省值0,0为未删除，1为已删除
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识，缺省值0,0为未删除，1为已删除")
    private Long deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setOptionalList(String optionalList) {
        this.optionalList = optionalList;
    }

    public String getOptionalList() {
        return this.optionalList;
    }

    public void setManualAddFlag(Long manualAddFlag) {
        this.manualAddFlag = manualAddFlag;
    }

    public Long getManualAddFlag() {
        return this.manualAddFlag;
    }

    public void setRequiredFlag(Long requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public Long getRequiredFlag() {
        return this.requiredFlag;
    }

    public void setSortNumber(Long sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Long getSortNumber() {
        return this.sortNumber;
    }

    public void setCarmodelLabelId(Long carmodelLabelId) {
        this.carmodelLabelId = carmodelLabelId;
    }

    public Long getCarmodelLabelId() {
        return this.carmodelLabelId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }


}