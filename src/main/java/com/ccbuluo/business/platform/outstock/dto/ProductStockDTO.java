package com.ccbuluo.business.platform.outstock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品和库存数量对应dto
 * @author liuduo
 * @version v1.0.0
 * @date 2018-10-29 09:53:19
 */
@ApiModel(value = "商品和库存数量对应dto", description = "商品和库存数量对应dto")
public class ProductStockDTO {
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "productNo", value = "商品编号")
    private String productNo;
    /**
     * 商品数量
     */
    @ApiModelProperty(name = "productNum", value = "商品数量")
    private Long productNum;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }
}
