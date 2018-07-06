package com.ccbuluo.business.platform.label.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 标签和服务中心DTO
 * @author liuduo
 * @date 2018-07-03 11:42:57
 */
@ApiModel(value = "标签和服务中心关联关系实体", description = "标签和服务中心关联关系实体")
public class LabelServiceCenterDTO extends AftersaleCommonEntity {
    /**
     *标签id
     */
    @ApiModelProperty(name = "labelId", value = "标签id")
    private Long labelId;
    /**
     * 服务中心code
     */
    @ApiModelProperty(name = "serviceCenterCode", value = "服务中心code")
    private String serviceCenterCode;

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getServiceCenterCode() {
        return serviceCenterCode;
    }

    public void setServiceCenterCode(String serviceCenterCode) {
        this.serviceCenterCode = serviceCenterCode;
    }
}
