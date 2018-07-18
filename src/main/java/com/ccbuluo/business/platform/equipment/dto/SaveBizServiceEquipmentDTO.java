package com.ccbuluo.business.platform.equipment.dto;

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
@ApiModel(value = "物料保存实体", description = "物料保存实体")
public class SaveBizServiceEquipmentDTO extends IdEntity {
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "equipName", value = "物料名称", required = true)
    private String equipName;
    /**
     * 物料类型的id
     */
    @ApiModelProperty(name = "equiptypeId", value = "物料类型的id", required = true)
    private Long equiptypeId;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

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

}