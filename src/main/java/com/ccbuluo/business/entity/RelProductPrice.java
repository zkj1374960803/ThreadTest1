package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.ValidateDigits;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangkangjian
 * @date 2018-09-06 16:38:52
 */
public class RelProductPrice {

    @ApiModelProperty(name = "id", value = "", hidden = true)
    private Long id;

    //物料或零配件的编号
    @ApiModelProperty(name = "productNo", value = "物料或零配件的编号", required = true)
    private String productNo;

    //商品的类型：零配件或分类
    @ApiModelProperty(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)", required = true)
    private String productType;

    //集团定的建议的售价
    @ApiModelProperty(name = "suggestedPrice", value = "集团定的建议的售价", required = true)
    @ValidateDigits(integer = 7, fraction = 2)
    private Double suggestedPrice = 0D;

    //允许上调抬价的比例对应的小数值，正数
    @ApiModelProperty(name = "upRate", value = "许上调抬价的比例对应的小数值，正数", hidden = true)
    private float upRate = 0;

    //允许下调优惠的比例对应的小数值，正数
    @ApiModelProperty(name = "downRate", value = "允许下调优惠的比例对应的小数值，正数", hidden = true)
    private float downRate = 0;

    //价格的级别，不同级别给不同机构用，用整数标识
    @ApiModelProperty(name = "priceLevel", value = "价格的级别，不同级别给不同机构用，用整数标识(用户4，客户经理价格3，服务中心价格2)")
    private Long priceLevel = 0L;

    //价格生效的开始时间
    @ApiModelProperty(name = "startTime", value = "价格生效的开始时间", hidden = true)
    private Date startTime;

    //价格生效的结束时间
    @ApiModelProperty(name = "endTime", value = "价格生效的结束时间", hidden = true)
    private Date endTime;

    //创建人
    @ApiModelProperty(name = "creator", value = "", hidden = true)
    private String creator;

    //创建时间
    @ApiModelProperty(name = "createTime", value = "", hidden = true)
    private Long createTime;

    //更新人
    @ApiModelProperty(name = "operator", value = "", hidden = true)
    private String operator;

    //更新时间
    @ApiModelProperty(name = "operateTime", value = "", hidden = true)
    private Long operateTime;

    //删除标识
    @ApiModelProperty(name = "deleteFlag", value = "", hidden = true)
    private int deleteFlag;

    // 备注
    @ApiModelProperty(name = "remark", value = "", hidden = true)
    private String remark;

    // 申请单的编号
    @ApiModelProperty(name = "applyNoList", value = "申请单的编号", hidden = true)
    private List<String> applyNoList;


    public List<String> getApplyNoList() {
        return applyNoList;
    }

    public void setApplyNoList(List<String> applyNoList) {
        this.applyNoList = applyNoList;
    }

    public enum PriceLevelEnum{
        USER(4L),CUSTMANAGER(3L),SERVICECENTER(2L),PURCHASE(0L),PLATFORM(0L);
        private long priceLevel;
        PriceLevelEnum(long priceLevel) {
            this.priceLevel = priceLevel;
        }

        public long getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(long priceLevel) {
            this.priceLevel = priceLevel;
        }

        public static Map<Long, PriceLevelEnum> map = new HashMap<Long, PriceLevelEnum>();
        static {
            for (PriceLevelEnum legEnum : PriceLevelEnum.values()) {
                map.put(legEnum.priceLevel, legEnum);
            }
        }
    }

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

    public Double getSuggestedPrice() {
        return this.suggestedPrice;
    }

    ;

    public void setSuggestedPrice(Double suggestedPrice) {
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

    public RelProductPrice(String productNo, String productType, Double suggestedPrice, Long priceLevel) {
        this.productNo = productNo;
        this.productType = productType;
        this.suggestedPrice = suggestedPrice;
        this.priceLevel = priceLevel;
    }

    public RelProductPrice() {
    }
}