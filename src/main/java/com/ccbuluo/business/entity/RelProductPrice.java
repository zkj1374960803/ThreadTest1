package com.ccbuluo.business.entity;

import java.util.Date;

/**
 * @author zhangkangjian
 * @date 2018-09-06 16:38:52
 */
public class RelProductPrice {


    private Long id;

    //物料或零配件的编号
    private String productNo;

    //商品的类型：零配件或分类
    private String productType;

    //集团定的建议的售价
    private double suggestedPrice = 0.000;

    //允许上调抬价的比例对应的小数值，正数
    private float upRate = 0;

    //允许下调优惠的比例对应的小数值，正数
    private float downRate = 0;

    //价格的级别，不同级别给不同机构用，用整数标识
    private Long priceLevel;

    //价格生效的开始时间
    private Date startTime;

    //价格生效的结束时间
    private Date endTime;

    //创建人
    private String creator;

    //创建时间
    private Long createTime;

    //更新人
    private String operator;

    //更新时间
    private Long operateTime;

    //删除标识
    private int deleteFlag;

    //备注
    private String remark;


    public Long getId() {
        return this.id;
    }

    ;

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductNo() {
        return this.productNo;
    }

    ;

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductType() {
        return this.productType;
    }

    ;

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getSuggestedPrice() {
        return this.suggestedPrice;
    }

    ;

    public void setSuggestedPrice(double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public float getUpRate() {
        return this.upRate;
    }

    ;

    public void setUpRate(float upRate) {
        this.upRate = upRate;
    }

    public float getDownRate() {
        return this.downRate;
    }

    ;

    public void setDownRate(float downRate) {
        this.downRate = downRate;
    }

    public Long getPriceLevel() {
        return this.priceLevel;
    }

    ;

    public void setPriceLevel(Long priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    ;

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    ;

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreator() {
        return this.creator;
    }

    ;

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    ;

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return this.operator;
    }

    ;

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getOperateTime() {
        return this.operateTime;
    }

    ;

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    ;

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getRemark() {
        return this.remark;
    }

    ;

    public void setRemark(String remark) {
        this.remark = remark;
    }


}