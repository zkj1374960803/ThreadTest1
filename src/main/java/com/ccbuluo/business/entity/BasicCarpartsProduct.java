package com.ccbuluo.business.entity;

import com.ccbuluo.excel.imports.ExcelField;
import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


/**
 * 车辆的配件商品表实体
 * @author  
 * @date 2018-07-05 17:02:53
 * @version V 2.0.0
 */
@ApiModel(value = "车辆的配件商品表实体", description = "车辆的配件商品表")
public class BasicCarpartsProduct {
    /**
     * 
     */
    @ApiModelProperty(name = "id", value = "")
    private Long id;

    /**
     * 零配件代码
     */
    @ExcelField(colIndex = 0)
    @ApiModelProperty(name = "carpartsMarkno", value = "零配件代码", required = true)
    private String carpartsMarkno;
    /**
     * 名称
     */
    @ExcelField(colIndex = 1)
    @ApiModelProperty(name = "carpartsName", value = "名称")
    private String carpartsName;
    /**
     * 零配件计量单位
     */
    @ExcelField(colIndex = 2)
    @ApiModelProperty(name = "carpartsUnit", value = "零配件计量单位", required = true)
    private String carpartsUnit;
    /**
     * 单车用量
     */
    @ExcelField(colIndex = 3)
    @ApiModelProperty(name = "usedAmount", value = "单车使用配件的个数", required = true)
    private Long usedAmount;
    /**
     * 图片
     */
    @ExcelField(colIndex = 4)
    @ApiModelProperty(name = "carpartsImage", value = "零配件图片在服务端的相对路径", required = true)
    private String carpartsImage;
    /**
     * 适用车型
     */
    @ExcelField(colIndex = 5)
    @ApiModelProperty(name = "carmodelName", value = "车型名称")
    private String carmodelName;
    /**
     * 服务中心价格
     */
    @ExcelField(colIndex = 6)
    @ApiModelProperty(name = "servercenterPrice", value = "服务中心价格")
    private BigDecimal servercenterPrice;
    /**
     * 客户经理价格
     */
    @ExcelField(colIndex = 7)
    @ApiModelProperty(name = "custmanagerPrice", value = "客户经理价格")
    private BigDecimal custmanagerPrice;
    /**
     * 用户销售价格
     */
    @ExcelField(colIndex = 8)
    @ApiModelProperty(name = "sellPrice", value = "用户销售价格")
    private BigDecimal sellPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarpartsMarkno() {
        return carpartsMarkno;
    }

    public void setCarpartsMarkno(String carpartsMarkno) {
        this.carpartsMarkno = carpartsMarkno;
    }

    public String getCarpartsName() {
        return carpartsName;
    }

    public void setCarpartsName(String carpartsName) {
        this.carpartsName = carpartsName;
    }

    public String getCarpartsUnit() {
        return carpartsUnit;
    }

    public void setCarpartsUnit(String carpartsUnit) {
        this.carpartsUnit = carpartsUnit;
    }

    public Long getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public String getCarpartsImage() {
        return carpartsImage;
    }

    public void setCarpartsImage(String carpartsImage) {
        this.carpartsImage = carpartsImage;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public BigDecimal getServercenterPrice() {
        return servercenterPrice;
    }

    public void setServercenterPrice(BigDecimal servercenterPrice) {
        this.servercenterPrice = servercenterPrice;
    }

    public BigDecimal getCustmanagerPrice() {
        return custmanagerPrice;
    }

    public void setCustmanagerPrice(BigDecimal custmanagerPrice) {
        this.custmanagerPrice = custmanagerPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}