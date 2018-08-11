package com.ccbuluo.business.platform.allocateapply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 处理申请单
 * @author zhangkangjian
 * @date 2018-08-10 11:32:45
 */
@ApiModel(value = "ProcessApplyDTO", description = "处理申请单")
public class ProcessApplyDTO {

    /**
     * 调拨申请单编号
     */
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号")
    private String applyNo;
    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)")
    private String processType;
    /**
     * 出库的组织架构的类型
     */
    @ApiModelProperty(name = "outstockOrgType", value = "出库的组织架构的类型", hidden = true)
    private String outstockOrgType;

    /**
     * 出库的组织架构
     */
    @ApiModelProperty(name = "outstockOrgno", value = "出库的组织架构(采购类型时，不必填)")
    private String outstockOrgno;

    /**
     * 申请详单列表
     */
    @ApiModelProperty(name = "processApplyDetailDTO", value = "申请详单列表")
    private List<ProcessApplyDetailDTO> processApplyDetailDTO;
    @ApiModelProperty(name = "versionNo", value = "版本号", hidden = true)
    private Long versionNo;
    @ApiModelProperty(name = "applyStatus", value = "申请单状态", hidden = true)
    private String applyStatus;

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public String getOutstockOrgType() {
        return outstockOrgType;
    }

    public void setOutstockOrgType(String outstockOrgType) {
        this.outstockOrgType = outstockOrgType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getOutstockOrgno() {
        return outstockOrgno;
    }

    public void setOutstockOrgno(String outstockOrgno) {
        this.outstockOrgno = outstockOrgno;
    }

    public List<ProcessApplyDetailDTO> getProcessApplyDetailDTO() {
        return processApplyDetailDTO;
    }

    public void setProcessApplyDetailDTO(List<ProcessApplyDetailDTO> processApplyDetailDTO) {
        this.processApplyDetailDTO = processApplyDetailDTO;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }
}
