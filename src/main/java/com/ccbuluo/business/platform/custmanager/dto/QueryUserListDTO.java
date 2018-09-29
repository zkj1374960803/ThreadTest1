package com.ccbuluo.business.platform.custmanager.dto;

import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-07-18 16:34:02
 */
@ApiModel(value = "QueryUserListDTO", description = "查询用户列表DTO")
public class QueryUserListDTO {
    @ApiModelProperty(name = "name", value = "用户姓名")
    private String name;
    @ApiModelProperty(name = "userStatus", value = "用户状态1在职0离职")
    private String userStatus;
    @ApiModelProperty(name = "vinNumber", value = "绑定维修车（vinNumber）")
    private String vinNumber;
    @ApiModelProperty(name = "officePhone", value = "办公电话")
    private String officePhone;
    @ApiModelProperty(name = "userUuid", value = "用户uuid")
    private String useruuid;
    @ApiModelProperty(name = "carsNumber", value = "管理车辆数")
    private Long carsNumber;
    @ApiModelProperty(name = "materialsNumber", value = "物料领取数量")
    private Long materialsNumber;
    @ApiModelProperty(name = "vinId", value = "维修车id")
    private Long vinId;
    @ApiModelProperty(name = "receivingAddress", value = "收货地址")
    private String receivingAddress;
    @ApiModelProperty(name = "queryPendingMaterialsDTO", value = "物料领取的列表")
    private List<QueryPendingMaterialsDTO> queryPendingMaterialsDTO;

    public List<QueryPendingMaterialsDTO> getQueryPendingMaterialsDTO() {
        return queryPendingMaterialsDTO;
    }

    public void setQueryPendingMaterialsDTO(List<QueryPendingMaterialsDTO> queryPendingMaterialsDTO) {
        this.queryPendingMaterialsDTO = queryPendingMaterialsDTO;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public Long getVinId() {
        return vinId;
    }

    public void setVinId(Long vinId) {
        this.vinId = vinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public Long getCarsNumber() {
        return carsNumber;
    }

    public void setCarsNumber(Long carsNumber) {
        this.carsNumber = carsNumber;
    }

    public Long getMaterialsNumber() {
        return materialsNumber;
    }

    public void setMaterialsNumber(Long materialsNumber) {
        this.materialsNumber = materialsNumber;
    }
}
