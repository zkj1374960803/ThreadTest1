package com.ccbuluo.business.platform.equipment.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.service.EquipmentService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public StatusDto saveEquiptype(@ApiParam(name = "物料对象", value = "传入json格式", required = true)@RequestBody @ValidateGroup SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
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
    public StatusDto<DetailBizServiceEquipmentDTO> getById(@RequestParam @ValidateNotNull(message = "id不能为空") Long id) {
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
    public StatusDto editEquiptype(@ApiParam(name = "物料对象", value = "传入json格式", required = true)@RequestBody @ValidateGroup SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
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
     * @param pageSize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @ApiOperation(value = "物料列表", notes = "【刘铎】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "equiptypeId", value = "物料类型id", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数", paramType = "query", dataType = "int",required = true),
        @ApiImplicitParam(name = "pageSize", value = "每页数", paramType = "query", dataType = "int",required = true)})
    @GetMapping("/list")
    public StatusDto<Page<DetailBizServiceEquipmentDTO>> queryList(@RequestParam(required = false) Long equiptypeId,
                                                                   @RequestParam(required = false) String keyword,
                                                                   @RequestParam(defaultValue = "0") Integer offset,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(equipmentService.queryList(equiptypeId, keyword, offset, pageSize));
    }


    /**
     * 查询计量单位
     * @return 计量单位
     * @author liuduo
     * @date 2018-07-31 19:10:23
     */
    @ApiOperation(value = "计量单位", notes = "【刘铎】")
    @GetMapping("/equipunitlist")
    public StatusDto<List<Map<String,String>>> getEquipUnitList() {
        return StatusDto.buildDataSuccessStatusDto(equipmentService.getUnit());
    }


    /**
     * 根据物料类型id查询物料
     * @param equiptypeId 物料类型id
     * @return 物料
     * @author liuduo
     * @date 2018-08-02 10:41:20
     */
    @ApiOperation(value = "根据物料类型查物料", notes = "【刘铎】")
    @ApiImplicitParam(name = "equiptypeId", value = "物料类型id", paramType = "query", dataType = "int")
    @GetMapping("/queryequpmentbyequiptype")
    public StatusDto<List<DetailBizServiceEquipmentDTO>> queryEqupmentByEquiptype(@ValidateNotNull(message = "equiptypeId(物料类型id)不能为空") Long equiptypeId) {
        return StatusDto.buildDataSuccessStatusDto(equipmentService.queryEqupmentByEquiptype(equiptypeId));
    }

    /**
     * 根据code删除物料
     * @param equipCode 物料code
     * @return 是否删除成功
     * @author liuduo
     * @date 2018-08-23 11:10:57
     */
    @ApiOperation(value = "删除物料", notes = "【刘铎】")
    @ApiImplicitParam(name = "equipCode", value = "物料code", paramType = "query", required = true)
    @DeleteMapping("/delete")
    public StatusDto delete(@RequestParam @ValidateNotBlank(message = "equipCode(物料code)不能为空") String equipCode) {
        int delete = equipmentService.delete(equipCode);
        if (delete == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料已被关联，无法删除！");
        } else if (delete == Constants.DELETE_FLAG_NORMAL) {
            return StatusDto.buildFailure("删除失败！");
        }

        return StatusDto.buildSuccessStatusDto("删除成功！");
    }
}
