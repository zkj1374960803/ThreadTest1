package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 申请单详情（用于处理申请的时候，构建订单）
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "AllocateapplyDetailBO", description = "申请详情")
public class AllocateapplyDetailBO {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 调拨申请单编号
     */
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号")
    private String applyNo;
    /**
     * 商品的编号
     */
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    /**
     * 是物料、零配件
     */
    @ApiModelProperty(name = "productType", value = "是物料、零配件")
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
     * 买方机构的编号
     */
    @ApiModelProperty(name = "instockOrgno", value = "买方机构的编号", hidden = true)
    private String instockOrgno;
    /**
     * 入库仓库编号(买方仓库)
     */
    @ApiModelProperty(name = "instockOrgno", value = "入库仓库编号", hidden = true)
    private String inRepositoryNo;
    /**
     * 卖方机构的编号
     */
    @ApiModelProperty(name = "outstockOrgno", value = "卖方机构的编号", hidden = true)
    private String outstockOrgno;

    /**
     * 交易类型
     */
    @ApiModelProperty(name = "processType", value = "交易类型（采购或是调拨）", hidden = true)
    private String processType;
    /**
     * 申请处理机构code
     */
    @ApiModelProperty(name = "processOrgno", value = "申请处理机构code", hidden = true)
    private String processOrgno;
    /**
     * 商品名字
     */
    @ApiModelProperty(name = "productName", value = "商品名字")
    private String productName;

    /**
     * 处理机构类型
     */
    @ApiModelProperty(name = "processOrgtype", value = "处理机构类型")
    private String processOrgtype;

    /**
     * 申请类型
     */
    @ApiModelProperty(name = "applyType", value = "申请类型（SERVICEPLATFORM，SAMELEVEL）")
    private String applyType;
    /**
     *  库存类型
     */
    @ApiModelProperty(name = "stockType", value = "库存类型（正常库存、问题库存、报损件库存，等等）")
    private String stockType;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 申请机构
     */
    @ApiModelProperty(name = "applyorgNo", value = "申请机构")
    private String applyorgNo;

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

    public String getInstockOrgno() {
        return instockOrgno;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public String getInRepositoryNo() {
        return inRepositoryNo;
    }

    public void setInRepositoryNo(String inRepositoryNo) {
        this.inRepositoryNo = inRepositoryNo;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessOrgno() {
        return processOrgno;
    }

    public void setProcessOrgno(String processOrgno) {
        this.processOrgno = processOrgno;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProcessOrgtype() {
        return processOrgtype;
    }

    public void setProcessOrgtype(String processOrgtype) {
        this.processOrgtype = processOrgtype;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyorgNo() {
        return applyorgNo;
    }

    public void setApplyorgNo(String applyorgNo) {
        this.applyorgNo = applyorgNo;
    }
}