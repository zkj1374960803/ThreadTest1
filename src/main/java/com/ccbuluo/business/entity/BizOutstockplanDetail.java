package com.ccbuluo.business.entity;

import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库计划实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "BizOutstockplanDetail", description = "出库计划实体实体")
public class BizOutstockplanDetail extends AftersaleCommonEntity{
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
     * 商品名字
     */
    @ApiModelProperty(name = "productName", value = "商品名字")
    private String productName;
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
     * 供应商名字
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名字")
    private String supplierName;
    /**
     * 仓库名字
     */
    @ApiModelProperty(name = "storehouseName", value = "仓库名字")
    private String storehouseName;
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
     * 出库机构编号
     */
    @ApiModelProperty(name = "outOrgno", value = "出库机构编号")
    private String outOrgno;
    /**
     * 出库机构名称
     */
    @ApiModelProperty(name = "outOrgName", value = "出库机构名称")
    private String outOrgName;
    /**
     * 计划出库数量
     */
    @ApiModelProperty(name = "planOutstocknum", value = "计划出库数量")
    private Long planOutstocknum;
    /**
     * 应出数量
     */
    @ApiModelProperty(name = "shouldOutstocknum", value = "应出数量")
    private Long shouldOutstocknum;
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
    private Long versionNo;

    /**
     * 商品计量单位
     */
    @ApiModelProperty(name = "productUnit", value = "商品计量单位")
    private String productUnit;
    /**
     *  库存类型
     */
    @ApiModelProperty(name = "stockType", value = "库存类型（正常库存、问题库存、报损件库存，等等）")
    private String stockType;


    @JsonIgnore
    public String getProdNoAndSupplyNo(){
        return StringUtils.defaultIfBlank(productNo, "") + "@" + StringUtils.defaultIfBlank(supplierNo, "");
    }

    public String getOutOrgName() {
        return outOrgName;
    }

    public double getTotalPrice(){
        if(salesPrice != null){
            return salesPrice.doubleValue() * planOutstocknum;
        }else if (costPrice != null){
            return costPrice.doubleValue() * planOutstocknum;
        }
        return 0D;
    }

    public void setOutOrgName(String outOrgName) {
        this.outOrgName = outOrgName;
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

    public String getOutOrgno() {
        return outOrgno;
    }

    public void setOutOrgno(String outOrgno) {
        this.outOrgno = outOrgno;
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

    public String getStatusName() {
        if(planStatus != null){
            return StockPlanStatusEnum.valueOf(planStatus).getLabel();
        }
        return StringUtils.EMPTY;
    }
    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Long getShouldOutstocknum() {
        return shouldOutstocknum;
    }

    public void setShouldOutstocknum(Long shouldOutstocknum) {
        this.shouldOutstocknum = shouldOutstocknum;
    }
}