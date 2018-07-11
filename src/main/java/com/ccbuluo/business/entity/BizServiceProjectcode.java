package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 编号实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "编号实体", description = "编号实体")
public class BizServiceProjectcode extends IdEntity{
    /**
     * 编号的前缀，值为枚举中的值
     */
    @ApiModelProperty(name = "codePrefix", value = "编号的前缀，值为枚举中的值")
    private String codePrefix;
    /**
     *  现在最大的自增数
     */
    @ApiModelProperty(name = "currentCount", value = "现在最大的自增数")
    private Integer currentCount;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;
    /**
     * 1：正常  0：失败
     */
    @ApiModelProperty(name = "codeStatus", value = "1：正常  0：失败")
    private Long codeStatus;

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    public String getCodePrefix() {
        return this.codePrefix;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public Integer getCurrentCount() {
        return this.currentCount;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setCodeStatus(Long codeStatus) {
        this.codeStatus = codeStatus;
    }

    public Long getCodeStatus() {
        return this.codeStatus;
    }


}