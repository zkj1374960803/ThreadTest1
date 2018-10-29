package com.ccbuluo.business.platform.allocateapply.dto;

import com.ccbuluo.business.constants.MyGroup;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 处理申请单
 * @author zhangkangjian
 * @date 2018-08-10 11:32:45
 */
@ApiModel(value = "ProcessApplyDTO", description = "处理申请单")
public class ProcessApplyDTO {

    @ApiModelProperty(name = "applyProcessor", value = "处理人", hidden = true)
    private String applyProcessor;
    @ApiModelProperty(name = "processTime", value = "处理时间", hidden = true)
    private Date processTime;

    @ApiModelProperty(name = "applyType", value = "申请的类型：(注：TRANSFER调拨、PURCHASE采购)", hidden = true)
    private String applyType;
    /**
     * 调拨申请单编号
     */
    @ValidateNotBlank(groups = {MyGroup.Add.class, MyGroup.Edit.class})
    @ApiModelProperty(name = "applyNo", value = "调拨申请单编号")
    private String applyNo;
    /**
     * 平台决定的处理类型：调拨、采购
     */
    @ApiModelProperty(name = "processType", value = "平台决定的处理类型(注：TRANSFER调拨、PURCHASE采购)", hidden = true)
    private String processType;
    /**
     * 出库的组织架构的类型
     */
    @ApiModelProperty(name = "outstockOrgType", value = "出库的组织架构的类型", hidden = true)
    private String outstockOrgType;

    /**
     * 出库的组织架构
     */
    @ValidateNotBlank(groups = MyGroup.Add.class)
    @ApiModelProperty(name = "outstockOrgno", value = "出库的组织架构(采购类型时，不必填)", hidden = true)
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

    @ApiModelProperty(name = "processMemo", value = "驳回理由", hidden = true)
    private String processMemo;

    public String getProcessMemo() {
        return processMemo;
    }

    public void setProcessMemo(String processMemo) {
        this.processMemo = processMemo;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyProcessor() {
        return applyProcessor;
    }

    public void setApplyProcessor(String applyProcessor) {
        this.applyProcessor = applyProcessor;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

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
