package com.ccbuluo.business.platform.carmanage.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carmanage.dto.ListCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.dto.SearchCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.dto.UpdateCarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private InnerUserInfoService innerUserInfoService;

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
    public StatusDto create(@ApiParam(name = "carcoreInfo对象", value = "传入json格式", required = true)@RequestBody CarcoreInfo carcoreInfo) {
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
    public StatusDto edit(@ApiParam(name = "saveCarcoreInfoDTO对象", value = "传入json格式", required = true)@RequestBody CarcoreInfo carcoreInfo) {
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
        return basicCarcoreInfoService.deleteCarcoreInfoByCarId(carId);
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
            @ApiImplicitParam(name = "storeAssigned", value = "车辆状态", required = false, paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "custmanagerUuid", value = "客户经理uuid【车辆管理列表不传，客户经理的管理车辆列表必传】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "Keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<SearchCarcoreInfoDTO>> queryCarcoreInfoList(@RequestParam(required = false) Long carbrandId,
                                                                          @RequestParam(required = false) Long carseriesId,
                                                                          @RequestParam(required = false) String Keyword,
                                                                          @RequestParam(required = false) String custmanagerUuid,
                                                                          @RequestParam(required = false) Integer storeAssigned,
                                                                          @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                          @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryCarcoreInfoList(carbrandId, carseriesId, storeAssigned, custmanagerUuid, Keyword, offset, pageSize));
    }
    /**
     * 查询未分配的车辆列表
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    @ApiOperation(value = "查询未分配的车辆列表",notes = "【魏俊标】")
    @ApiImplicitParam(name = "vinNumber", value = "车辆vin", required = false, paramType = "query")
    @GetMapping("/queryundistributedlist")
    public StatusDto<List<ListCarcoreInfoDTO>> queryuUndistributedList(@RequestParam(required = false) String vinNumber) {
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryuUndistributedList(vinNumber));
    }

    /**
     * 批量更新维修车状态（根据车辆code）
     * @param carcoreInfoList
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    @ApiOperation(value = "批量更新维修车状态（根据车辆code）", notes = "【魏俊标】")
    @PostMapping("/updatestatusbycode")
    public StatusDto updateStatusByCode(@ApiParam(name = "updateCarcoreInfoDTO集合", value = "传入updateCarcoreInfoDTO数组", required = true)@RequestBody List<UpdateCarcoreInfoDTO> carcoreInfoList) {
        int status = basicCarcoreInfoService.updateStatusByCode(carcoreInfoList);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("操作失败！");
        }
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }
    /**
     * 根据车架号查询车辆信息
     * @param vinNumber 车辆vin
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @ApiOperation(value = "根据车辆vin查询车辆信息", notes = "【魏俊标】")
    @ApiImplicitParam(name = "vinNumber", value = "车辆vin", required = true, paramType = "query")
    @GetMapping("/getcarinfobyvin")
    public StatusDto getCarInfoByVin(@RequestParam String vinNumber,@RequestParam String appId,@RequestParam String secretId) throws TException {
        StatusDto<String> statusDto = innerUserInfoService.checkAppIdAndSecretId( appId, secretId);
        //权限校验
        if(Constants.ERROR_CODE.equals(statusDto.getCode())){
            return statusDto;
        }
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.getCarInfoByVin(vinNumber));
    }
    /**
     * 根据车辆vin更新车辆的门店信息
     * @param vinNumber 车架号
     * @param storeCode 门店code
     * @param storeName 门店名称
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-08-01 15:55:14
     */
    @ApiOperation(value = "根据车辆vin更新车辆的门店信息", notes = "【魏俊标】")
    @ApiImplicitParams({@ApiImplicitParam(name = "vinNumber", value = "车架号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "storeCode", value = "门店code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "storeName", value = "门店名称", required = false, paramType = "query")})
    @GetMapping("/updatecarcoreinfobyvin")
    public StatusDto updateCarcoreInfoByVin(@RequestParam String vinNumber,@RequestParam String storeCode,@RequestParam String storeName
            ,@RequestParam String appId,@RequestParam String secretId) {
        StatusDto<String> statusDto = innerUserInfoService.checkAppIdAndSecretId( appId, secretId);
        //权限校验
        if(Constants.ERROR_CODE.equals(statusDto.getCode())){
            return statusDto;
        }
        int flag = basicCarcoreInfoService.updateCarcoreInfoByVin(vinNumber,storeCode,storeName);
        if (flag != Constants.STATUS_FLAG_ZERO) {
            return StatusDto.buildSuccessStatusDto("操作成功！");
        }
        return StatusDto.buildFailureStatusDto("操作失败！");
    }

    /**
     * 解除车辆与客户经理的关联关系
     * @param carNumber 车辆编号
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-08-01 14:23:04
     */
    @ApiOperation(value = "移除车辆与客户经理的关联关系", notes = "【刘铎】")
    @ApiImplicitParam(name = "carNumber", value = "车辆编码", required = true, paramType = "query")
    @GetMapping("/release")
    public StatusDto release(@RequestParam String carNumber) {
        int status = basicCarcoreInfoService.release(carNumber);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("操作失败！");
        }
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

}
