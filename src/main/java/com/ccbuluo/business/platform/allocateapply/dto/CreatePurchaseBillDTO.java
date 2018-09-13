package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 创建采购单DTO
 * @author zhangkangjian
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "CreatePurchaseBillDTO", description = "创建采购单DTO")
public class CreatePurchaseBillDTO {
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "inRepositoryNo", value = "入库仓库编号")
    private String inRepositoryNo;
    /**
     * 申请商品详情列表
     */
    @ApiModelProperty(name = "allocateapplyDetailList", value = "申请商品详情列表")
    private List<AllocateapplyDetailDTO> allocateapplyDetailList;

    public String getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public List<AllocateapplyDetailDTO> getAllocateapplyDetailList() {
        return allocateapplyDetailList;
    }

    public void setAllocateapplyDetailList(List<AllocateapplyDetailDTO> allocateapplyDetailList) {
        this.allocateapplyDetailList = allocateapplyDetailList;
    }
}