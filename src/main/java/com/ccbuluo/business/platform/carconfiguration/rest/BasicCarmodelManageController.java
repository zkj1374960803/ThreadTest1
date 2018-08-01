package com.ccbuluo.business.platform.carconfiguration.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.dao.CarmodelManageDTO;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 车型管理controller
 * @author chaoshuai
 * @date 2018-05-08 10:53:22
 * @version v 1.0.0
 */
@Api(tags = "车型相关接口")
@RestController
@RequestMapping("/carconfiguration/basiccarmodelmanage")
public class BasicCarmodelManageController extends BaseController {
    @Autowired
    private BasicCarmodelManageService basicCarmodelManageService;

    /**
     * 分页查询车型列表
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param status 状态
     * @param offset
     * @param limit
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 19:37:29
     */
    @ApiOperation(value = "分页查询车型列表", notes = "【chaoshuai】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandId", value = "品牌id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carseriesId", value = "车系id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carmodelName", value = "车型名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "当前页数", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query")})
    @GetMapping("/list")
    public StatusDto list(Long carbrandId,
                          Long carseriesId,
                          Integer status,
                          String carmodelName,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "10") int limit){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelManageService.queryPageForCarModelManage(carbrandId, carseriesId, status,carmodelName, offset, limit));
    }

    /**
     * 查询所有的车型
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 11:20:13
     */
    @ApiOperation(value = "查询所有的车型", notes = "【chaoshuai】")
    @GetMapping("/queryallmodel")
    public StatusDto queryAllModel(){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelManageService.queryAllModel());
    }

    /**
     * 根据车系id查询车型
     * @param id 车系id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-10 09:37:13
     */
    @ApiOperation(value = "根据车系id查询车型", notes = "【chaoshuai】")
    @ApiImplicitParam(name = "id", value = "车系id", required = true, paramType = "path")
    @GetMapping("/getbycarseriesid/{id}")
    public StatusDto getByCarSeriesId(@PathVariable("id") Long id){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelManageService.getByCarSeriesId(id));
    }
    /**
     * 新增车型
     * @param carmodelManageDTO 车型扩展实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    @ApiOperation(value = "新增车型", notes = "【chaoshuai】")
    @PostMapping("/create")
    public StatusDto create(@RequestBody CarmodelManageDTO carmodelManageDTO){
        StatusDto statusDto = this.basicCarmodelManageService.createCarModel(carmodelManageDTO);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }

    /**
     * 编辑车型
     * @param carmodelManageDTO 车型扩展实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    @ApiOperation(value = "编辑车型", notes = "【chaoshuai】")
    @PostMapping("/edit")
    public StatusDto edit(@RequestBody CarmodelManageDTO carmodelManageDTO){
        StatusDto statusDto = this.basicCarmodelManageService.editCarModel(carmodelManageDTO);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }

    /**
     * 车型停用启用
     * @param id 车型id
     * @param status 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:19:31
     */
    @ApiOperation(value = "车型停用启用", notes = "【chaoshuai】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "车型id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "status", value = "车型id", required = true, paramType = "query")
    })
    @PostMapping("/updatestatus")
    public StatusDto updateStatus(Long id, int status){
        StatusDto statusDto = this.basicCarmodelManageService.stopOperationCarModel(id, status);
        return StatusDto.buildSuccessStatusDto(statusDto.getMessage());
    }

    /**
     * 根据车型id 获取车型名称 品牌logo
     * @param carmodelId
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author lizhao
     * @date 2018-05-29 15:31:17
     */
    @ApiOperation(value = "根据车型id 获取车型名称，车辆logo", notes = "【李照】")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carmodelId", value = "车型id", dataType = "Long", required = true, paramType = "query"),
    })
    @GetMapping("/getcarmodelandlogo")
    public StatusDto getCarmodelAndLogo(@RequestParam(name = "carmodelId") Long carmodelId){
        return StatusDto.buildDataSuccessStatusDto(basicCarmodelManageService.getCarmodelAndLogo(carmodelId));
    }

    /**
     * 获取全部车型 下拉用
     * @return com.ccbuluo.http.StatusDto
     * @author Ryze
     * @date 2018-06-12 15:31:17
     */
    @ApiOperation(value = "获取全部车型 下拉用", notes = "【李福田】 参数说明: id->车型id name->车型名称")
    @GetMapping("/queryall")
    public StatusDto<List<Map<String, Object>>> queryall() {
        return StatusDto.buildDataSuccessStatusDto( this.basicCarmodelManageService.queryAll());
    }

    /**
     * 删除车型
     * @param id 车型id
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-01 09:37:13
     */
    @ApiOperation(value = "删除车型",notes = "【魏俊标】")
    @GetMapping("/delete")
    @ApiImplicitParam(name = "id", value = "车型id", required = true, paramType = "query")
    public StatusDto delete(@RequestParam Long id) {
        int flag = basicCarmodelManageService.deleteCarmodelManageById(id);
        if (flag == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("删除成功！");
        }
        return StatusDto.buildFailureStatusDto("删除失败！");
    }

}
