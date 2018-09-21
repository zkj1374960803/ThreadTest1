package com.ccbuluo.business.platform.adjust.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.BizStockAdjustdetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 盘库实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "盘库实体(保存用)", description = "盘库实体(保存用)")
public class SaveBizStockAdjustDTO extends AftersaleCommonEntity{
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
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
    /**
     * 盘库详单
     */
    @ApiModelProperty(name = "bizStockAdjustdetailList", value = "盘库详单")
    private List<BizStockAdjustdetail> bizStockAdjustdetailList;


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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public List<BizStockAdjustdetail> getBizStockAdjustdetailList() {
        return bizStockAdjustdetailList;
    }

    public void setBizStockAdjustdetailList(List<BizStockAdjustdetail> bizStockAdjustdetailList) {
        this.bizStockAdjustdetailList = bizStockAdjustdetailList;
    }
}