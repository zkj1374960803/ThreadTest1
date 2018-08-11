package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体", description = "批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表")
public class BizStockDetail extends AftersaleCommonEntity{
    /**
     * 仓库编号
     */
    @ApiModelProperty(name = "repositoryNo", value = "仓库编号")
    private String repositoryNo;
    /**
     * 所属机构的编号
     */
    @ApiModelProperty(name = "orgNo", value = "所属机构的编号")
    private String orgNo;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "")
    private String productNo;
    /**
     * 商品类型(物料、零配件)
     */
    @ApiModelProperty(name = "productType", value = "商品类型(物料、零配件)")
    private String productType;
    /**
     * 交易批次号,可以是调拨申请单号等
     */
    @ApiModelProperty(name = "tradeNo", value = "交易批次号,可以是调拨申请单号等")
    private String tradeNo;
    /**
     * 供应商
     */
    @ApiModelProperty(name = "supplierNo", value = "")
    private String supplierNo;
    /**
     * 有效库存
     */
    @ApiModelProperty(name = "validStock", value = "")
    private Long validStock;
    /**
     * 占用库存
     */
    @ApiModelProperty(name = "occupyStock", value = "")
    private Long occupyStock;
    /**
     * 有问题但是可以享受三包的商品库存数量
     */
    @ApiModelProperty(name = "problemStock", value = "有问题但是可以享受三包的商品库存数量")
    private Long problemStock;
    /**
     * 损坏件且不享受三包的商品库存数量
     */
    @ApiModelProperty(name = "damagedStock", value = "损坏件且不享受三包的商品库存数量")
    private Long damagedStock;
    /**
     * 在运输途中的商品库存数量
     */
    @ApiModelProperty(name = "transitStock", value = "在运输途中的商品库存数量")
    private Long transitStock;
    /**
     * 冻结的库存数量
     */
    @ApiModelProperty(name = "freezeStock", value = "冻结的库存数量")
    private Long freezeStock;
    /**
     * 商品从哪个机构买来的
     */
    @ApiModelProperty(name = "sellerOrgno", value = "商品从哪个机构买来的")
    private String sellerOrgno;
    /**
     * 成本价
     */
    @ApiModelProperty(name = "costPrice", value = "")
    private BigDecimal costPrice;
    /**
     * 入库计划明细id
     */
    @ApiModelProperty(name = "instockPlanid", value = "")
    private Long instockPlanid;
    /**
     * 最后一次矫正该库存(盘库)的时间
     */
    @ApiModelProperty(name = "latestCorrectTime", value = "最后一次矫正该库存(盘库)的时间")
    private Date latestCorrectTime;
    /**
     * 乐观锁使用的版本号
     */
    @ApiModelProperty(name = "versionNo", value = "乐观锁使用的版本号")
    private Long versionNo;

    public void setRepositoryNo(String repositoryNo) {
        this.repositoryNo = repositoryNo;
    }

    public String getRepositoryNo() {
        return this.repositoryNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getOrgNo() {
        return this.orgNo;
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

    public void setValidStock(Long validStock) {
        this.validStock = validStock;
    }

    public Long getValidStock() {
        return this.validStock;
    }

    public void setOccupyStock(Long occupyStock) {
        this.occupyStock = occupyStock;
    }

    public Long getOccupyStock() {
        return this.occupyStock;
    }

    public void setProblemStock(Long problemStock) {
        this.problemStock = problemStock;
    }

    public Long getProblemStock() {
        return this.problemStock;
    }

    public void setDamagedStock(Long damagedStock) {
        this.damagedStock = damagedStock;
    }

    public Long getDamagedStock() {
        return this.damagedStock;
    }

    public void setTransitStock(Long transitStock) {
        this.transitStock = transitStock;
    }

    public Long getTransitStock() {
        return this.transitStock;
    }

    public void setFreezeStock(Long freezeStock) {
        this.freezeStock = freezeStock;
    }

    public Long getFreezeStock() {
        return this.freezeStock;
    }

    public void setSellerOrgno(String sellerOrgno) {
        this.sellerOrgno = sellerOrgno;
    }

    public String getSellerOrgno() {
        return this.sellerOrgno;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public void setInstockPlanid(Long instockPlanid) {
        this.instockPlanid = instockPlanid;
    }

    public Long getInstockPlanid() {
        return this.instockPlanid;
    }

    public void setLatestCorrectTime(Date latestCorrectTime) {
        this.latestCorrectTime = latestCorrectTime;
    }

    public Date getLatestCorrectTime() {
        return this.latestCorrectTime;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }
}