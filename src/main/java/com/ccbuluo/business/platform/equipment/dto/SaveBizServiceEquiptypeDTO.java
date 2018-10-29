package com.ccbuluo.business.platform.equipment.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料的类型实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料类型保存实体", description = "物料类型保存实体")
public class SaveBizServiceEquiptypeDTO extends IdEntity {
    /**
     * 类型的名称
     */
    @ValidateLength(min = 0, max = 6, message = "名称不能超过6个字")
    @ValidateNotBlank(message = "名称不能为空")
    @ApiModelProperty(name = "typeName", value = "类型的名称", required = true)
    private String typeName;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

}