package com.ccbuluo.business.entity;

import com.ccbuluo.core.annotation.validate.ValidateMin;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.junit.After;

import java.util.Date;

/**
 * 盘库详单实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "盘库详单实体", description = "盘库详单实体")
public class BizStockAdjustdetail extends AftersaleCommonEntity {
    /**
     * 所属盘库单的编号
     */
    @ApiModelProperty(name = "adjustDocno", value = "所属盘库单的编号")
    private String adjustDocno;
    /**
     * 商品的编号
     */
    @ValidateNotBlank(message = "商品编号不能为空")
    @ApiModelProperty(name = "productNo", value = "商品的编号")
    private String productNo;
    /**
     * 商品类型：零配件、物料
     */
    @ValidateNotBlank(message = "商品类型不能为空")
    @ApiModelProperty(name = "productType", value = "商品类型：零配件、物料")
    private String productType;
    /**
     * 商品的名称
     */
    @ValidateNotBlank(message = "商品名称不能为空")
    @ApiModelProperty(name = "productName", value = "商品的名称")
    private String productName;
    /**
     * 商品分类的全路径名称
     */
    @ValidateNotBlank(message = "商品分类的全路径名称不能为空")
    @ApiModelProperty(name = "productCategoryname", value = "商品分类的全路径名称")
    private String productCategoryname;
    /**
     * 完美情况下该商品的总库存量
     */
    @ValidateMin(value = 0)
    @ValidateNotNull(message = "商品应有库存不能为空")
    @ApiModelProperty(name = "perfectNum", value = "完美情况下该商品的总库存量")
    private Long perfectNum;
    /**
     * 该商品实际情况下的总库存量
     */
    @ValidateMin(value = 0)
    @ValidateNotNull(message = "商品实际库存不能为空")
    @ApiModelProperty(name = "actualNum", value = "该商品实际情况下的总库存量")
    private Long actualNum;
    /**
     * 调整的库存表记录的id和数量json，多个逗号隔开
     */
    @ApiModelProperty(name = "affectStockid", value = "调整的库存表记录的id和数量json，多个逗号隔开")
    private String affectStockid;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setAdjustDocno(String adjustDocno) {
        this.adjustDocno = adjustDocno;
    }

    public String getAdjustDocno() {
        return this.adjustDocno;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return this.productNo;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductCategoryname(String productCategoryname) {
        this.productCategoryname = productCategoryname;
    }

    public String getProductCategoryname() {
        return this.productCategoryname;
    }

    public void setPerfectNum(Long perfectNum) {
        this.perfectNum = perfectNum;
    }

    public Long getPerfectNum() {
        return this.perfectNum;
    }

    public void setActualNum(Long actualNum) {
        this.actualNum = actualNum;
    }

    public Long getActualNum() {
        return this.actualNum;
    }

    public void setAffectStockid(String affectStockid) {
        this.affectStockid = affectStockid;
    }

    public String getAffectStockid() {
        return this.affectStockid;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


}