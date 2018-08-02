package com.ccbuluo.business.platform.custmanager.entity;

import java.util.Date;

/**
 * 客户经理实体
 * @author zhangkangjian
 * @date 2018-07-18 09:49:01
 */
public class BizServiceCustmanager {
    private  Long id;

    //办公手机号
    private  String officePhone;

    //收货地址
    private  String receivingAddress;

    //用户的uuid
    private  String userUuid;
    //用户名称
    private  String name;

    //备注
    private  String remark;

    //创建人
    private  String creator;

    //创建时间
    private Date createTime;

    //更新人
    private  String operator;

    //更新时间
    private  Date operateTime;

    //删除标识
    private  Integer deleteFlag = 0;
    // 服务中心
    private String servicecenterCode;
    // 维修车
    private String mendCode;

    public String getMendCode() {
        return mendCode;
    }

    public void setMendCode(String mendCode) {
        this.mendCode = mendCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getServicecenterCode() {
        return servicecenterCode;
    }

    public void setServicecenterCode(String servicecenterCode) {
        this.servicecenterCode = servicecenterCode;
    }
}
