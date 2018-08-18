package com.ccbuluo.business.platform.storehouse.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SearchStorehouseListDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterListDTO;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public StatusDto saveStoreHouse(@ApiParam(name = "仓库对象", value = "传入json格式", required = true) SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) throws TException {
        int status = storeHouseService.saveStoreHouse(saveBizServiceStorehouseDTO);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("保存成功！");
        } else if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailureStatusDto("仓库名字已存在，请核对！");
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
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "仓库id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "storeHouseStatus", value = "仓库状态", required = true, paramType = "query")})
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
        } else if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailureStatusDto("仓库名字已存在，请核对！");
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
    @ApiImplicitParam(name = "id", value = "仓库id",  required = true, paramType = "query")
    @GetMapping("/getbyid")
    public StatusDto getById(@RequestParam Long id) throws TException {
        return StatusDto.buildDataSuccessStatusDto(storeHouseService.getById(id));
    }

    /**
     * 查询仓库列表
     * @param provinceName 省
     * @param cityName 市
     * @param areaName 区
     * @param storeHouseStatus 状态
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 仓库列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    @ApiOperation(value = "仓库列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "provinceName", value = "省", required = false, paramType = "query"),
        @ApiImplicitParam(name = "cityName", value = "市", required = false, paramType = "query"),
        @ApiImplicitParam(name = "areaName", value = "区", required = false, paramType = "query"),
        @ApiImplicitParam(name = "storeHouseStatus", value = "状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pagesize", value = "每页数", required = true, paramType = "query")})
    @PostMapping("/list")
    public StatusDto<Page<SearchStorehouseListDTO>> queryList(@RequestParam(required = false) String provinceName,
                                                              @RequestParam(required = false) String cityName,
                                                              @RequestParam(required = false) String areaName,
                                                              @RequestParam(required = false) Integer storeHouseStatus,
                                                              @RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer offset,
                                                              @RequestParam(defaultValue = "10") Integer pagesize) throws TException  {
        return StatusDto.buildDataSuccessStatusDto(storeHouseService.queryList(provinceName, cityName, areaName, storeHouseStatus, keyword, offset, pagesize));
    }

    /**
     * 根据服务中心查询启用的仓库列表（下拉框）
     * @param serviceCenterCode 据服务中心code
     * @return StatusDto<QueryStorehouseDTO>
     * @author zhangkangjian
     * @date 2018-08-07 14:32:09
     */
    @ApiOperation(value = "根据服务中心查询启用的仓库列表（下拉框）", notes = "【张康健】")
    @ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query",dataType = "string")
    @GetMapping("/querystorehousebyservicecentercode")
    public StatusDto<List<QueryStorehouseDTO>> queryStorehouseByServiceCenterCode(@RequestParam String serviceCenterCode) {
        return storeHouseService.queryStorehouseByServiceCenterCode(serviceCenterCode);
    }

}
