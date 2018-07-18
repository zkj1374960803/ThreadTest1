package com.ccbuluo.business.platform.carmanage.rest;

import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carmanage.dto.SearchCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 车辆基本信息controller
 * @author weijb
 * @date 2018-05-08 10:59:51
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/carmanage/basiccarcoreinfo")
@Api(tags = "车辆管理")
public class BasicCarcoreInfoController extends BaseController {

    @Autowired
    private BasicCarcoreInfoService basicCarcoreInfoService;

    /**
     * 车辆注册新增
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    @ApiOperation(value = "新增车辆", notes = "【weijb】")
    @PostMapping("/create")
    public StatusDto create(@ApiParam(name = "carcoreInfo对象", value = "传入json格式", required = true) CarcoreInfo carcoreInfo) {
        return basicCarcoreInfoService.saveCarcoreInfo(carcoreInfo);
    }

    /**
     * 车辆注册新增
     * @param carcoreInfo 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    @ApiOperation(value = "编辑车辆", notes = "【weijb】")
    @PostMapping("/edit")
    public StatusDto edit(@ApiParam(name = "saveCarcoreInfoDTO对象", value = "传入json格式", required = true) CarcoreInfo carcoreInfo) {
        return basicCarcoreInfoService.editCarcoreInfo(carcoreInfo);
    }



    /**
     * 根据车辆id查询车辆详情
     * @param carId 车辆id
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @ApiOperation(value = "根据车辆id查询车辆信息", notes = "【巢帅】carNumber:车辆编号,plateNumber:车牌号,vinNumber:车架号,storeName:门店名称,parkName:停车场")
    @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "query")
    @GetMapping("/querycardetailbycarid")
    public StatusDto queryCarDetailByCarId(Long carId) throws TException {
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryCarDetailByCarId(carId));
    }

    /**
     * 删除车辆
     * @param carId 车辆id
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    @ApiOperation(value = "删除车辆",notes = "【魏俊标】")
    @GetMapping("/delete/{carId}")
    @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "path")
    public StatusDto delete(@PathVariable Long carId) {
        basicCarcoreInfoService.deleteCarcoreInfoByCarId(carId);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param Keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    @ApiOperation(value = "车辆列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandId", value = "品牌id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "carseriesId", value = "车系id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "carStatus", value = "车辆状态", required = false, paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "Keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<SearchCarcoreInfoDTO>> queryCarcoreInfoList(@RequestParam(required = false) Long carbrandId,
                                                                          @RequestParam(required = false) Long carseriesId,
                                                                          @RequestParam(required = false) Integer carStatus,
                                                                          @RequestParam(required = false) String Keyword,
                                                                          @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                          @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryCarcoreInfoList(carbrandId, carseriesId, carStatus, Keyword, offset, pageSize));
    }

}
