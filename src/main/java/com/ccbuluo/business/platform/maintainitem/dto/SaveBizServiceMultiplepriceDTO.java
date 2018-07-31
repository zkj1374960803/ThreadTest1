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
@ApiModel(value = "SaveBizServiceMultiplepriceDTO", description = "服务项目 各地区对基本定价的倍数")
public class SaveBizServiceMultiplepriceDTO {

    /**
     * 城市
     */
    @ApiModelProperty(name = "remark", value = "城市")
    private List<CorrespondAreaDTO> multiplepriceDTOList;

    public List<CorrespondAreaDTO> getMultiplepriceDTOList() {
        return multiplepriceDTOList;
    }

    public void setMultiplepriceDTOList(List<CorrespondAreaDTO> multiplepriceDTOList) {
        this.multiplepriceDTOList = multiplepriceDTOList;
    }
}