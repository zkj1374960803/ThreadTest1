package com.ccbuluo.business.platform.carconfiguration.rest;

import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelConfigurationService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车型具体配置controller
 * @author chaoshuai
 * @date 2018-05-08 10:53:48
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/carconfiguration/basiccarmodelconfiguration")
@Api(tags = "车型具体配置")
public class BasicCarmodelConfigurationController extends BaseController {
    @Autowired
    private BasicCarmodelConfigurationService basicCarmodelConfigurationService;

    /**
     * 根据车型id查询车型配置详情
     * @param id 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 18:21:10
     */
    @ApiOperation(value = "根据车型id查询车型配置详情", notes = "【chaoshuai】")
    @ApiImplicitParam(name = "id", value = "车型id", required = false, paramType = "query")
    @GetMapping("/queryconfigbycarmodelid/{id}")
    public StatusDto queryConfigByCarModelId(@PathVariable("id") Long id){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelConfigurationService.queryCarModelConfigurationByCarModelId(id));
    }


    /**
     * 根据车型id和参数名称查询车型参数
     * @param carmodelId 车型id
     * @param parameterName 参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 15:50:15
     */
    @ApiOperation(value = "根据车型id和参数名称查询车型参数", notes = "【chaoshuai】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carmodelId", value = "车型id", required = true, paramType = "query"),
                        @ApiImplicitParam(name = "parameterName", value = "参数名称", required = true, paramType = "query")})
    @GetMapping("/getbycarmodelidandname")
    public StatusDto getByCarModelIdAndName(Long carmodelId, String parameterName){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelConfigurationService.getByCarModelIdAndName(carmodelId, parameterName));
    }
}
