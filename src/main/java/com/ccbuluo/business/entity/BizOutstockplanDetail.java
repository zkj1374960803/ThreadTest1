package com.ccbuluo.business.entity;

import com.ccbuluo.business.constants.StockPlanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class BizOutstockplanDetail {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 出库类型
     */
    @ApiModelProperty(name = "outstockType", value = "出库类型")
    private String outstockType;
    /**
     * 使用的批次库存的id
     */
    @ApiModelProperty(name = "stockId", value = "使用的批次库存的id")
    private Long stockId;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品类型
     */
    @ApiModelProperty(name = "productType", value = "商品类型")
    private String productType;
    /**
     * 交易批次号,可以是调拨申请的单号或维修销售的服务单号
     */
    @ApiModelProperty(name = "tradeNo", value = "交易批次号,可以是调拨申请的单号或维修销售的服务单号")
    private String tradeNo;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商编号")
    private String supplierNo;
    /**
     * 
     */
    @ApiModelProperty(name = "applyDetailId", value = "")
    private Long applyDetailId;
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice;
    /**
     * 销售单价
     */
    @ApiModelProperty(name = "salesPrice", value = "销售单价")
    private BigDecimal salesPrice;
    /**
     * 出库仓库编号
     */
    @ApiModelProperty(name = "outRepositoryNo", value = "出库仓库编号")
    private String outRepositoryNo;
    /**
     * 计划出库数量
     */
    @ApiModelProperty(name = "planOutstocknum", value = "计划出库数量")
    private Long planOutstocknum;
    /**
     * 实际出库数量
     */
    @ApiModelProperty(name = "actualOutstocknum", value = "实际出库数量")
    private Long actualOutstocknum;
    /**
     * 出库计划的状态
     */
    @ApiModelProperty(name = "planStatus", value = "出库计划的状态")
    private String planStatus;
    /**
     * 完成时间
     */
    @ApiModelProperty(name = "completeTime", value = "完成时间")
    private Date completeTime;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(name = "operator", value = "更新人")
    private String operator;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private Integer deleteFlag;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
    /**
     * 商品分类名称,多级逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,多级逗号隔开")
    private String productCategoryname;
    /**
     * 乐观锁使用的版本号
     */
    @ApiModelProperty(name = "versionNo", value = "乐观锁使用的版本号")
    private Integer versionNo;

    /**
     * 商品计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "商品计量单位")
    private String productUnit;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setOutstockType(String outstockType) {
        this.outstockType = outstockType;
    }

    public String getOutstockType() {
        return this.outstockType;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockId() {
        return this.stockId;
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

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierNo() {
        return this.supplierNo;
    }

    public void setApplyDetailId(Long applyDetailId) {
        this.applyDetailId = applyDetailId;
    }

    public Long getApplyDetailId() {
        return this.applyDetailId;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    public void setOutRepositoryNo(String outRepositoryNo) {
        this.outRepositoryNo = outRepositoryNo;
    }

    public String getOutRepositoryNo() {
        return this.outRepositoryNo;
    }

    public void setPlanOutstocknum(Long planOutstocknum) {
        this.planOutstocknum = planOutstocknum;
    }

    public Long getPlanOutstocknum() {
        return this.planOutstocknum;
    }

    public void setActualOutstocknum(Long actualOutstocknum) {
        this.actualOutstocknum = actualOutstocknum;
    }

    public Long getActualOutstocknum() {
        return this.actualOutstocknum;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanStatus() {
        return this.planStatus;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() {
        return this.completeTime;
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

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductCategoryname() {
        return this.productCategoryname;
    }

    public String getProductUnit() {
        return productUnit;
    }
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
    public String getStatusName() {
        return StockPlanEnum.valueOf(planStatus).getLabel();
    }
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
}