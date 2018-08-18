package com.ccbuluo.business.platform.stockmanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhangkangjian
 * @date 2018-08-16 10:43:45
 */
@ApiModel(value = "StockManagementDTO", description = "查询库存的列表")
public class StockManagementDTO {
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    @ApiModelProperty(name = "productType", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）")
    private String productType;
    @ApiModelProperty(name = "serviceCenterCode", value = "服务中心的code")
    private String serviceCenterCode;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getServiceCenterCode() {
        return serviceCenterCode;
    }

    public void setServiceCenterCode(String serviceCenterCode) {
        this.serviceCenterCode = serviceCenterCode;
    }
}
