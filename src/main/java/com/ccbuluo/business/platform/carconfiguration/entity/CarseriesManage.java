package com.ccbuluo.business.platform.carconfiguration.entity;

import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 车系管理表
 * @author wuyibo
 * @date 2018-05-08 11:47:00
 */
@ApiModel(value = "车系管理表实体CarseriesManage", description = "车系管理表")
public class CarseriesManage extends CarCommonEntity {

    /**
     * 车系名称
     */
    @ValidateLength(min = 0, max = 20,message = "车系名称长度不合法")
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
    @ValidateNotNull(message = "排序号不能为空")
    @ValidateMax(value = 100,message = "排序号不能大于100")
    @ApiModelProperty(name = "sortNumber", value = "排序号")
    private Integer sortNumber;
    /**
     * 车系编号, CX+5位自增编号，例如：CX00001
     */
    @ApiModelProperty(name = "carseriesNumber", value = "车系编号, CX+5位自增编号，例如：CX00001")
    private String carseriesNumber;


    public String getCarseriesName() {
        return carseriesName;
    }

    public void setCarseriesName(String carseriesName) {
        this.carseriesName = carseriesName;
    }

    public Long getCarbrandId() {
        return carbrandId;
    }

    public void setCarbrandId(Long carbrandId) {
        this.carbrandId = carbrandId;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getCarseriesNumber() {
        return carseriesNumber;
    }

    public void setCarseriesNumber(String carseriesNumber) {
        this.carseriesNumber = carseriesNumber;
    }

}
