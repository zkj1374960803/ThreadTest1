package com.ccbuluo.business.platform.carconfiguration.rest;

import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarseriesManageService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 车系管理controller
 * @author chaoshuai
 * @date 2018-05-08 10:59:16
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/carconfiguration/basiccarseriesmanage")
@Api(tags = "车系")
public class BasicCarseriesManageController extends BaseController {

    @Autowired
    private BasicCarseriesManageService basicCarseriesManageService;

    /**
     * 车系新增
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:53:37
     */
    @ApiOperation(value = "车系新增", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carseriesName", value = "车系名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carbrandId", value = "所属品牌", required = true, paramType = "query"),
        @ApiImplicitParam(name = "sortNumber", value = "排序号", required = true, paramType = "query")})
    @PostMapping("/create")
    public StatusDto create(@ApiIgnore CarseriesManage carseriesManage) {
        return basicCarseriesManageService.saveCarseriesManage(carseriesManage);
    }

    /**
     * 车系编辑
     * @param carseriesManage 车系
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:53:37
     */
    @ApiOperation(value = "车系编辑", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "品牌id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carseriesName", value = "车系名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "sortNumber", value = "排序号", required = true, paramType = "query")})
    @PostMapping("/edit")
    public StatusDto edit(@ApiIgnore CarseriesManage carseriesManage) {
        return basicCarseriesManageService.updateCarseriesManage(carseriesManage);
    }

    /**
     * 车系删除
     * @param id 车系id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:53:37
     */
    @ApiOperation(value = "车系删除", notes = "【wuyibo】")
    @ApiImplicitParam(name = "id", value = "车系id", required = true, paramType = "path")
    @GetMapping("/delete/{id}")
    public StatusDto delete(@PathVariable("id") Long id) {
        return basicCarseriesManageService.deleteCarseriesManage(id);
    }

    /**
     * 车系详情
     * @param id 车系id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 17:37:17
     */
    @ApiOperation(value = "车系详情", notes = "【wuyibo】")
    @ApiImplicitParam(name = "id", value = "车系id", required = true, paramType = "path")
    @GetMapping("/detail/{id}")
    public StatusDto detail(@PathVariable("id") Long id) {
        return StatusDto.buildDataSuccessStatusDto(basicCarseriesManageService.findCarseriesManageDetail(id));
    }

    /**
     * 分页查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @ApiOperation(value = "分页查询品牌下车系列表", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandId", value = "品牌id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carseriesName", value = "车系名称", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "当前页数", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query")})
    @GetMapping("/list")
    public StatusDto list(@RequestParam(required = true) Long carbrandId,
                          @RequestParam(required = false) String carseriesName,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "10") int limit) {
        Page<Map<String, Object>> carseriesManagePage = basicCarseriesManageService.queryCarseriesManagePage(carbrandId, carseriesName, offset, limit);
        return StatusDto.buildDataSuccessStatusDto(carseriesManagePage);
    }

    /**
     * 查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @ApiOperation(value = "查询品牌下车系列表", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandId", value = "品牌id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carseriesName", value = "车系名称", paramType = "query")})
    @GetMapping("/carseriesmanagelist")
    public StatusDto carseriesManageList(@RequestParam(required = true) Long carbrandId,
                                         @RequestParam(required = false) String carseriesName) {
        return StatusDto.buildDataSuccessStatusDto(basicCarseriesManageService.queryCarseriesManageList(carbrandId, carseriesName));
    }

    /**
     * 查询所有车系 下拉框
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @ApiOperation(value = "查询所有车系【下拉框】", notes = "【wuyibo】")
    @GetMapping("/allCarseriesmanagelist")
    public StatusDto allCarseriesmanagelist() {
        return StatusDto.buildDataSuccessStatusDto(basicCarseriesManageService.queryAllCarseriesManageList());
    }

}
