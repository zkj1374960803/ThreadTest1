package com.ccbuluo.business.platform.maintainitem.dto;

import com.ccbuluo.business.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 服务项目 各地区对基本定价的倍数实体
 * @author liuduo
 * @date 2018-05-10 11:43:11
 * @version V1.0.0
 */
@ApiModel(value = "服务项目 各地区对基本定价的倍数实体", description = "服务项目 各地区对基本定价的倍数")
public class SaveBizServiceMultiplepriceDTO extends IdEntity {
    /**
     * 服务项目的编码
     */
    @ApiModelProperty(name = "maintainitemCode", value = "服务项目的编码")
    private String maintainitemCode;
    /**
     * 倍数
     */
    @ApiModelProperty(name = "multiple", value = "倍数")
    private Double multiple;
    /**
     * 城市
     */
    @ApiModelProperty(name = "remark", value = "城市")
    private List<CorrespondAreaDTO> multiplepriceDTOList;

    public void setMaintainitemCode(String maintainitemCode) {
        this.maintainitemCode = maintainitemCode;
    }

    public String getMaintainitemCode() {
        return this.maintainitemCode;
    }

    public void setMultiple(Double multiple) {
        this.multiple = multiple;
    }

    public Double getMultiple() {
        return this.multiple;
    }

    public List<CorrespondAreaDTO> getMultiplepriceDTOList() {
        return multiplepriceDTOList;
    }

    public void setMultiplepriceDTOList(List<CorrespondAreaDTO> multiplepriceDTOList) {
        this.multiplepriceDTOList = multiplepriceDTOList;
    }
}