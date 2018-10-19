package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品库存数量DTO
 * @author zhangkangjian
 * @date 2018-08-15 18:07:52
 */
@ApiModel(value = "ProductStockInfoDTO", description = "商品库存数量DTO")
public class ProductStockInfoDTO {
    @ValidateNotBlank
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    @ValidateNotBlank
    @ApiModelProperty(name = "applyProductNum", value = "申请的库存数量")
    private Long applyProductNum;
    @ApiModelProperty(name = "realProductNum", value = "实际库存的数量")
    private Long realProductNum;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Long getApplyProductNum() {
        return applyProductNum;
    }

    public void setApplyProductNum(Long applyProductNum) {
        this.applyProductNum = applyProductNum;
    }

    public Long getRealProductNum() {
        return realProductNum;
    }

    public void setRealProductNum(Long realProductNum) {
        this.realProductNum = realProductNum;
    }
}
