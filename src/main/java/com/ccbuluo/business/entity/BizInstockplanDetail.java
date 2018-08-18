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
public class BizInstockplanDetail {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 入库类型(采购入库，调拨入库)
     */
    @ApiModelProperty(name = "instockType", value = "入库类型(采购入库，调拨入库)")
    private String instockType;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;/**
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
     * 商品分类名称,多级逗号隔开
     */
    @ApiModelProperty(name = "productCategoryname", value = "商品分类名称,多级逗号隔开")
    private String productCategoryname;
    /**
     * 交易批次号,可以是调拨申请单号等
     */
    @ApiModelProperty(name = "tradeNo", value = "交易批次号,可以是调拨申请单号等")
    private String tradeNo;
    /**
     * 供应商编号
     */
    @ApiModelProperty(name = "supplierNo", value = "供应商编号")
    private String supplierNo;
    /**
     * 入库仓库编号
     */
    @ApiModelProperty(name = "instockRepositoryNo", value = "入库仓库编号")
    private String instockRepositoryNo;
    /**
     * 入库机构编号
     */
    @ApiModelProperty(name = "instockOrgno", value = "入库机构编号")
    private String instockOrgno;
    /**
     * 成本单价
     */
    @ApiModelProperty(name = "costPrice", value = "成本单价")
    private BigDecimal costPrice;
    /**
     * 计划入库数量
     */
    @ApiModelProperty(name = "planInstocknum", value = "计划入库数量")
    private Long planInstocknum;
    /**
     * 实际入库数量
     */
    @ApiModelProperty(name = "actualInstocknum", value = "实际入库数量")
    private Long actualInstocknum;
    /**
     * 完成状态（入库中，完成）
     */
    @ApiModelProperty(name = "completeStatus", value = "完成状态（入库中，完成）")
    private String completeStatus;
    /**
     * 完成时间
     */
    @ApiModelProperty(name = "completeTime", value = "完成时间")
    private Date completeTime;
    /**
     * 成对生成的出库计划的id
     */
    @ApiModelProperty(name = "outstockPlanid", value = "成对生成的出库计划的id")
    private Long outstockPlanid;
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
     * 供应商名字
     */
    @ApiModelProperty(name = "supplierName", value = "供应商名字")
    private String supplierName;
    /**
     *  库存类型
     */
    @ApiModelProperty(name = "stockType", value = "库存类型（正常库存、问题库存、报损件库存，等等）")
    private String stockType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setInstockType(String instockType) {
        this.instockType = instockType;
    }

    public String getInstockType() {
        return this.instockType;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return this.productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setInstockRepositoryNo(String instockRepositoryNo) {
        this.instockRepositoryNo = instockRepositoryNo;
    }

    public String getInstockRepositoryNo() {
        return this.instockRepositoryNo;
    }

    public String getInstockOrgno() {
        return instockOrgno;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public void setPlanInstocknum(Long planInstocknum) {
        this.planInstocknum = planInstocknum;
    }

    public Long getPlanInstocknum() {
        return this.planInstocknum;
    }

    public void setActualInstocknum(Long actualInstocknum) {
        this.actualInstocknum = actualInstocknum;
    }

    public Long getActualInstocknum() {
        return this.actualInstocknum;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }

    public String getCompleteStatus() {
        return this.completeStatus;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() {
        return this.completeTime;
    }

    public void setOutstockPlanid(Long outstockPlanid) {
        this.outstockPlanid = outstockPlanid;
    }

    public Long getOutstockPlanid() {
        return this.outstockPlanid;
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

    public String getStatusName() {
        if (null != completeStatus) {
            return StockPlanEnum.valueOf(completeStatus).getLabel();
        }
        return null;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }
}