package com.ccbuluo.business.platform.maintainitem.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 维修服务项实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "维修服务项实体详情dto", description = "维修服务项实体详情dto")
public class DetailBizServiceMaintainitemDTO extends IdEntity {
    /**
     * 服务项目的编码
     */
    @ApiModelProperty(name = "maintainitemCode", value = "服务项目的编码")
    private String maintainitemCode;
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
    /**
     * 地区倍数数量
     */
    @ApiModelProperty(name = "multipleNum", value = "地区倍数数量")
    private Integer multipleNum;


    public void setMaintainitemCode(String maintainitemCode) {
        this.maintainitemCode = maintainitemCode;
    }

    public String getMaintainitemCode() {
        return this.maintainitemCode;
    }

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

    public Integer getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(Integer multipleNum) {
        this.multipleNum = multipleNum;
    }
}