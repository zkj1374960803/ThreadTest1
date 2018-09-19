package com.ccbuluo.business.platform.maintainitem.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.service.MaintainitemService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工时controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:15:41
 */
@Api(tags = "工时")
@RestController
@RequestMapping("/platform/maintainitem")
public class MaintainitemController extends BaseController {

    @Autowired
    private MaintainitemService maintainitemService;

    /**
     * 保存工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    @ApiOperation(value = "工时保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveMaintainitem(@ApiParam(name = "工时对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        int status = maintainitemService.save(saveBizServiceMaintainitemDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该服务已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("保存失败！");
        } else if (status == Constants.FAILURE_TWO) {
            return StatusDto.buildFailure("编号生成失败，请重试！");
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     * 根据id查询详情
     * @param id 工时id
     * @return 工时详情
     * @author liuduo
     * @date 2018-07-18 10:57:07
     */
    @ApiOperation(value = "工时详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "id", value = "工时id",  required = true, paramType = "query")
    @GetMapping("/getbyid")
    public StatusDto<DetailBizServiceMaintainitemDTO> getById(@RequestParam Long id) {
        return StatusDto.buildDataSuccessStatusDto(maintainitemService.getById(id));
    }

    /**
     * 编辑工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    @ApiOperation(value = "工时编辑", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto editMaintainitem(@ApiParam(name = "工时对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        int status = maintainitemService.edit(saveBizServiceMaintainitemDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该服务已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("编辑失败！");
        }
        return StatusDto.buildSuccessStatusDto("编辑成功！");
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @ApiOperation(value = "工时列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数", paramType = "query", dataType = "int",required = true),
        @ApiImplicitParam(name = "pagesize", value = "每页数", paramType = "query", dataType = "int",required = true)})
    @GetMapping("/list")
    public StatusDto<Page<DetailBizServiceMaintainitemDTO>> queryList(@RequestParam(required = false) String keyword,
                                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                                      @RequestParam(defaultValue = "10") Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(maintainitemService.queryList(keyword, offset, pagesize));
    }


    /**
     * 根据code删除工时
     * @param equipCode 工时code
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-09-18 14:32:56
     */
    @ApiOperation(value = "工时删除", notes = "【刘铎】")
    @ApiImplicitParam(name = "equipCode", value = "工时编号", paramType = "query",required = true)
    @DeleteMapping("/delete")
    public StatusDto delete(@RequestParam String equipCode) {
        int status = maintainitemService.delete(equipCode);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该服务项已被引用，无法删除！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("删除失败！");
        }
        return StatusDto.buildSuccessStatusDto();
    }
}
