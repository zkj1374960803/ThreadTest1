package com.ccbuluo.business.platform.carconfiguration.entity;


import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌管理表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "品牌管理表实体CarbrandManage", description = "品牌管理表")
public class CarbrandManage extends CarCommonEntity {


    /**
     * 品牌名称
     */
    @ValidateLength(min = 0, max = 20,message = "品牌名称长度不合法")
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
    @ValidateNotNull(message = "排序号不能为空")
    @ValidateMax(value = 100,message = "排序号不能大于100")
    @ApiModelProperty(name = "sortNumber", value = "排序号")
    private Integer sortNumber;
    /**
     *  品牌编号 ,P+3位自增编号，例如：P001
     */
    @ApiModelProperty(name = "carbrandNumber", value = " 品牌编号 ,P+3位自增编号，例如：P001")
    private String carbrandNumber;

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public String getCarbrandNumber() {
        return carbrandNumber;
    }

    public void setCarbrandNumber(String carbrandNumber) {
        this.carbrandNumber = carbrandNumber;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getCarbrandLogo() {
        return carbrandLogo;
    }

    public void setCarbrandLogo(String carbrandLogo) {
        this.carbrandLogo = carbrandLogo;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }


}
