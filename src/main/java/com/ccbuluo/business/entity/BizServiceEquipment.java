package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 物料实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料实体", description = "物料")
public class BizServiceEquipment extends AftersaleCommonEntity{
    /**
     * 物料编号
     */
    @ApiModelProperty(name = "equipCode", value = "物料编号")
    private String equipCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "equipName", value = "物料名称")
    private String equipName;
    /**
     * 物料类型的编号
     */
    @ApiModelProperty(name = "equipType", value = "物料类型的编号")
    private String equipType;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public String getEquipCode() {
        return this.equipCode;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getEquipName() {
        return this.equipName;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipType() {
        return this.equipType;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}