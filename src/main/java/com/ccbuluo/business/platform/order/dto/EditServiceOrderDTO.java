package com.ccbuluo.business.platform.order.dto;

import com.ccbuluo.business.entity.AftersaleCommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 描述 编辑服务订单DTO
 * @author baoweiding
 * @date 2018-09-03 18:47:55
 * @version V1.0.0
 */
@ApiModel(value = "EditServiceOrderDTO", description = "编辑服务订单DTO")
public class EditServiceOrderDTO extends AftersaleCommonEntity {
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "serviceOrderno", value = "订单编号")
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
     * 服务单的负责人和客户约好的上门维修时间
     */
    @ApiModelProperty(name = "serviceTime", value = "服务单的负责人和客户约好的上门维修时间")
    private Date serviceTime;
    /**
     * 维修或保养的具体内容描述
     */
    @ApiModelProperty(name = "problemContent", value = "维修或保养的具体内容描述")
    private String problemContent;
    /**
     * 省code
     */
    @ApiModelProperty(name = "provinceCode", value = "省code")
    private String provinceCode;
    /**
     * 省
     */
    @ApiModelProperty(name = "provinceName", value = "省")
    private String provinceName;
    /**
     * 市code
     */
    @ApiModelProperty(name = "cityCode", value = "市code")
    private String cityCode;
    /**
     * 市
     */
    @ApiModelProperty(name = "cityName", value = "市")
    private String cityName;
    /**
     * 区code
     */
    @ApiModelProperty(name = "areaCode", value = "区code")
    private String areaCode;
    /**
     * 区
     */
    @ApiModelProperty(name = "areaName", value = "区")
    private String areaName;
    /**
     * 具体地址
     */
    @ApiModelProperty(name = "detailAddress", value = "具体地址")
    private String detailAddress;

    public String getServiceOrderno() {
        return serviceOrderno;
    }

    public void setServiceOrderno(String serviceOrderno) {
        this.serviceOrderno = serviceOrderno;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getReportOrgtype() {
        return reportOrgtype;
    }

    public void setReportOrgtype(String reportOrgtype) {
        this.reportOrgtype = reportOrgtype;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getReserveContacter() {
        return reserveContacter;
    }

    public void setReserveContacter(String reserveContacter) {
        this.reserveContacter = reserveContacter;
    }

    public String getReservePhone() {
        return reservePhone;
    }

    public void setReservePhone(String reservePhone) {
        this.reservePhone = reservePhone;
    }

    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
