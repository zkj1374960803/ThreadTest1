package com.ccbuluo.business.platform.outstock.dto;

import com.ccbuluo.business.constants.StockPlanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改出库计划的状态
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "修改出库计划的状态", description = "")
public class updatePlanStatusDTO {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 乐观锁使用的版本号
     */
    @ApiModelProperty(name = "versionNo", value = "乐观锁使用的版本号")
    private Long versionNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }
}