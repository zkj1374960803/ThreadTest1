package com.ccbuluo.business.platform.custmanager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客户经理详情DTO
 * @author zhangkangjian
 * @date 2018-07-18 16:34:02
 */
@ApiModel(value = "CustManagerDetailDTO", description = "客户经理详情DTO")
public class CustManagerDetailDTO {
    @ApiModelProperty(name = "name", value = "用户姓名")
    private String name;
    @ApiModelProperty(name = "userStatus", value = "用户状态1在职0离职")
    private String userStatus;
    @ApiModelProperty(name = "officePhone", value = "办公电话")
    private String officePhone;
    @ApiModelProperty(name = "receivingAddress", value = "收货地址")
    private String receivingAddress;
    @ApiModelProperty(name = "userUuid", value = "用户uuid")
    private String useruuid;
    @ApiModelProperty(name = "englishName", value = "英文名")
    private String englishName;
    @ApiModelProperty(name = "gender", value = "性别")
    private String gender;
    @ApiModelProperty(name = "age", value = "年龄")
    private Integer age;
    private String ethnicCode;
    @ApiModelProperty(name = "ethnic", value = "民族")
    private String ethnic;
    @ApiModelProperty(name = "birthday", value = "出生日期")
    private long birthday;
    @ApiModelProperty(name = "certificateType", value = "证件类型")
    private String certificateType;
    @ApiModelProperty(name = "certificateNo", value = "证件号码")
    private String certificateNo;
    @ApiModelProperty(name = "telephone", value = "联系电话")
    private String telephone;
    @ApiModelProperty(name = "mail", value = "电子邮箱")
    private String mail;
    @ApiModelProperty(name = "province", value = "省")
    private String province;
    private String provinceCode;
    @ApiModelProperty(name = "city", value = "市")
    private String city;
    private String cityCode;
    @ApiModelProperty(name = "area", value = "区")
    private String area;
    private String areaCode;
    @ApiModelProperty(name = "address", value = "详细地址")
    private String address;
    @ApiModelProperty(name = "maritalStatus", value = "婚姻状况0未婚1已婚2离异")
    private String maritalStatus;
    @ApiModelProperty(name = "education", value = "学历")
    private String education;
    @ApiModelProperty(name = "orgName", value = "部门")
    private String orgName;
    @ApiModelProperty(name = "orgCode", value = "部门code")
    private String orgCode;
    @ApiModelProperty(name = "workplaceName", value = "职场")
    private String workplaceName;
    @ApiModelProperty(name = "position", value = "职位")
    private String position;
    @ApiModelProperty(name = "userType", value = "用户类型")
    private String userType;
    @ApiModelProperty(name = "roleName", value = "角色")
    private String roleName;
    @ApiModelProperty(name = "hiredate", value = "入职时间")
    private long hiredate;
    @ApiModelProperty(name = "dimissionTime", value = "离职时间")
    private long dimissionTime;
    @ApiModelProperty(name = "dimissionReson", value = "离职原因")
    private String dimissionReson;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
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

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEthnicCode() {
        return ethnicCode;
    }

    public void setEthnicCode(String ethnicCode) {
        this.ethnicCode = ethnicCode;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getHiredate() {
        return hiredate;
    }

    public void setHiredate(long hiredate) {
        this.hiredate = hiredate;
    }

    public long getDimissionTime() {
        return dimissionTime;
    }

    public void setDimissionTime(long dimissionTime) {
        this.dimissionTime = dimissionTime;
    }

    public String getDimissionReson() {
        return dimissionReson;
    }

    public void setDimissionReson(String dimissionReson) {
        this.dimissionReson = dimissionReson;
    }
}
