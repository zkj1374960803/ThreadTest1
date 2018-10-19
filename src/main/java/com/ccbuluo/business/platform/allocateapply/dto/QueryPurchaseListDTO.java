package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 查询
 * @author zhangkangjian
 * @date 2018-09-13 10:02:09
 */
public class QueryPurchaseListDTO {


    /**
     * 采购单号
     */
    @ApiModelProperty(name = "applyNo", value = "采购单号")
    private String applyNo;

    /**
     * 申请时间
     */
    @ApiModelProperty(name = "applyTime", value = "申请时间")
    private Long applyTime;

    /**
     * 采购总额
     */
    @ApiModelProperty(name = "totalPurchase", value = "采购总额")
    private BigDecimal totalPurchase;

    /**
     * 采购状态
     */
    @ApiModelProperty(name = "applyStatus", value = "采购状态")
    private String applyStatus;
    /**
     *
     */
    @ApiModelProperty(name = "applyType", value = "采购类型")
    private String applyType;

    /**
     *商品的类型
     */
    @ApiModelProperty(name = "productType", value = "商品的类型")
    private String productType;

    /**
     * 偏移量
     */
    @ApiModelProperty(name = "offset", value = "偏移量")
    private Integer offset = 0;
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private int deleteFlag;

    /**
     * 每页显示的数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示的数量")
    private Integer pageSize = 10;

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
