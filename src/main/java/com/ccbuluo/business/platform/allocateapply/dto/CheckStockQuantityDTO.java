package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-08-15 18:06:09
 */
@ApiModel(value = "CheckStockQuantityDTO", description = "商品信息DTO")
public class CheckStockQuantityDTO {
    @ApiModelProperty(name = "outstockOrgno", value = "出库的机构")
    private String outstockOrgno;
    @ApiModelProperty(name = "productInfoList", value = "商品的信息")
    List<ProductStockInfoDTO> productInfoList;
    @ApiModelProperty(name = "sellerOrgno", value = "库存的来源", hidden = true)
    private String sellerOrgno;

    public String getSellerOrgno() {
        return sellerOrgno;
    }

    public void setSellerOrgno(String sellerOrgno) {
        this.sellerOrgno = sellerOrgno;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public List<ProductStockInfoDTO> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(List<ProductStockInfoDTO> productInfoList) {
        this.productInfoList = productInfoList;
    }
}
