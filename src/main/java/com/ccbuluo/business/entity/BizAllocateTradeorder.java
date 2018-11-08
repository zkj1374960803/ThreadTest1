package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 调拨申请交易订单实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "调拨申请交易订单实体", description = "调拨申请交易订单")
public class BizAllocateTradeorder extends AftersaleCommonEntity{
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 
     */
    @ApiModelProperty(name = "orderNo", value = "")
    private String orderNo;
    /**
     * 
     */
    @ApiModelProperty(name = "applyNo", value = "")
    private String applyNo;
    /**
     * 买方机构的编号
     */
    @ApiModelProperty(name = "purchaserOrgno", value = "买方机构的编号")
    private String purchaserOrgno;
    /**
     * 卖方机构的编号
     */
    @ApiModelProperty(name = "sellerOrgno", value = "卖方机构的编号")
    private String sellerOrgno;
    /**
     * 待支付、支付完成、订单取消
     */
    @ApiModelProperty(name = "orderStatus", value = "待支付、支付完成、订单取消")
    private String orderStatus;
    /**
     * 订单中所有商品的总价
     */
    @ApiModelProperty(name = "totalPrice", value = "订单中所有商品的总价")
    private BigDecimal totalPrice;
    /**
     * 支付人的uuid
     */
    @ApiModelProperty(name = "payer", value = "支付人的uuid")
    private String payer;
    /**
     * 支付方式
     */
    @ApiModelProperty(name = "payMethod", value = "支付方式")
    private String payMethod;
    /**
     * 完成支付时间
     */
    @ApiModelProperty(name = "payedTime", value = "完成支付时间")
    private Date payedTime;
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
     *  采购类型
     */
    @ApiModelProperty(name = "tradeType", value = "交易类型（采购或是调拨）")
    private String tradeType;

    /**
     *  预付款金额
     */
    @ApiModelProperty(name = "perpayAmount", value = "预付款金额")
    private BigDecimal perpayAmount;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyNo() {
        return this.applyNo;
    }

    public void setPurchaserOrgno(String purchaserOrgno) {
        this.purchaserOrgno = purchaserOrgno;
    }

    public String getPurchaserOrgno() {
        return this.purchaserOrgno;
    }

    public void setSellerOrgno(String sellerOrgno) {
        this.sellerOrgno = sellerOrgno;
    }

    public String getSellerOrgno() {
        return this.sellerOrgno;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayer() {
        return this.payer;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMethod() {
        return this.payMethod;
    }

    public void setPayedTime(Date payedTime) {
        this.payedTime = payedTime;
    }

    public Date getPayedTime() {
        return this.payedTime;
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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getPerpayAmount() {
        return perpayAmount;
    }

    public void setPerpayAmount(BigDecimal perpayAmount) {
        this.perpayAmount = perpayAmount;
    }
}