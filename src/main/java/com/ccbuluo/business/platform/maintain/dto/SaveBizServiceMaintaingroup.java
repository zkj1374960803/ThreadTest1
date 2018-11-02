package com.ccbuluo.business.platform.maintain.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.core.annotation.validate.ValidateDigits;
import com.ccbuluo.core.annotation.validate.ValidateLength;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保养实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "保养实体", description = "保养实体")
public class SaveBizServiceMaintaingroup extends AftersaleCommonEntity{
    /**
     * 保养套餐的编号
     */
    @ApiModelProperty(name = "groupCode", value = "保养套餐的编号")
    private String groupCode;
    /**
     * 保养套餐的名称
     */
    @ValidateLength(min = 0, max = 15, message = "保养名称不能超过15个字")
    @ValidateNotBlank(message = "保养名称不能为空")
    @ApiModelProperty(name = "groupName", value = "保养套餐的名称")
    private String groupName;
    /**
     * 套餐的ToC价格
     */
    @ValidateNotNull(message = "保养额度不能为空")
    @ValidateDigits(integer = 99999, fraction = 99, message = "保养额度不能超过99999.99")
    @ApiModelProperty(name = "groupPrice", value = "套餐的ToC价格")
    private BigDecimal groupPrice;
    /**
     * 套餐的状态
     */
    @ApiModelProperty(name = "groupStatus", value = "套餐的状态")
    private String groupStatus;
    /**
     * 套餐图片的相对路径
     */
    @ValidateNotBlank(message = "图片不能为空")
    @ApiModelProperty(name = "groupImage", value = "套餐图片的相对路径")
    private String groupImage;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 工时集合
     */
    @ApiModelProperty(name = "saveMaintaintemDTOS", value = "工时集合")
    private List<SaveMaintaintemDTO> saveMaintaintemDTOS;
    /**
     * 零配件集合
     */
    @ApiModelProperty(name = "saveMerchandiseDTOS", value = "零配件集合")
    private List<SaveMerchandiseDTO> saveMerchandiseDTOS;

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public BigDecimal getGroupPrice() {
        return this.groupPrice;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupStatus() {
        return this.groupStatus;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupImage() {
        return this.groupImage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SaveMaintaintemDTO> getSaveMaintaintemDTOS() {
        return saveMaintaintemDTOS;
    }

    public void setSaveMaintaintemDTOS(List<SaveMaintaintemDTO> saveMaintaintemDTOS) {
        this.saveMaintaintemDTOS = saveMaintaintemDTOS;
    }

    public List<SaveMerchandiseDTO> getSaveMerchandiseDTOS() {
        return saveMerchandiseDTOS;
    }

    public void setSaveMerchandiseDTOS(List<SaveMerchandiseDTO> saveMerchandiseDTOS) {
        this.saveMerchandiseDTOS = saveMerchandiseDTOS;
    }
}