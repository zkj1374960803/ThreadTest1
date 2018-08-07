package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 车辆基本信息表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "车辆基本信息表实体", description = "车辆基本信息表")
public class BasicCarcoreInfo {
    /**
     * 主键，自增
     */
    @ApiModelProperty(name = "id", value = "主键，自增")
    private Long id;
    /**
     * 车辆编号,CL+年月日+3位自增编号+1位随机码，例如：CL1805030018  
     */
    @ApiModelProperty(name = "carNumber", value = "车辆编号,CL+年月日+3位自增编号+1位随机码，例如：CL1805030018  ")
    private String carNumber;
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
     * 北斗设备编号
     */
    @ApiModelProperty(name = "beidouNumber", value = "北斗设备编号")
    private String beidouNumber;
    /**
     * 所属品牌id，引用自品牌管理表(basic_carbrand_manage)的id字段
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌id，引用自品牌管理表(basic_carbrand_manage)的id字段")
    private Long carbrandId;
    /**
     * 所属车系id，引用自车系管理表(basic_carseries_manage)的id字段
     */
    @ApiModelProperty(name = "carseriesId", value = "所属车系id，引用自车系管理表(basic_carseries_manage)的id字段")
    private Long carseriesId;
    /**
     * 所属车型id，引用自车型管理表(basic_carmodel_manage)的id字段
     */
    @ApiModelProperty(name = "carmodelId", value = "所属车型id，引用自车型管理表(basic_carmodel_manage)的id字段")
    private Long carmodelId;
    /**
     * 车辆出厂时间
     */
    @ApiModelProperty(name = "produceTime", value = "车辆出厂时间")
    private Date produceTime;
    /**
     * 客户经理的uuid
     */
    @ApiModelProperty(name = "cusmanagerUuid", value = "客户经理的uuid")
    private String cusmanagerUuid;
    /**
     * 客户经理名称
     */
    @ApiModelProperty(name = "cusmanagerName", value = "客户经理名称")
    private String cusmanagerName;
    /**
     * 租赁系统中是否分配了门店
     */
    @ApiModelProperty(name = "storeAssigned", value = "租赁系统中是否分配了门店")
    private Long storeAssigned;
    /**
     * 门店的机构编码
     */
    @ApiModelProperty(name = "storeCode", value = "门店的机构编码")
    private String storeCode;
    /**
     * 门店机构的名称
     */
    @ApiModelProperty(name = "storeName", value = "门店机构的名称")
    private String storeName;
    /**
     * 
     */
    @ApiModelProperty(name = "remark", value = "")
    private String remark;
    /**
     * 维修车的分配状态(1：租赁，0：未分配)
     */
    @ApiModelProperty(name = "carStatus", value = "维修车的分配状态(1：租赁，0：未分配)")
    private Long carStatus;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 创建用户
     */
    @ApiModelProperty(name = "creator", value = "创建用户")
    private String creator;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "operateTime", value = "修改时间")
    private Date operateTime;
    /**
     * 修改用户
     */
    @ApiModelProperty(name = "operator", value = "修改用户")
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

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return this.carNumber;
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

    public void setCusmanagerUuid(String cusmanagerUuid) {
        this.cusmanagerUuid = cusmanagerUuid;
    }

    public String getCusmanagerUuid() {
        return this.cusmanagerUuid;
    }

    public void setCusmanagerName(String cusmanagerName) {
        this.cusmanagerName = cusmanagerName;
    }

    public String getCusmanagerName() {
        return this.cusmanagerName;
    }

    public void setStoreAssigned(Long storeAssigned) {
        this.storeAssigned = storeAssigned;
    }

    public Long getStoreAssigned() {
        return this.storeAssigned;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreCode() {
        return this.storeCode;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setCarStatus(Long carStatus) {
        this.carStatus = carStatus;
    }

    public Long getCarStatus() {
        return this.carStatus;
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