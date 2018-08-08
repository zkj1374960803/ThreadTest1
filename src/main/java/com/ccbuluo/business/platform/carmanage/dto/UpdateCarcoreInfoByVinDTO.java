package com.ccbuluo.business.platform.carmanage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 批量更新用（根据code）
 * @author weijb6
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "UpdateCarcoreInfoByVinDTO", description = "批量更新用（根据code）")
public class UpdateCarcoreInfoByVinDTO {
    /**
     * appId
     */
    @ApiModelProperty(name = "appId", value = "appId")
    private String appId;
    /**
     * secretId
     */
    @ApiModelProperty(name = "secretId", value = "secretId")
    private String secretId;

    private List<CarcoreInfoByVinDTO> carcoreInfoList;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public List<CarcoreInfoByVinDTO> getCarcoreInfoList() {
        return carcoreInfoList;
    }

    public void setCarcoreInfoList(List<CarcoreInfoByVinDTO> carcoreInfoList) {
        this.carcoreInfoList = carcoreInfoList;
    }
}