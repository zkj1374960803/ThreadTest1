package com.ccbuluo.business.platform.equipment.dto;

import com.ccbuluo.business.constants.EquipUnitEnum;
import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料详情实体", description = "物料详情实体")
public class DetailBizServiceEquipmentDTO extends IdEntity {
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
     * 计量单位
     */
    @ApiModelProperty(name = "equipUnit", value = "计量单位")
    private String equipUnit;
    /**
     * 物料类型的id
     */
    @ApiModelProperty(name = "equiptypeId", value = "物料类型的id")
    private Long equiptypeId;
    /**
     * 物料类型的名字
     */
    @ApiModelProperty(name = "equiptypeName", value = "物料类型的名字")
    private String equiptypeName;
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

    public String getEquiptypeName() {
        return equiptypeName;
    }

    public void setEquiptypeName(String equiptypeName) {
        this.equiptypeName = equiptypeName;
    }

    public String getEquipUnit() {
        return equipUnit;
    }

    public void setEquipUnit(String equipUnit) {
        this.equipUnit = equipUnit;
    }

    public String getUnitName() {
        return EquipUnitEnum.valueOf(equipUnit).getLabel();
    }
}