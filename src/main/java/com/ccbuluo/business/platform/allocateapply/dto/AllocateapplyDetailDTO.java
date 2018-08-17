package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 申请详情
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "BizAllocateapplyDetail", description = "申请详情")
public class AllocateapplyDetailDTO {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "", hidden = true)
    private Long id;
    /**
     * 调拨申请单编号
     */
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号", hidden = true)
    private String applyNo;
    /**
     * 商品的编号
     */
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    /**
     * 商品的名称
     */
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    /**
     * 商品类型（注：FITTINGS零配件，EQUIPMENT物料）
     */
    @ApiModelProperty(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）")
    private String productType;
    /**
     * 做冗余，分类整个路径的名字，多级用逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "做冗余，分类整个路径的名字，多级用逗号隔开")
    private String productCategoryname;
    /**
     * 申请的商品数量
     */
    @ApiModelProperty(name = "applyNum", value = "申请的商品数量")
    private Long applyNum;
    /**
     * 计量单位，用作展示冗余
     */
    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;
    /**
     * 销售单价
     */
    @ApiModelProperty(name = "sellPrice", value = "销售单价")
    private BigDecimal sellPrice;
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice;
    /**
     * 当集团采购时，可以设置供应商，将调拨申请单作为采购单
     */
    @ApiModelProperty(name = "supplierNo", value = "当集团采购时，可以设置供应商，将调拨申请单作为采购单")
    private String supplierNo;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人", hidden = true)
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人", hidden = true)
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间", hidden = true)
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识", hidden = true)
    private Long deleteFlag = 0L;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注", hidden = true)
    private String remark;

    /**
     * 库存类型
     */
    @ApiModelProperty(name = "stockType", value = "库存类型", hidden = true)
    private String stockType;

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyNo() {
        return this.applyNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return this.productNo;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductCategoryname() {
        return this.productCategoryname;
    }

    public void setApplyNum(Long applyNum) {
        this.applyNum = applyNum;
    }

    public Long getApplyNum() {
        return this.applyNum;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {

        return this.unit;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellPrice() {
        return this.sellPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierNo() {
        return this.supplierNo;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}