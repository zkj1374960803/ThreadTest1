package com.ccbuluo.business.platform.carmodellabel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车型标签（获取全部车型标签用） 实体表实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "车型标签（获取全部车型标签用） 实体表实体", description = "车型标签（获取全部车型标签用） 实体表实体")
public class BizCarmodelLabelDTO {
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
}