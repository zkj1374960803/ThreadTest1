package com.ccbuluo.business.platform.carconfiguration.entity;


import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
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
    @ValidateLength(min = 0, max = 10,message = "参数名称长度不合法")
    @ApiModelProperty(name = "parameterName", value = "参数名称", required = true)
    private String parameterName;
    /**
     * 参数值类型，目前支持 文本、单选属性、多选属性
     */
    @ValidateNotNull(message = "参数类型不能为空")
    @ApiModelProperty(name = "valueType", value = "参数值类型，目前支持 文本(INPUT)、单选属性(SINGLE)、多选属性(MULTI)", required = true)
    private String valueType;
    /**
     * 可选值列表，json
     */
    @ValidateNotNull(message = "可选值不能为空")
    @ApiModelProperty(name = "optionalList", value = "可选值列表，json", required = true)
    private String optionalList;
    /**
     * 是否手动新增,1为可以手动新增，0为不可以，默认为0
     */
    @ApiModelProperty(name = "manualAddFlag", value = "是否手动新增,1为可以手动新增，0为不可以，默认为0", required = true)
    private Integer manualAddFlag;
    /**
     * 是否支持必填,1为必填，0为非必填，默认值为非必填
     */
    @ValidateNotNull(message = "是否支持必填不能为空")
    @ApiModelProperty(name = "requiredFlag", value = "是否支持必填,1为必填，0为非必填，默认值为非必填", required = true)
    private Integer requiredFlag;
    /**
     * 排序编号
     */
    @ValidateNotNull(message = "优先级不能为空")
    @ValidateMax(value = 100,message = "优先级不能大于100")
    @ApiModelProperty(name = "sortNumber", value = "排序编号")
    private Integer sortNumber;

    /**
     * 标签id
     */
    @ValidateNotNull(message = "标签id不能为空")
    @ApiModelProperty(name = "carmodelLabelId", value = "标签id")
    private Integer carmodelLabelId;
    /**
     * 标签name
     */
    @ApiModelProperty(name = "labelName", value = "标签name")
    private String labelName;


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

    public Integer getCarmodelLabelId() {
        return carmodelLabelId;
    }

    public void setCarmodelLabelId(Integer carmodelLabelId) {
        this.carmodelLabelId = carmodelLabelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
