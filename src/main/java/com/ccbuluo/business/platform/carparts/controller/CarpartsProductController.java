package com.ccbuluo.business.platform.carparts.controller;

import com.ccbuluo.business.platform.carparts.service.CarpartsProductService;
import com.ccbuluo.business.platform.constants.Constants;
import com.ccbuluo.business.platform.parameter.dto.SaveCarpartsParameterDTO;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccbuluo.business.platform.carparts.dto.SaveBasicCarpartsProductDTO;

/**
 * 零配件
 * @author wuyibo
 * @date 2018-06-29 14:18:58
 */
@Api(tags = "车辆售后服务(平台端)")
@RestController
@RequestMapping("/sfterSales/parameter")
public class CarpartsProductController {

    @Autowired
    CarpartsProductService carpartsProductService;

    /**
     * 添加零配件
     * @param saveBasicCarpartsProductDTO 零配件实体dto
     * @return 是否保存成功
     * @author weijb
     * @date 2018-07-2 08:59:35
     */
    @ApiOperation(value = "添加零配件",notes = "【魏俊标】")
    @PostMapping("/save")
    public StatusDto saveParameter(@ApiParam(name = "SaveParameterDTO对象", value = "传入json格式", required = true)@RequestBody SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO) {
        int status = carpartsProductService.saveParameter(saveBasicCarpartsProductDTO);
        if (Constants.YES == status) {
            return StatusDto.buildSuccessStatusDto("保存成功！");
        } else if (Constants.PLATNUMBER_CHECK == status) {
            return StatusDto.buildFailureStatusDto("车牌号和车辆id不匹配！");
        }
        return StatusDto.buildFailureStatusDto("保存失败！");
    }
}
