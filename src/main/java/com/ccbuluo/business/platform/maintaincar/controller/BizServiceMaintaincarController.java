package com.ccbuluo.business.platform.maintaincar.controller;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintaincar;
import com.ccbuluo.business.platform.maintaincar.dto.ListServiceMaintaincarDTO;
import com.ccbuluo.business.platform.maintaincar.dto.SearchBizServiceMaintaincarDTO;
import com.ccbuluo.business.platform.maintaincar.service.BizServiceMaintaincarService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 维修车辆基本信息controller
 * @author weijb
 * @date 2018-07-18 14:59:51
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/afterSales/servicemaintaincar")
@Api(tags = "维修车辆管理")
public class BizServiceMaintaincarController extends BaseController {

    @Resource
    private BizServiceMaintaincarService bizServiceMaintaincarService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;

    /**
     * 车辆注册新增
     * @param bizServiceMaintaincar 维修车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "新增维修车辆", notes = "【weijb】")
    @PostMapping("/create")
    public StatusDto create(@ApiParam(name = "bizServiceMaintaincar对象", value = "传入json格式", required = true)@RequestBody BizServiceMaintaincar bizServiceMaintaincar) {
        // 生成编码
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FR);
        //获取code失败
        if(!Constants.SUCCESS_CODE.equals(stringStatusDto.getCode())){
            return stringStatusDto;
        }
        bizServiceMaintaincar.setMendCode(stringStatusDto.getData());
        return bizServiceMaintaincarService.saveServiceMaintaincar(bizServiceMaintaincar);
    }

    /**
     * 车辆注册新增
     * @param bizServiceMaintaincar 维修车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "编辑维修车辆", notes = "【weijb】")
    @PostMapping("/edit")
    public StatusDto edit(@ApiParam(name = "bizServiceMaintaincar对象", value = "传入json格式", required = true)@RequestBody BizServiceMaintaincar bizServiceMaintaincar) {
        return bizServiceMaintaincarService.editServiceMaintaincar(bizServiceMaintaincar);
    }



    /**
     * 根据车辆id查询车辆详情
     * @param carId 车辆id
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "根据维修车辆id查询维修车辆信息", notes = "根据维修车辆id查询维修车辆信息")
    @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "query")
    @GetMapping("/queryservicemaintaincarbycarid")
    public StatusDto queryServiceMaintaincarByCarId(Long carId) throws TException {
        return StatusDto.buildDataSuccessStatusDto(bizServiceMaintaincarService.queryServiceMaintaincarByCarId(carId));
    }

    /**
     * 删除车辆
     * @param carId 车辆id
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "删除维修车辆",notes = "【魏俊标】")
    @GetMapping("/delete/{carId}")
    @ApiImplicitParam(name = "carId", value = "维修车辆id", required = true, paramType = "path")
    public StatusDto delete(@PathVariable Long carId) {
        bizServiceMaintaincarService.deleteServiceMaintaincarByCarId(carId);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "维修车辆列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "carbrandId", value = "品牌id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "carseriesId", value = "车系id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "carStatus", value = "维修车辆状态", required = false, paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<SearchBizServiceMaintaincarDTO>> queryCarcoreInfoList(@RequestParam(required = false) Long carbrandId,
                                                                                @RequestParam(required = false) Long carseriesId,
                                                                                @RequestParam(required = false) Integer carStatus,
                                                                                @RequestParam(required = false) String keyword,
                                                                                @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceMaintaincarService.queryServiceMaintaincarList(carbrandId, carseriesId, carStatus, keyword, offset, pageSize));
    }
    /**
     * 查询未分配的维修车列表
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    @ApiOperation(value = "查询未分配的维修车列表",notes = "【魏俊标】")
    @GetMapping("/queryundistributedlist")
    public StatusDto<List<ListServiceMaintaincarDTO>> queryundistributedlist() {
        return StatusDto.buildDataSuccessStatusDto(bizServiceMaintaincarService.queryundistributedlist());
    }

}
