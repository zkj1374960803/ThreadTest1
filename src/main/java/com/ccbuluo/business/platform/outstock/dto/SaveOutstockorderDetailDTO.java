package com.ccbuluo.business.platform.outstock.dto;

import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.entity.BizOutstockorderDetail;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 保存出库单
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "保存出库单", description = "保存出库单")
public class SaveOutstockorderDetailDTO {
    /**
     * 申请单号
     */
    @ApiModelProperty(name = "applyNo", value = "申请单号")
    private String applyNo;
    /**
     * 出库仓库编号
     */
    @ValidateNotBlank(message = "出库仓库编号不能为空")
    @ApiModelProperty(name = "outRepositoryNo", value = "出库仓库编号")
    private String outRepositoryNo;
    /**
     * 物流单号
     */
    @ApiModelProperty(name = "transportorderNo", value = "物流单号")
    private String transportorderNo;
    /**
     * 出库单详单
     */
    @ApiModelProperty(name = "bizInstockorderDetailList", value = "出库单详单")
    private List<BizOutstockorderDetail> bizOutstockorderDetailList;

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getOutRepositoryNo() {
        return outRepositoryNo;
    }

    public void setOutRepositoryNo(String outRepositoryNo) {
        this.outRepositoryNo = outRepositoryNo;
    }

    public String getTransportorderNo() {
        return transportorderNo;
    }

    public void setTransportorderNo(String transportorderNo) {
        this.transportorderNo = transportorderNo;
    }

    public List<BizOutstockorderDetail> getBizOutstockorderDetailList() {
        return bizOutstockorderDetailList;
    }

    public void setBizOutstockorderDetailList(List<BizOutstockorderDetail> bizOutstockorderDetailList) {
        this.bizOutstockorderDetailList = bizOutstockorderDetailList;
    }
}