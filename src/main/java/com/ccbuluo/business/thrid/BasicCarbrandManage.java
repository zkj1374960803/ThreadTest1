package com.ccbuluo.business.thrid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 品牌管理表实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "品牌管理表实体", description = "品牌管理表")
public class BasicCarbrandManage {
    /**
     * 主键，自增
     */
    @ApiModelProperty(name = "id", value = "主键，自增")
    private Long id;
    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "carbrandName", value = "品牌名称")
    private String carbrandName;
    /**
     * 首字母
     */
    @ApiModelProperty(name = "initial", value = "首字母")
    private String initial;
    /**
     * 品牌logo
     */
    @ApiModelProperty(name = "carbrandLogo", value = "品牌logo")
    private String carbrandLogo;
    /**
     * 排序号
     */
    @ApiModelProperty(name = "sortNumber", value = "排序号")
    private Long sortNumber;
    /**
     *  品牌编号 ,P+3位自增编号，例如：P001
     */
    @ApiModelProperty(name = "carbrandNumber", value = " 品牌编号 ,P+3位自增编号，例如：P001")
    private String carbrandNumber;
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
     * 最后操作时间
     */
    @ApiModelProperty(name = "operateTime", value = "最后操作时间")
    private Date operateTime;
    /**
     * 最后操作人
     */
    @ApiModelProperty(name = "operator", value = "最后操作人")
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

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public String getCarbrandName() {
        return this.carbrandName;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getInitial() {
        return this.initial;
    }

    public void setCarbrandLogo(String carbrandLogo) {
        this.carbrandLogo = carbrandLogo;
    }

    public String getCarbrandLogo() {
        return this.carbrandLogo;
    }

    public void setSortNumber(Long sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Long getSortNumber() {
        return this.sortNumber;
    }

    public void setCarbrandNumber(String carbrandNumber) {
        this.carbrandNumber = carbrandNumber;
    }

    public String getCarbrandNumber() {
        return this.carbrandNumber;
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