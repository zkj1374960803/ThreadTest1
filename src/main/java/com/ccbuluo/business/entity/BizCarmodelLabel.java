package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型标签 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "BizCarmodelLabel", description = "车型标签 实体表实体")
public class BizCarmodelLabel extends AftersaleCommonEntity{
    /**
     * 标签编号
     */
    @ApiModelProperty(name = "labelCode", value = "标签编号")
    private String labelCode;
    /**
     * 标签名称
     */
    @ValidateLength(min = 0, max = 10,message = "标签名称长度不合法")
    @ApiModelProperty(name = "labelName", value = "标签名称",required = true)
    private String labelName;
    /**
     * 排序号
     */
    @ValidateNotNull(message = "排序号不能为空")
    @ValidateMax(value = 100,message = "排序号不能大于100")
    @ApiModelProperty(name = "sort", value = "排序号")
    private Integer sort;

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}