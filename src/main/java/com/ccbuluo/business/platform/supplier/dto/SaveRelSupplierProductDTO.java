package com.ccbuluo.business.platform.supplier.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 保存供应商关联商品关系DTO
 * @author zhangkangjian
 * @date 2018-08-01 10:12:08
 */
@ApiModel(value = "SaveRelSupplierProductDTO", description = "保存供应商关联商品关系DTO")
public class SaveRelSupplierProductDTO {
    /**
     * 供应商关联商品关系
     */
    @ApiModelProperty(name = "supplierProductList", value = "供应商关联商品关系")
    private List<RelSupplierProduct> supplierProductList;

    public List<RelSupplierProduct> getSupplierProductList() {
        return supplierProductList;
    }

    public void setSupplierProductList(List<RelSupplierProduct> supplierProductList) {
        this.supplierProductList = supplierProductList;
    }
}
