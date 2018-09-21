package com.ccbuluo.business.platform.label.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 查询列表DTO
 * @author zhangkangjian
 * @date 2018-07-03 11:42:57
 */
public class ListLabelDTO {
    /**
     *标签id
     */
    @ApiModelProperty(name = "id", value = "标签id")
    private Long id;
    /**
     * 标签名称
     */
    @ApiModelProperty(name = "labelName", value = "标签名称")
    private String labelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
