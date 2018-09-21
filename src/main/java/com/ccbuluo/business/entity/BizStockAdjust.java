package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 盘库实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "盘库实体", description = "盘库实体")
public class BizStockAdjust extends AftersaleCommonEntity{
    /**
     * 盘库单号
     */
    @ApiModelProperty(name = "adjustDocno", value = "盘库单号")
    private String adjustDocno;
    /**
     * 执行盘库操作的机构编号
     */
    @ApiModelProperty(name = "adjustOrgno", value = "执行盘库操作的机构编号")
    private String adjustOrgno;
    /**
     * 盘库操作执行人
     */
    @ApiModelProperty(name = "adjustUserid", value = "盘库操作执行人")
    private String adjustUserid;
    /**
     * 执行盘库操作的时间
     */
    @ApiModelProperty(name = "adjustTime", value = "执行盘库操作的时间")
    private Date adjustTime;
    /**
     * 调整库存的原因
     */
    @ApiModelProperty(name = "adjustReson", value = "调整库存的原因")
    private String adjustReson;
    /**
     * 库存被调整的商品数量
     */
    @ApiModelProperty(name = "adjustResult", value = "库存被调整的商品数量")
    private Long adjustResult;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setAdjustDocno(String adjustDocno) {
        this.adjustDocno = adjustDocno;
    }

    public String getAdjustDocno() {
        return this.adjustDocno;
    }

    public void setAdjustUserid(String adjustUserid) {
        this.adjustUserid = adjustUserid;
    }

    public String getAdjustUserid() {
        return this.adjustUserid;
    }

    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }

    public Date getAdjustTime() {
        return this.adjustTime;
    }

    public void setAdjustReson(String adjustReson) {
        this.adjustReson = adjustReson;
    }

    public String getAdjustReson() {
        return this.adjustReson;
    }

    public void setAdjustResult(Long adjustResult) {
        this.adjustResult = adjustResult;
    }

    public Long getAdjustResult() {
        return this.adjustResult;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getAdjustOrgno() {
        return adjustOrgno;
    }

    public void setAdjustOrgno(String adjustOrgno) {
        this.adjustOrgno = adjustOrgno;
    }
}