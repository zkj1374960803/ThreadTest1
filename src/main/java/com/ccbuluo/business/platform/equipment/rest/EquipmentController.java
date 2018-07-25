package com.ccbuluo.business.platform.equipment.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.service.EquipmentService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:17:21
 */
@Api(tags = "物料")
@RestController
@RequestMapping("/platform/equipment")
public class EquipmentController extends BaseController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 保存物料
     * @param saveBizServiceEquipmentDTO 物料实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    @ApiOperation(value = "物料保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveEquiptype(@ApiParam(name = "物料对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
        int status = equipmentService.save(saveBizServiceEquipmentDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("保存失败！");
        } else if (status == Constants.FAILURE_TWO) {
            return StatusDto.buildFailure("编号生成失败，请重试！");
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }


    /**
     * 根据id查询物料详情
     * @param id 物料id
     * @return 物料详情
     * @author liuduo
     * @date 2018-07-17 19:12:20
     */
    @ApiOperation(value = "物料详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "id", value = "物料id",  required = true, paramType = "query")
    @GetMapping("/getbyid")
    public StatusDto<DetailBizServiceEquipmentDTO> getById(@RequestParam Long id) {
        return StatusDto.buildDataSuccessStatusDto(equipmentService.getById(id));
    }

    /**
     * 编辑物料
     * @param saveBizServiceEquipmentDTO 物料实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    @ApiOperation(value = "物料保存", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto editEquiptype(@ApiParam(name = "物料对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
        int status = equipmentService.editEquiptype(saveBizServiceEquipmentDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("修改失败！");
        }
        return StatusDto.buildSuccessStatusDto("修改成功！");
    }


    /**
     * 查询物料列表
     * @param equiptypeId 物料类型id
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @ApiOperation(value = "物料列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "equiptypeId", value = "物料类型id", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "keywork", value = "关键字", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数", paramType = "query", dataType = "int",required = true),
        @ApiImplicitParam(name = "pagesize", value = "每页数", paramType = "query", dataType = "int",required = true)})
    @GetMapping("/list")
    public StatusDto<Page<DetailBizServiceEquipmentDTO>> queryList(@RequestParam(required = false) Long equiptypeId,
                                                                   @RequestParam(required = false) String keyword,
                                                                   @RequestParam(defaultValue = "0") Integer offset,
                                                                   @RequestParam(defaultValue = "10") Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(equipmentService.queryList(equiptypeId, keyword, offset, pagesize));
    }

}
