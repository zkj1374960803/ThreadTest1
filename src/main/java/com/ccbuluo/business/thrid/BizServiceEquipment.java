package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 物料实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "物料实体", description = "物料")
public class BizServiceEquipment {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;
    /**
     * 物料编号
     */
    @ApiModelProperty(name = "equipCode", value = "物料编号")
    private String equipCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "equipName", value = "物料名称")
    private String equipName;
    /**
     * 物料类型的id
     */
    @ApiModelProperty(name = "equiptypeId", value = "物料类型的id")
    private Long equiptypeId;
    /**
     * 计量单位
     */
    @ApiModelProperty(name = "equipUnit", value = "计量单位")
    private String equipUnit;
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
     * 更新时间
     */
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Date operateTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private Long deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public String getEquipCode() {
        return this.equipCode;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getEquipName() {
        return this.equipName;
    }

    public void setEquiptypeId(Long equiptypeId) {
        this.equiptypeId = equiptypeId;
    }

    public Long getEquiptypeId() {
        return this.equiptypeId;
    }

    public void setEquipUnit(String equipUnit) {
        this.equipUnit = equipUnit;
    }

    public String getEquipUnit() {
        return this.equipUnit;
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

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDeleteFlag() {
        return this.deleteFlag;
    }


}