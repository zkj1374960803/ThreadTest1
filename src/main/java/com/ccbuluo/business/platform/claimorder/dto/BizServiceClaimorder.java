
package com.ccbuluo.business.platform.claimorder.dto;

import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-09-08 11:04:42
 */
@ApiModel(value = "BizServiceClaimorder", description = "索赔单实体")
public class BizServiceClaimorder {

    @ApiModelProperty(name = "id", value = "索赔单id")
    private Long id;

    // 索赔单的编号
    @ApiModelProperty(name = "claimOrdno", value = "索赔单的编号")
    private String claimOrdno;

    // 维修单的编号
    @ApiModelProperty(name = "serviceOrdno", value = "维修单的编号")
    private String serviceOrdno;

    // 将三包件发给平台时的物流单号
    @ApiModelProperty(name = "trackingNo", value = "将三包件发给平台时的物流单号")
    private String trackingNo;

    // 将三包件推给平台的地址
    @ApiModelProperty(name = "refundAdress", value = "将三包件推给平台的地址")
    private String refundAdress;

    // 索赔单的状态
    @ApiModelProperty(name = "docStatus", value = "索赔单的状态")
    private String docStatus;

    // 索赔机构的编号
    @ApiModelProperty(name = "claimOrgno", value = "索赔机构的编号")
    private String claimOrgno;

    // 索赔机构的名称
    @ApiModelProperty(name = "claimOrgname", value = "索赔机构的名称")
    private String claimOrgname;

    // 索赔的金额
    @ApiModelProperty(name = "claimAmount", value = "索赔的金额")
    private Double claimAmount = 0D;

    // 实际赔偿的金额
    @ApiModelProperty(name = "actualAmount", value = "实际赔偿的金额")
    private Double actualAmount = 0D;

    // 赔偿时间
    @ApiModelProperty(name = "repayTime", value = "赔偿时间")
    private Date repayTime;

    // 创建人
    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;

    // 创建时间
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;

    // 更新人
    @ApiModelProperty(name = "operator", value = "更新人")
    private String operator;

    // 更新时间
    @ApiModelProperty(name = "operateTime", value = "更新时间")
    private Long operateTime;

    // 删除标识
    @ApiModelProperty(name = "deleteFlag", value = "删除标识")
    private Integer deleteFlag;

    // 备注
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    // 处理索赔的机构的编号
    @ApiModelProperty(name = "processOrgno", value = "处理索赔的机构的编号")
    private String processOrgno;

    // 处理索赔的机构的名称
    @ApiModelProperty(name = "processOrgname", value = "处理索赔的机构的名称")
    private String processOrgname;

    // 验收时间
    @ApiModelProperty(name = "processTime", value = "验收时间")
    private Long processTime;

    // 车牌号
    @ApiModelProperty(name = "carNo", value = "车牌号")
    private String carNo;

    // 零配件详情
    @ApiModelProperty(name = "fittingDetail", value = "零配件详情")
    List<ProductDetailDTO> fittingDetail;

    // 工时详情
    @ApiModelProperty(name = "maintainitemDetail", value = "工时详情")
    List<ProductDetailDTO> maintainitemDetail;

    public List<ProductDetailDTO> getFittingDetail() {
        return fittingDetail;
    }

    public void setFittingDetail(List<ProductDetailDTO> fittingDetail) {
        this.fittingDetail = fittingDetail;
    }

    public List<ProductDetailDTO> getMaintainitemDetail() {
        return maintainitemDetail;
    }

    public void setMaintainitemDetail(List<ProductDetailDTO> maintainitemDetail) {
        this.maintainitemDetail = maintainitemDetail;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getProcessOrgno() {
        return processOrgno;
    }

    public void setProcessOrgno(String processOrgno) {
        this.processOrgno = processOrgno;
    }

    public String getProcessOrgname() {
        return processOrgname;
    }

    public void setProcessOrgname(String processOrgname) {
        this.processOrgname = processOrgname;
    }

    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimOrdno() {
        return this.claimOrdno;
    }

    public void setClaimOrdno(String claimOrdno) {
        this.claimOrdno = claimOrdno;
    }

    public String getServiceOrdno() {
        return this.serviceOrdno;
    }


    public void setServiceOrdno(String serviceOrdno) {
        this.serviceOrdno = serviceOrdno;
    }

    public String getTrackingNo() {
        return this.trackingNo;
    }


    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getRefundAdress() {
        return this.refundAdress;
    }

    public void setRefundAdress(String refundAdress) {
        this.refundAdress = refundAdress;
    }

    public String getDocStatus() {
        return this.docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getClaimOrgno() {
        return this.claimOrgno;
    }

    public void setClaimOrgno(String claimOrgno) {
        this.claimOrgno = claimOrgno;
    }

    public String getClaimOrgname() {
        return this.claimOrgname;
    }

    public void setClaimOrgname(String claimOrgname) {
        this.claimOrgname = claimOrgname;
    }

    public Double getClaimAmount() {
        return this.claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Double getActualAmount() {
        return this.actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getRepayTime() {
        return this.repayTime;
    }

    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getOperateTime() {
        return this.operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum DocStatusEnum {
        PENDINGSUBMISSION("待提交"), WAITACCEPTANCE("待验收"),
        PENDINGPAYMENT("待付款"), COMPLETED("已完成");
        private String label;

        DocStatusEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

}