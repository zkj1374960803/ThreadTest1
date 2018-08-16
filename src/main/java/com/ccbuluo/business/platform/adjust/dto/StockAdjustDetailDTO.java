package com.ccbuluo.business.platform.adjust.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 盘库列表展示用dto
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-14 16:54:57
 */
@ApiModel(value = "盘库列表展示用实体", description = "盘库列表展示用实体")
public class StockAdjustDetailDTO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private Long id;
    /**
     * 盘库单号
     */
    @ApiModelProperty(name = "adjustDocno", value = "盘库单号")
    private String adjustDocno;
    /**
     * 盘库时间
     */
    @ApiModelProperty(name = "adjustTime", value = "盘库时间")
    private Date adjustTime;
    /**
     * 盘库单来源（机构名字）
     */
    @ApiModelProperty(name = "adjustDocName", value = "盘库单来源（机构名字）")
    private String adjustDocName;
    /**
     * 盘库单来源code（机构code）
     */
    @ApiModelProperty(name = "adjustOrgno", value = "盘库单来源code（机构code）")
    private String adjustOrgno;
    /**
     * 盘库人姓名
     */
    @ApiModelProperty(name = "adjustUserName", value = "盘库人姓名")
    private String adjustName;
    /**
     * 盘库人uuid
     */
    @ApiModelProperty(name = "adjustUserid", value = "盘库人uuid")
    private String adjustUserid;
    /**
     * 调整库存的原因
     */
    @ApiModelProperty(name = "adjustReson", value = "调整库存的原因")
    private String adjustReson;
    /**
     * 盘库单详情
     */
    @ApiModelProperty(name = "stockAdjustListDTOList", value = "盘库单详情")
    private List<StockAdjustListDTO> stockAdjustListDTOList;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdjustDocno() {
        return adjustDocno;
    }

    public void setAdjustDocno(String adjustDocno) {
        this.adjustDocno = adjustDocno;
    }

    public Date getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }

    public String getAdjustDocName() {
        return adjustDocName;
    }

    public void setAdjustDocName(String adjustDocName) {
        this.adjustDocName = adjustDocName;
    }

    public String getAdjustOrgno() {
        return adjustOrgno;
    }

    public void setAdjustOrgno(String adjustOrgno) {
        this.adjustOrgno = adjustOrgno;
    }

    public String getAdjustName() {
        return adjustName;
    }

    public void setAdjustName(String adjustName) {
        this.adjustName = adjustName;
    }

    public String getAdjustUserid() {
        return adjustUserid;
    }

    public void setAdjustUserid(String adjustUserid) {
        this.adjustUserid = adjustUserid;
    }

    public String getAdjustReson() {
        return adjustReson;
    }

    public void setAdjustReson(String adjustReson) {
        this.adjustReson = adjustReson;
    }

    public List<StockAdjustListDTO> getStockAdjustListDTOList() {
        return stockAdjustListDTOList;
    }

    public void setStockAdjustListDTOList(List<StockAdjustListDTO> stockAdjustListDTOList) {
        this.stockAdjustListDTOList = stockAdjustListDTOList;
    }
}
