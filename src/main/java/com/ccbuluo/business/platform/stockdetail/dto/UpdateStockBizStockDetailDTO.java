package com.ccbuluo.business.platform.stockdetail.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体", description = "批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表")
public class UpdateStockBizStockDetailDTO{
    /**
     * 占用库存
     */
    @ApiModelProperty(name = "occupyStock", value = "占用库存")
    private Long occupyStock;
    /**
     * 问题库存
     */
    @ApiModelProperty(name = "problemStock", value = "问题库存")
    private Long problemStock;
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 版本号（乐观锁）
     */
    @ApiModelProperty(name = "versionNo", value = "版本号（乐观锁）")
    private Long versionNo;

    public Long getOccupyStock() {
        return occupyStock;
    }

    public void setOccupyStock(Long occupyStock) {
        this.occupyStock = occupyStock;
    }

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

    public Long getProblemStock() {
        return problemStock;
    }

    public void setProblemStock(Long problemStock) {
        this.problemStock = problemStock;
    }
}