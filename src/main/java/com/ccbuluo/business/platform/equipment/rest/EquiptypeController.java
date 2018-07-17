package com.ccbuluo.business.platform.equipment.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.service.EquiptypeService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param bizServiceEquiptype 物料类型实体
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 14:31:15
     */
    @ApiOperation(value = "物料类型保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveEquiptype(@ApiParam(name = "物料类型对象", value = "传入json格式", required = true)BizServiceEquiptype bizServiceEquiptype) {
        int status = equiptypeService.save(bizServiceEquiptype);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailure("该物料类型已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("保存失败！");
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    @ApiOperation(value = "物料类型列表", notes = "【刘铎】")
    @GetMapping("/list")
    public StatusDto equiptypeList() {
return null;
    }
}
