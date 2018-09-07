package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 描述 新增维修单详单DTO
 * @author baoweiding
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "SaveOrderDetailDTO", description = "新增维修单详单DTO")
public class SaveOrderDetailDTO extends AftersaleCommonEntity {
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "订单编号")
    private String serviceOrderno;
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

    public String getServiceOrderno() {
        return serviceOrderno;
    }

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
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
