package com.ccbuluo.business.platform.equipment.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquiptypeDTO;
import com.ccbuluo.business.platform.equipment.service.EquiptypeService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料类型controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:21:23
 */
@Api(tags = "物料类型")
@RestController
@RequestMapping("/platform/equiptype")
public class EquiptypeController extends BaseController {

    @Autowired
    private EquiptypeService equiptypeService;

    /**
     * 保存物料类型
     * @param saveBizServiceEquiptypeDTO 物料类型实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 14:31:15
     */
    @ApiOperation(value = "物料类型保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveEquiptype(@ApiParam(name = "物料类型对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO) {
        int status = equiptypeService.save(saveBizServiceEquiptypeDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料类型已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("保存失败！");
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     * 查询物料列表
     * @return 物料类型列表
     * @author liuduo
     * @date 2018-07-17 14:48:07
     */
    @ApiOperation(value = "物料类型列表", notes = "【刘铎】")
    @GetMapping("/list")
    public StatusDto<List<BizServiceEquiptype>> equiptypeList() {
        return StatusDto.buildDataSuccessStatusDto(equiptypeService.queryList());
    }

    /**
     * 修改物料类型
     * @param saveBizServiceEquiptypeDTO 物料类型实体
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-07-17 14:54:18
     */
    @ApiOperation(value = "物料类型编辑", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto editEquiptype(@ApiParam(name = "物料类型对象", value = "传入json格式", required = true)@RequestBody SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO) {
        int status = equiptypeService.edit(saveBizServiceEquiptypeDTO);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料类型已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("修改失败！");
        }
        return StatusDto.buildSuccessStatusDto("修改成功！");
    }

    /**
     * 根据id删除物料类型
     * @param id 物料类型id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-17 15:03:48
     */
    @ApiOperation(value = "物料类型删除", notes = "【刘铎】")
    @ApiImplicitParam(name = "id", value = "物料类型id",  required = true, paramType = "query")
    @DeleteMapping("/delete")
    public StatusDto deleteById(@RequestParam Long id) {
        int status = equiptypeService.deleteById(id);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("删除失败！");
        } else if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("删除失败！该类型下存在物料，请核对！");
        }
        return StatusDto.buildSuccessStatusDto("删除成功！");
    }


}
