package com.ccbuluo.business.platform.carconfiguration.controller;

import com.ccbuluo.business.platform.carconfiguration.entity.CarbrandManage;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarbrandManageService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.validate.Group;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 品牌管理controller
 * @author chaoshuai
 * @date 2018-05-08 10:58:42
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/carconfiguration/basiccarbrandmanage")
@Api(tags = "品牌")
public class BasicCarbrandManageController extends BaseController {

    @Autowired
    private BasicCarbrandManageService basicCarbrandManageService;

    /**
     * 品牌新增
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:02:30
     */
    @ApiOperation(value = "品牌新增", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandName", value = "品牌名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "sortNumber", value = "排序号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carbrandLogo", value = "品牌logo", paramType = "query")})
    @PostMapping("/create")
    public StatusDto create(@ApiIgnore CarbrandManage carbrandManage) {
        return basicCarbrandManageService.saveCarbrandManage(carbrandManage);
    }

    /**
     * 品牌编辑
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:02:30
     */
    @ApiOperation(value = "品牌编辑", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "品牌id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carbrandName", value = "品牌名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "sortNumber", value = "排序号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "carbrandLogo", value = "品牌logo", paramType = "query")})
    @PostMapping("/edit")
    public StatusDto edit(@ApiIgnore CarbrandManage carbrandManage) {
        return basicCarbrandManageService.updateCarbrandManage(carbrandManage);
    }

    /**
     * 品牌删除
     * @param id 品牌id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:18:59
     */
    @ApiOperation(value = "品牌删除", notes = "【wuyibo】")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, paramType = "path")
    @GetMapping("/delete/{id}")
    public StatusDto delete(@PathVariable("id") Long id) {
        return basicCarbrandManageService.deleteCarbrandManage(id);
    }

    /**
     * 品牌详情
     * @param id 品牌id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:18:59
     */
    @ApiOperation(value = "品牌详情", notes = "【wuyibo】")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, paramType = "path")
    @GetMapping("/detail/{id}")
    public StatusDto<CarbrandManage> detail(@PathVariable("id") Long id) {
        return StatusDto.buildDataSuccessStatusDto(basicCarbrandManageService.findCarbrandManageDetail(id));
    }

    /**
     * 分页查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @ApiOperation(value = "分页查询品牌列表", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandName", value = "品牌名称", paramType = "query"),
        @ApiImplicitParam(name = "initial", value = "品牌首字母", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "当前页数", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页条数", required = true, paramType = "query")})
    @GetMapping("/list")
    public StatusDto list(@RequestParam(required = false) String carbrandName,
                          @RequestParam(required = false) String initial,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "10") int limit) {
        return StatusDto.buildDataSuccessStatusDto(basicCarbrandManageService.queryCarbrandManagePage(carbrandName, initial, offset, limit));
    }

    /**
     * 首字母索引列表
     * @param
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 19:40:42
     */
    @ApiOperation(value = "首字母索引列表", notes = "【wuyibo】")
    @GetMapping("/initiallist")
    public StatusDto<List<String>> initialList() {
        return StatusDto.buildDataSuccessStatusDto(basicCarbrandManageService.queryInitialList());
    }

    /**
     * 查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 10:50:30
     */
    @ApiOperation(value = "查询所有品牌列表（不分页）", notes = "【wuyibo】")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandName", value = "品牌名称", paramType = "query"),
        @ApiImplicitParam(name = "initial", value = "品牌首字母", paramType = "query")})
    @GetMapping("/carbrandmanagelist")
    public StatusDto<List<CarbrandManage>> carbrandManageList(@RequestParam(required = false) String carbrandName,
                                                              @RequestParam(required = false) String initial) {
        return StatusDto.buildDataSuccessStatusDto(basicCarbrandManageService.queryCarbrandManageList(carbrandName, initial));
    }

}
