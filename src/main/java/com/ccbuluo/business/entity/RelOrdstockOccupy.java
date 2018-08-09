package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单占用库存关系实体
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 20:21:24
 */
@ApiModel(value = "RelOrdstockOccupy", description = "订单占用库存关系实体")
public class RelOrdstockOccupy {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 交易类型
     */
    @ApiModelProperty(name = "orderType", value = "采购或是调拨")
    private String orderType;
    /**
     * 申请单code
     */
    @ApiModelProperty(name = "docNo", value = "申请单code")
    private String docNo;
    /**
     * 占用批次库存id
     */
    @ApiModelProperty(name = "stockId", value = "占用批次库存id")
    private Long stockId;
    /**
     * 占用商品的数量
     */
    @ApiModelProperty(name = "occupyNum", value = "占用商品的数量")
    private Long occupyNum;
    /**
     * 状态（占用中、撤销占用、售出）
     */
    @ApiModelProperty(name = "occupyStatus", value = "状态（占用中、撤销占用、售出）")
    private String occupyStatus;
    /**
     * 开始占用该库存的时间
     */
    @ApiModelProperty(name = "occupyStarttime", value = "开始占用该库存的时间")
    private Date occupyStarttime;
    /**
     * 结束占用该库存的时间
     */
    @ApiModelProperty(name = "occupyEndtime", value = "结束占用该库存的时间")
    private Date occupyEndtime;
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
     * 卖方机构的编号
     */
    @ApiModelProperty(name = "outstockOrgno", value = "卖方机构的编号", hidden = true)
    private String outstockOrgno;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识", hidden = true)
    private Integer deleteFlag = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getOccupyNum() {
        return occupyNum;
    }

    public void setOccupyNum(Long occupyNum) {
        this.occupyNum = occupyNum;
    }

    public String getOccupyStatus() {
        return occupyStatus;
    }

    public void setOccupyStatus(String occupyStatus) {
        this.occupyStatus = occupyStatus;
    }

    public Date getOccupyStarttime() {
        return occupyStarttime;
    }

    public void setOccupyStarttime(Date occupyStarttime) {
        this.occupyStarttime = occupyStarttime;
    }

    public Date getOccupyEndtime() {
        return occupyEndtime;
    }

    public void setOccupyEndtime(Date occupyEndtime) {
        this.occupyEndtime = occupyEndtime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getInstockOrgno() {
        return instockOrgno;
    }

    public void setInstockOrgno(String instockOrgno) {
        this.instockOrgno = instockOrgno;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
