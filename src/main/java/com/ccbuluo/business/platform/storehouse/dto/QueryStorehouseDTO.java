package com.ccbuluo.business.platform.storehouse.dto;

import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库DTO
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "QueryStorehouseDTO", description = "仓库DTO（查询仓库下拉框）")
public class QueryStorehouseDTO {
    /**
     * 仓库编号
     */
    @ApiModelProperty(name = "storehouseCode", value = "仓库编号")
    private String storehouseCode;
    /**
     * 名称
     */
    @ApiModelProperty(name = "storehouseName", value = "名称")
    private String storehouseName;


    public void setStorehouseCode(String storehouseCode) {
        this.storehouseCode = storehouseCode;
    }

    public String getStorehouseCode() {
        return this.storehouseCode;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public String getStorehouseName() {
        return this.storehouseName;
    }

}