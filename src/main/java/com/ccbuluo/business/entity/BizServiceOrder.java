package com.ccbuluo.business.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后系统的服务单实体
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@ApiModel(value = "售后系统的服务单实体", description = "售后系统的服务单")
public class BizServiceOrder extends AftersaleCommonEntity{
    /**
     * 服务单的编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "服务单的编号")
    private String serviceOrderno;
    /**
     * 车牌号
     */
    @ApiModelProperty(name = "carNo", value = "车牌号")
    private String carNo;
    /**
     * 车辆的VIN码
     */
    @ApiModelProperty(name = "carVin", value = "车辆的VIN码")
    private String carVin;
    /**
     * 服务类型：维修(REPAIR)、保养(MAINTAIN)、维修加保养(BOTH)
     */
    @ApiModelProperty(name = "serviceType", value = "服务类型：维修(REPAIR)、保养(MAINTAIN)、维修加保养(BOTH)")
    private String serviceType;
    /**
     * 报修人所在机构的编号
     */
    @ApiModelProperty(name = "reportOrgno", value = "报修人所在机构的编号")
    private String reportOrgno;
    /**
     * 报修机构的类型：租赁门店(RENTALSTORE)、客户经理(CUSTMANAGER)
     */
    @ApiModelProperty(name = "reportOrgtype", value = "报修机构的类型：租赁门店(RENTALSTORE)、客户经理(CUSTMANAGER)")
    private String reportOrgtype;
    /**
     * 报修时间
     */
    @ApiModelProperty(name = "reportTime", value = "报修时间")
    private Date reportTime;
    /**
     * 车辆此时的客户的姓名
     */
    @ApiModelProperty(name = "customerName", value = "车辆此时的客户的姓名")
    private String customerName;
    /**
     * 客户的联系方式
     */
    @ApiModelProperty(name = "customerPhone", value = "客户的联系方式")
    private String customerPhone;
    /**
     * 客户方机构编号
     */
    @ApiModelProperty(name = "customerOrgno", value = "客户方机构编号")
    private String customerOrgno;
    /**
     * 备用联系人的姓名
     */
    @ApiModelProperty(name = "reserveContacter", value = "备用联系人的姓名")
    private String reserveContacter;
    /**
     * 备用联系人的联系方式
     */
    @ApiModelProperty(name = "reservePhone", value = "备用联系人的联系方式")
    private String reservePhone;
    /**
     * 服务单的状态
     */
    @ApiModelProperty(name = "orderStatus", value = "服务单的状态")
    private String orderStatus;
    /**
     * 该服务任务被转发的次数，新任务值为1
     */
    @ApiModelProperty(name = "dispatchTimes", value = "该服务任务被转发的次数，新任务值为1")
    private Long dispatchTimes;
    /**
     * 当前负责该任务的员工的uuid
     */
    @ApiModelProperty(name = "curProcessor", value = "当前负责该任务的员工的uuid")
    private String curProcessor;
    /**
     * 当前负责人所在机构的编号
     */
    @ApiModelProperty(name = "processorOrgno", value = "当前负责人所在机构的编号")
    private String processorOrgno;
    /**
     * 负责人所在的机构类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)
     */
    @ApiModelProperty(name = "processorOrgtype", value = "负责人所在的机构类型：服务中心(SERVICECENTER)、客户经理(CUSTMANAGER)")
    private String processorOrgtype;
    /**
     * 服务单的负责人和客户约好的上门维修时间
     */
    @ApiModelProperty(name = "serviceTime", value = "服务单的负责人和客户约好的上门维修时间")
    private Date serviceTime;
    /**
     * 该服务订单需要用户支付的金额
     */
    @ApiModelProperty(name = "orderCost", value = "该服务订单需要用户支付的金额")
    private BigDecimal orderCost;
    /**
     * 该订单是否已完成支付
     */
    @ApiModelProperty(name = "payed", value = "该订单是否已完成支付")
    private Integer payed;
    /**
     * 维修或保养的具体内容描述
     */
    @ApiModelProperty(name = "problemContent", value = "维修或保养的具体内容描述")
    private String problemContent;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 服务类型枚举
     */
    public enum ServiceTypeEnum{
        REPAIR("维修"),
        MAINTAIN("保养"),
        BOTH("维修加保养");

        private String label;

        ServiceTypeEnum(String label) {
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }
    /**
     * 报修机构的类型枚举
     */
    public enum ReportOrgtypeEnum{
        RENTALSTORE("租赁门店"),
        CUSTMANAGER("客户经理");

        private String label;

        ReportOrgtypeEnum(String label) {
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }
    /**
     * 机构类型枚举
     */
    public enum ProcessorOrgtypeEnum{
        SERVICECENTER("服务中心"),
        CUSTMANAGER("客户经理"),
        STORE("门店");

        private String label;

        ProcessorOrgtypeEnum(String label) {
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }

    /**
     * 订单状态枚举
     */
    public enum OrderStatusEnum {
        DRAFT("草稿"),
        WAITING_RECEIVE("待接收"),
        WAITING_PERFECTION("待完善"),
        PROCESSING("处理中"),
        WAITING_CHECKING("待验收"),
        WAITING_PAYMENT("待付款"),
        COMPLETED("已完成"),
        CANCELED("已取消");

        private String label;

        OrderStatusEnum(String label) {
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
    }

    public String getServiceOrderno() {
        return this.serviceOrderno;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarNo() {
        return this.carNo;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getCarVin() {
        return this.carVin;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setReportOrgno(String reportOrgno) {
        this.reportOrgno = reportOrgno;
    }

    public String getReportOrgno() {
        return this.reportOrgno;
    }

    public void setReportOrgtype(String reportOrgtype) {
        this.reportOrgtype = reportOrgtype;
    }

    public String getReportOrgtype() {
        return this.reportOrgtype;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getReportTime() {
        return this.reportTime;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() {
        return this.customerPhone;
    }

    public void setReserveContacter(String reserveContacter) {
        this.reserveContacter = reserveContacter;
    }

    public String getReserveContacter() {
        return this.reserveContacter;
    }

    public void setReservePhone(String reservePhone) {
        this.reservePhone = reservePhone;
    }

    public String getReservePhone() {
        return this.reservePhone;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setDispatchTimes(Long dispatchTimes) {
        this.dispatchTimes = dispatchTimes;
    }

    public Long getDispatchTimes() {
        return this.dispatchTimes;
    }

    public void setCurProcessor(String curProcessor) {
        this.curProcessor = curProcessor;
    }

    public String getCurProcessor() {
        return this.curProcessor;
    }

    public void setProcessorOrgtype(String processorOrgtype) {
        this.processorOrgtype = processorOrgtype;
    }

    public String getProcessorOrgtype() {
        return this.processorOrgtype;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Date getServiceTime() {
        return this.serviceTime;
    }

    public void setOrderCost(BigDecimal orderCost) {
        this.orderCost = orderCost;
    }

    public BigDecimal getOrderCost() {
        return this.orderCost;
    }

    public void setPayed(Integer payed) {
        this.payed = payed;
    }

    public Integer getPayed() {
        return this.payed;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getProblemContent() {
        return this.problemContent;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getCustomerOrgno() {
        return customerOrgno;
    }

    public void setCustomerOrgno(String customerOrgno) {
        this.customerOrgno = customerOrgno;
    }

    public String getProcessorOrgno() {
        return processorOrgno;
    }

    public void setProcessorOrgno(String processorOrgno) {
        this.processorOrgno = processorOrgno;
    }
}