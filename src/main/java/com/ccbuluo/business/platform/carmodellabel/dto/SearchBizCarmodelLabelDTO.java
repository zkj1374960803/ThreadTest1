package com.ccbuluo.business.platform.carmodellabel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客服经理 上门维修 使用的维修车 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "客服经理 上门维修 使用的维修车 实体表实体", description = "客服经理 上门维修 使用的维修车 实体表")
public class SearchBizCarmodelLabelDTO {
    /**
     * 标签id
     */
    @ApiModelProperty(name = "id", value = "标签id")
    private Long id;
    /**
     * 标签编号
     */
    @ApiModelProperty(name = "labelCode", value = "标签编号")
    private String labelCode;
    /**
     * 标签名称
     */
    @ApiModelProperty(name = "labelName", value = "标签名称",required = true)
    private String labelName;
    /**
     * 排序号
     */
    @ApiModelProperty(name = "carStatus", value = "排序号")
    private Integer sort;

    /**
     * 参数量
     */
    @ApiModelProperty(name = "parameterTotal", value = "参数量")
    private Integer parameterTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getParameterTotal() {
        return parameterTotal;
    }

    public void setParameterTotal(Integer parameterTotal) {
        this.parameterTotal = parameterTotal;
    }
}