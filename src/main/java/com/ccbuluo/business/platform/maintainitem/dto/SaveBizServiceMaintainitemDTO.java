package com.ccbuluo.business.platform.maintainitem.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 维修服务项实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "维修服务项实体保存dto", description = "维修服务项实体保存dto")
public class SaveBizServiceMaintainitemDTO extends IdEntity {
    /**
     * 服务项目名称
     */
    @ApiModelProperty(name = "maintainitemName", value = "服务项目名称")
    private String maintainitemName;
    /**
     * 单价
     */
    @ApiModelProperty(name = "unitPrice", value = "单价")
    private BigDecimal unitPrice;

    public String getMaintainitemName() {
        return maintainitemName;
    }

    public void setMaintainitemName(String maintainitemName) {
        this.maintainitemName = maintainitemName;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }
}