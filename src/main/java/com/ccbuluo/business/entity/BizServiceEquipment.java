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
     * 计量单位
     */
    @ApiModelProperty(name = "equipUnit", value = "计量单位")
    private String equipUnit;
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "equipName", value = "物料名称")
    private String equipName;
    /**
     * 物料类型的id
     */
    @ApiModelProperty(name = "equiptypeId", value = "物料类型的id")
    private Long equiptypeId;
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

    public Long getEquiptypeId() {
        return equiptypeId;
    }

    public void setEquiptypeId(Long equiptypeId) {
        this.equiptypeId = equiptypeId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getEquipUnit() {
        return equipUnit;
    }

    public void setEquipUnit(String equipUnit) {
        this.equipUnit = equipUnit;
    }
}