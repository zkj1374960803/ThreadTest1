package com.ccbuluo.business.platform.carconfiguration.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型具体配置表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "车型具体配置表实体CarmodelConfiguration", description = "车型具体配置表")
public class CarmodelConfiguration extends CarCommonEntity {

    /**
     * 所属车型，引用自车型管理表(basic_carmodel_manage)的id字段
     */
    @ApiModelProperty(name = "carmodelId", value = "所属车型，引用自车型管理表(basic_carmodel_manage)的id字段")
    private Long carmodelId;
    /**
     * 模型参数id，引用自车型参数配置表(basic_carmodel_parameter)的id 字段
     */
    @ApiModelProperty(name = "carmodelParameterId", value = "模型参数id，引用自车型参数配置表(basic_carmodel_parameter)的id 字段")
    private Long carmodelParameterId;
    /**
     * 参数名称
     */
    @ApiModelProperty(name = "parameterName", value = "参数名称")
    private String parameterName;
    /**
     * 所选参数值
     */
    @ApiModelProperty(name = "parameterValue", value = "所选参数值")
    private String parameterValue;

    public Long getCarmodelId() {
        return carmodelId;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public Long getCarmodelParameterId() {
        return carmodelParameterId;
    }

    public void setCarmodelParameterId(Long carmodelParameterId) {
        this.carmodelParameterId = carmodelParameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
