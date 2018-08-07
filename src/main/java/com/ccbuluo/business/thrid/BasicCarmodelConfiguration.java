package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型具体配置表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "车型具体配置表实体", description = "车型具体配置表")
public class BasicCarmodelConfiguration {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public Long getCarmodelId() {
        return this.carmodelId;
    }

    public void setCarmodelParameterId(Long carmodelParameterId) {
        this.carmodelParameterId = carmodelParameterId;
    }

    public Long getCarmodelParameterId() {
        return this.carmodelParameterId;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getParameterValue() {
        return this.parameterValue;
    }


}