package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-09-13 16:06:31
 */
@ApiModel(value = "ConfirmationQuoteDTO", description = "确认报价")
public class ConfirmationQuoteDTO {
    @ApiModelProperty(name = "purchaseProductInfo", value = "采购单号")
    private String applyNo;
    @ApiModelProperty(name = "purchaseProductInfo", value = "采购商品信息")
    private List<PurchaseProductInfo> purchaseProductInfo;
    @ApiModelProperty(name = "perpayAmountDTO", value = "预付款金额")
    private List<PerpayAmountDTO> perpayAmountDTO;

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public List<PurchaseProductInfo> getPurchaseProductInfo() {
        return purchaseProductInfo;
    }

    public void setPurchaseProductInfo(List<PurchaseProductInfo> purchaseProductInfo) {
        this.purchaseProductInfo = purchaseProductInfo;
    }

    public List<PerpayAmountDTO> getPerpayAmountDTO() {
        return perpayAmountDTO;
    }

    public void setPerpayAmountDTO(List<PerpayAmountDTO> perpayAmountDTO) {
        this.perpayAmountDTO = perpayAmountDTO;
    }
}
