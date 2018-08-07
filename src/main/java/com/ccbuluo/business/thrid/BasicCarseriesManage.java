package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 车系管理表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "车系管理表实体", description = "车系管理表")
public class BasicCarseriesManage {
    /**
     * 主键，自增
     */
    @ApiModelProperty(name = "id", value = "主键，自增")
    private Long id;
    /**
     * 车系名称
     */
    @ApiModelProperty(name = "carseriesName", value = "车系名称")
    private String carseriesName;
    /**
     * 所属品牌，引用basic_carbrand_manage 表的id字段
     */
    @ApiModelProperty(name = "carbrandId", value = "所属品牌，引用basic_carbrand_manage 表的id字段")
    private Long carbrandId;
    /**
     * 排序号
     */
    @ApiModelProperty(name = "sortNumber", value = "排序号")
    private Long sortNumber;
    /**
     * 车系编号, CX+5位自增编号，例如：CX00001
     */
    @ApiModelProperty(name = "carseriesNumber", value = "车系编号, CX+5位自增编号，例如：CX00001")
    private String carseriesNumber;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "operateTime", value = "修改时间")
    private Date operateTime;
    /**
     * 最后修改用户
     */
    @ApiModelProperty(name = "operator", value = "最后修改用户")
    private String operator;
    /**
     * 删除标识，0为未删除，1为已删除，缺省值0
     */
    @ApiModelProperty(name = "deleteFlag", value = "删除标识，0为未删除，1为已删除，缺省值0")
    private Long deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setCarseriesName(String carseriesName) {
        this.carseriesName = carseriesName;
    }

    public String getCarseriesName() {
        return this.carseriesName;
    }

    public void setCarbrandId(Long carbrandId) {
        this.carbrandId = carbrandId;
    }

    public Long getCarbrandId() {
        return this.carbrandId;
    }

    public void setSortNumber(Long sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Long getSortNumber() {
        return this.sortNumber;
    }

    public void setCarseriesNumber(String carseriesNumber) {
        this.carseriesNumber = carseriesNumber;
    }

    public String getCarseriesNumber() {
        return this.carseriesNumber;
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