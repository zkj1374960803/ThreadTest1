package com.ccbuluo.business.platform.carconfiguration.rest;

import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelParameter;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelParameterService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 车型参数配置controller
 * @author chaoshuai
 * @date 2018-05-08 10:52:54
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/carconfiguration/basiccarmodelparameter")
@Api(tags = "车型参数配置")
public class BasicCarmodelParameterController extends BaseController {
    @Autowired
    private BasicCarmodelParameterService basicCarmodelParameterService;

    /**
     * 分页查询所有配置参数
     * @param parameterName 配置参数名称
     * @param offset 偏移量
     * @param limit 步长
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:58:58
     */
    @ApiOperation(value = "分页查询所有配置参数", notes = "【chaoshuai】")
    @ApiImplicitParams({@ApiImplicitParam(name = "parameterName", value = "配置参数名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "当前页数", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query")})
    @GetMapping("/list")
    public StatusDto list(String parameterName,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "10") int limit){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelParameterService.queryPageForParameter(parameterName, offset, limit));
    }

    /**
     * 查询所用的配置参数
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 10:12:31
     */
    @ApiOperation(value = "查询所用的配置参数", notes = "【chaoshuai】")
    @GetMapping("/querylist")
    public StatusDto queryList(){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelParameterService.queryAllParameter());
    }

    /**
     * 根据id查询配置参数详情
     * @param id 配置参数id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    @ApiOperation(value = "根据id查询配置参数详情", notes = "【chaoshuai】")
    @ApiImplicitParam(name = "id", value = "配置参数id", required = true, paramType = "path")
    @GetMapping("/queryforparameterbyid/{id}")
    public StatusDto queryForParameterById(@PathVariable("id") Long id){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelParameterService.queryForParameterById(id));
    }

    /**
     * 根据配置参数名称查询配置参数详情
     * @param parameterName 配置参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    @ApiOperation(value = "根据配置参数名称查询配置参数详情", notes = "【chaoshuai】")
    @ApiImplicitParam(name = "parameterName", value = "配置参数名称", required = true, paramType = "query")
    @GetMapping("/getbyparametername")
    public StatusDto getByParameterName(String parameterName){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelParameterService.getByParameterName(parameterName));
    }

    /**
     * 新增车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 15:33:04
     */
    @ApiOperation(value = "新增车型配置参数", notes = "【chaoshuai】")
    @PostMapping("/create")
    public StatusDto create(CarmodelParameter carmodelParameter){
        StatusDto statusDto = this.basicCarmodelParameterService.createParameter(carmodelParameter);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }

    /**
     * 编辑车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 16:32:16
     */
    @ApiOperation(value = "编辑车型配置参数", notes = "【chaoshuai】")
    @PostMapping("/edit")
    public StatusDto edit(CarmodelParameter carmodelParameter){
        StatusDto statusDto = this.basicCarmodelParameterService.editParameter(carmodelParameter);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }

    /**
     * 根据id删除车型参数配置
     * @param id 车型参数配置id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:15:47
     */
    @ApiOperation(value = "根据id删除车型参数配置", notes = "【chaoshuai】")
    @ApiImplicitParam(name = "id", value = "车型参数配置id", required = true, paramType = "path")
    @GetMapping("/delete/{id}")
    public StatusDto delete(@PathVariable("id") Long id){
        StatusDto statusDto = this.basicCarmodelParameterService.deleteParameter(id);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }
}
