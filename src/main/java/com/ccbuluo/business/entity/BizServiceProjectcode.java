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

    /**
     *
     * @author liupengfei
     * @date 2018-07-03 17:27:05
     */
    public enum CodePrefixEnum {
        FW("服务中心"),
        FC("仓库"),
        FP("零配件"),
        FK("零配件分类"),
        FM("零配件模板"),
        FS("员工"),
        FA("物料"),
        FL("工时"),
        FG("供应商"),
        FR("维修车"),
        FO("客户经理组织架构"),
        FD("车型标签"),
        FB("车品牌"),
        FN("车系"),
        FH("车型"),
        FJ("车辆"),
        SW("申请单号"),
        R("入库单号"),
        C("出库单号"),
        PK("盘库单号"),
        TH("退换单号");


        CodePrefixEnum(String label){
            this.label = label;
        }

        private String label;

        public String getLabel(){
            return label;
        }
    }

}