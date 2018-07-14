package com.ccbuluo.business.platform.carconfiguration.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型参数配置表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "车型参数配置表实体CarmodelParameter", description = "车型参数配置表")
public class CarmodelParameter extends CarCommonEntity {


    /**
     * 参数名称
     */
    @ApiModelProperty(name = "parameterName", value = "参数名称")
    private String parameterName;
    /**
     * 参数值类型，目前支持 文本、单选属性、多选属性
     */
    @ApiModelProperty(name = "valueType", value = "参数值类型，目前支持 文本、单选属性、多选属性")
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
    private Integer manualAddFlag;
    /**
     * 是否支持必填,1为必填，0为非必填，默认值为非必填
     */
    @ApiModelProperty(name = "requiredFlag", value = "是否支持必填,1为必填，0为非必填，默认值为非必填")
    private Integer requiredFlag;
    /**
     * 排序编号
     */
    @ApiModelProperty(name = "sortNumber", value = "排序编号")
    private Integer sortNumber;


    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getOptionalList() {
        return optionalList;
    }

    public void setOptionalList(String optionalList) {
        this.optionalList = optionalList;
    }

    public Integer getManualAddFlag() {
        return manualAddFlag;
    }

    public void setManualAddFlag(Integer manualAddFlag) {
        this.manualAddFlag = manualAddFlag;
    }

    public Integer getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(Integer requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

}
