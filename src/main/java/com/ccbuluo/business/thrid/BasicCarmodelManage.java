package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "实体", description = "")
public class BasicCarmodelManage {
    /**
     * 主键，自增长
     */
    @ApiModelProperty(name = "id", value = "主键，自增长")
    private Long id;
    /**
     * 车型名称
     */
    @ApiModelProperty(name = "carmodelName", value = "车型名称")
    private String carmodelName;
    /**
     * 所属品牌，引用自品牌管理表（basic_carbrand_manage）的id字段
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌，引用自品牌管理表（basic_carbrand_manage）的id字段")
    private Long carbrandId;
    /**
     * 所属车系，引用自车系管理表(basic_carseries_manage)的id字段
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系，引用自车系管理表(basic_carseries_manage)的id字段")
    private Long carseriesId;
    /**
     * 车型标题
     */
    @ApiModelProperty(name = "modelTitle", value = "车型标题")
    private String modelTitle;
    /**
     * 车辆类型：81:微型车83:商务车84:越野车89:面包车90:微型客车96:小型车98:中型车99:中大型车100:豪华车103:电动车(常用)112:电动单车113:单车
     */
    @ApiModelProperty(name = "carType", value = "车辆类型：81:微型车83:商务车84:越野车89:面包车90:微型客车96:小型车98:中型车99:中大型车100:豪华车103:电动车(常用)112:电动单车113:单车")
    private Long carType;
    /**
     * 车型主图
     */
    @ApiModelProperty(name = "modelMasterImage", value = "车型主图")
    private String modelMasterImage;
    /**
     * 车型图片,副图，用英文分号分隔。
     */
    @ApiModelProperty(name = "modelImage", value = "车型图片,副图，用英文分号分隔。")
    private String modelImage;
    /**
     * 车型编号,  C+6位自增编号，例如：C000001
     */
    @ApiModelProperty(name = "carmodelNumber", value = "车型编号,  C+6位自增编号，例如：C000001")
    private String carmodelNumber;
    /**
     * 车型状态，0为未启用，1为启用，默认为1
     */
    @ApiModelProperty(name = "carmodelStatus", value = "车型状态，0为未启用，1为启用，默认为1")
    private Long carmodelStatus;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 创建用户,引用用户表的用户名
     */
    @ApiModelProperty(name = "creator", value = "创建用户,引用用户表的用户名")
    private String creator;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "operateTime", value = "修改时间")
    private Date operateTime;
    /**
     * 修改用户，引用用户表的用户名
     */
    @ApiModelProperty(name = "operator", value = "修改用户，引用用户表的用户名")
    private String operator;
    /**
     * 删除标识，缺省值0,0为未删除，1为已删除
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识，缺省值0,0为未删除，1为已删除")
    private Long deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getCarmodelName() {
        return this.carmodelName;
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

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public String getModelTitle() {
        return this.modelTitle;
    }

    public void setCarType(Long carType) {
        this.carType = carType;
    }

    public Long getCarType() {
        return this.carType;
    }

    public void setModelMasterImage(String modelMasterImage) {
        this.modelMasterImage = modelMasterImage;
    }

    public String getModelMasterImage() {
        return this.modelMasterImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }

    public String getModelImage() {
        return this.modelImage;
    }

    public void setCarmodelNumber(String carmodelNumber) {
        this.carmodelNumber = carmodelNumber;
    }

    public String getCarmodelNumber() {
        return this.carmodelNumber;
    }

    public void setCarmodelStatus(Long carmodelStatus) {
        this.carmodelStatus = carmodelStatus;
    }

    public Long getCarmodelStatus() {
        return this.carmodelStatus;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }


}