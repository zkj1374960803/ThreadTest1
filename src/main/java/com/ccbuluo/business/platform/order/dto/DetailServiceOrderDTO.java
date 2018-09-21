package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import com.ccbuluo.business.entity.BizCarPosition;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 描述 订单详情DTO
 * @author liuduo
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "DetailServiceOrderDTO", description = "订单详情DTO")
public class DetailServiceOrderDTO extends AftersaleCommonEntity {
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "订单编号")
    private String serviceOrderno;
    /**
     * 订单信息
     */
    @ApiModelProperty(name = "bizServiceOrder", value = "订单信息")
    private BizServiceOrder bizServiceOrder;
    /**
     * 车辆信息
     */
    @ApiModelProperty(name = "carcoreInfoDTO", value = "车辆信息")
    private CarcoreInfoDTO carcoreInfoDTO;
    /**
     * 车辆停放地址
     */
    @ApiModelProperty(name = "bizCarPosition", value = "车辆停放地址")
    private BizCarPosition bizCarPosition;

    public String getServiceOrderno() {
        return serviceOrderno;
    }

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
    }

    public BizServiceOrder getBizServiceOrder() {
        return bizServiceOrder;
    }

    public void setBizServiceOrder(BizServiceOrder bizServiceOrder) {
        this.bizServiceOrder = bizServiceOrder;
    }

    public CarcoreInfoDTO getCarcoreInfoDTO() {
        return carcoreInfoDTO;
    }

    public void setCarcoreInfoDTO(CarcoreInfoDTO carcoreInfoDTO) {
        this.carcoreInfoDTO = carcoreInfoDTO;
    }

    public BizCarPosition getBizCarPosition() {
        return bizCarPosition;
    }

    public void setBizCarPosition(BizCarPosition bizCarPosition) {
        this.bizCarPosition = bizCarPosition;
    }
}
