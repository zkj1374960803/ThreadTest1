package com.ccbuluo.business.platform.carconfiguration.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 实体
 * @author weijb
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class CarcoreInfo {
    /**
     * 主键，自增
     */
    @ApiModelProperty(name = "id", value = "主键，自增")
    private Long id;
    /**
     * 车辆编号
     */
    @ApiModelProperty(name = "carcoreCode", value = "车辆编号")
    private String carcoreCode;
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号")
    private String vinNumber;
    /**
     * 发动机号
     */
    @ApiModelProperty(name = "engineNumber", value = "发动机号")
    private String engineNumber;
    /**
     * 车辆北斗ID
     */
    @ApiModelProperty(name = "beidouNumber", value = "车辆北斗ID")
    private String beidouNumber;
    /**
     * 所属品牌id
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌id")
    private Long carbrandId;
    /**
     * 所属车系id
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系id")
    private Long carseriesId;
    /**
     * 所属车型id
     */
    @ApiModelProperty(name = "carmodelId", value = "所属车型id")
    private Long carmodelId;
    /**
     * 出厂时间
     */
    @ApiModelProperty(name = "produceTime", value = "出厂时间")
    private Date produceTime;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
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
     * 最后操作时间
     */
    @ApiModelProperty(name = "operateTime", value = "最后操作时间")
    private Date operateTime;
    /**
     * 删除标示
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标示")
    private Integer deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setCarcoreCode(String carcoreCode) {
        this.carcoreCode = carcoreCode;
    }

    public String getCarcoreCode() {
        return this.carcoreCode;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getVinNumber() {
        return this.vinNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getEngineNumber() {
        return this.engineNumber;
    }

    public void setBeidouNumber(String beidouNumber) {
        this.beidouNumber = beidouNumber;
    }

    public String getBeidouNumber() {
        return this.beidouNumber;
    }

    public void setCarbrandId(Long carbrandId) {
        this.carbrandId = carbrandId;
    }

    public Long getCarbrandId() {
        return this.carbrandId;
    }

    public void setCarseriesId(Long carseriesId) {
        this.carseriesId = carseriesId;
    }

    public Long getCarseriesId() {
        return this.carseriesId;
    }

    public void setCarmodelId(Long carmodelId) {
        this.carmodelId = carmodelId;
    }

    public Long getCarmodelId() {
        return this.carmodelId;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public Date getProduceTime() {
        return this.produceTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
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


}