package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zhangkangjian
 * @date 2018-09-13 16:17:04
 */
@ApiModel(value = "PurchaseProductInfo", description = "采购商品信息")
public class PurchaseProductInfo {
    @ApiModelProperty(name = "id", value = "采购商品id")
    @ValidateNotBlank
    private Long id;
    @ApiModelProperty(name = "sellPrice", value = "销售价格")
    @ValidateNotBlank
    private BigDecimal sellPrice;

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
