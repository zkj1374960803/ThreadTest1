package com.ccbuluo.business.platform.instock.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保存入库单
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "保存入库单", description = "保存入库单")
public class SaveInstockorderDetailDTO{
    /**
     * 申请单号
     */
    @ApiModelProperty(name = "applyNo", value = "申请单号")
    private String applyNo;
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "入库仓库编号")
    private String inRepositoryNo;
    /**
     * 入库单详单
     */
    @ApiModelProperty(name = "bizInstockorderDetailList", value = "入库单详单")
    private List<BizInstockorderDetail> bizInstockorderDetailList;

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public List<BizInstockorderDetail> getBizInstockorderDetailList() {
        return bizInstockorderDetailList;
    }

    public void setBizInstockorderDetailList(List<BizInstockorderDetail> bizInstockorderDetailList) {
        this.bizInstockorderDetailList = bizInstockorderDetailList;
    }
}