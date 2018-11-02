package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.business.constants.PriceTypeEnum;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-10-29 18:14:38
 */
@ApiModel(value = "CheckedSellPriceDTO", description = "校验商品的价格DTO")
public class CheckedSellPriceDTO {
   @ApiModelProperty(name = "priceTypeEnum", value = "价格的等级阶梯类型", required = true)
   @ValidateNotNull(message = "价格等级阶梯类型")
   private PriceTypeEnum priceTypeEnum;
   @ApiModelProperty(name = "productNo", value = "商品的编号", required = true)
   private List<String> productNo;
   @ApiModelProperty(name = "sellPrice", value = "销售的价格", required = true)
   private List<Double> sellPrice;

    public PriceTypeEnum getPriceTypeEnum() {
        return priceTypeEnum;
    }

    public void setPriceTypeEnum(PriceTypeEnum priceTypeEnum) {
        this.priceTypeEnum = priceTypeEnum;
    }

    public List<String> getProductNo() {
        return productNo;
    }

    public void setProductNo(List<String> productNo) {
        this.productNo = productNo;
    }

    public List<Double> getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(List<Double> sellPrice) {
        this.sellPrice = sellPrice;
    }
}
