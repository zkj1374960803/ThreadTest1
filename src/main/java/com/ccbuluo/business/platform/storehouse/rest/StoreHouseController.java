package com.ccbuluo.business.platform.storehouse.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 09:56:42
 */
@Api(tags = "仓库")
@RestController
@RequestMapping("/platform/storehouse")
public class StoreHouseController extends BaseController {

    @Autowired
    private StoreHouseService storeHouseService;

    /**
     * 保存仓库
     * @param saveBizServiceStorehouseDTO 仓库保存用的实体dto
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-07-03 10:20:14
     */
    @ApiOperation(value = "仓库保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveStoreHouse(@ApiParam(name = "仓库对象", value = "传入json格式", required = true) SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) {
        int status = storeHouseService.saveStoreHouse(saveBizServiceStorehouseDTO);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("保存成功！");
        }
        return StatusDto.buildFailureStatusDto("保存失败！");
    }

    /**
     * 启停仓库
     * @param id 仓库id
     * @param storeHouseStatus 仓库状态
     * @return  操作是否成功
     * @author liuduo
     * @date 2018-07-03 10:37:55
     */
    @ApiOperation(value = "仓库启停", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "仓库id", required = true),
        @ApiImplicitParam(name = "storeHouseStatus", value = "仓库状态", required = true)})
    @GetMapping("/editstorehousestatus")
    public StatusDto editStoreHouseStatus(@RequestParam Long id,
                                          @RequestParam Integer storeHouseStatus) {
        int status = storeHouseService.editStoreHouseStatus(id, storeHouseStatus);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("操作成功！");
        }
        return StatusDto.buildFailureStatusDto("操作失败！");
    }

    /**
     * 编辑仓库
     * @param saveBizServiceStorehouseDTO 仓库实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-03 11:21:20
     */
    @ApiOperation(value = "编辑仓库", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto edit(@ApiParam(name = "仓库对象", value = "传入json格式", required = true) SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) {
        int status = storeHouseService.editStoreHouse(saveBizServiceStorehouseDTO);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("编辑成功！");
        }
        return StatusDto.buildFailureStatusDto("编辑失败！");
    }

    /**
     * 根据id查询仓库详情
     * @param id 仓库id
     * @return 仓库详情
     * @author liuduo
     * @date 2018-07-03 11:29:10
     */
    @ApiOperation(value = "仓库详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "id", value = "仓库id",  required = true)
    @GetMapping("/getbyid")
    public StatusDto getById(@RequestParam Long id) {
        return StatusDto.buildDataSuccessStatusDto(storeHouseService.getById(id));
    }

}
