package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 批量更新用（根据code）
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "CarcoreInfoByVinDTO", description = "批量更新用（根据code）")
public class CarcoreInfoByVinDTO {
    /**
     * 车架号
     */
    @ApiModelProperty(name = "vinNumber", value = "车架号")
    private String vinNumber;

    /**
     * 门店code
     */
    @ApiModelProperty(name = "storeCode", value = "门店code")
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty(name = "storeName", value = "门店名称")
    private String storeName;

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}