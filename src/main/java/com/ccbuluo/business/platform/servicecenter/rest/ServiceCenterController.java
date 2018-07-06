package com.ccbuluo.business.platform.servicecenter.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchListDTO;
import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterService;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.proxy.ThriftProxyServiceFactory;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.ServiceCenterWorkplaceDTO;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 服务中心controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 18:38:17
 */
@Api(tags = "服务中心")
@RestController
@RequestMapping("/platform/servicecenter")
public class ServiceCenterController extends BaseController {

    @Autowired
    private ServiceCenterService serviceCenterService;

    /**
     * 保存服务中心
     * @param saveServiceCenterDTO 服务中心实体
     * @return 保存状态
     * @author liuduo
     * @date 2018-07-04 09:40:53
     */
    @ApiOperation(value = "服务中心保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveServiceCenter(@ApiParam(name = "服务中心对象", value = "传入json格式", required = true)SaveServiceCenterDTO saveServiceCenterDTO)  throws TException {
        int status = serviceCenterService.saveServiceCenter(saveServiceCenterDTO);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildSuccessStatusDto("保存失败！");
        } else if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailureStatusDto("仓库名字已存在，请核对！");
        } else if (status == Constants.FAILURE_TWO) {
            return StatusDto.buildFailureStatusDto("服务中心名字已存在，请核对！");
        } else if (status == Constants.ORG_ERROR) {
            return StatusDto.buildFailureStatusDto("没有顶级服务中心，请核对！");
        }
        return StatusDto.buildFailureStatusDto("保存成功！");
    }

    /**
     * 服务中心详情
     * @param serviceCenterCode 服务中心code
     * @return 服务中心详情
     * @author liuduo
     * @date 2018-07-05 09:15:47
     */
    @ApiOperation(value = "服务中心详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query")
    @GetMapping("/getbycode")
    public StatusDto getByCode(@RequestParam String serviceCenterCode) throws TException {
        return StatusDto.buildDataSuccessStatusDto(serviceCenterService.getByCode(serviceCenterCode));
    }


    /**
     * 编辑服务中心
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterName 服务中心名称
     * @param labels 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:10:30
     */
    @ApiOperation(value = "编辑服务中心", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "serviceCenterName", value = "服务中心名字",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "labels", value = "标签ids",  required = true, paramType = "query"),})
    @GetMapping("/edit")
    public StatusDto editServiceCenter(@RequestParam String serviceCenterCode,
                                       @RequestParam String serviceCenterName,
                                       @RequestParam String labels) throws TException {

        int status = serviceCenterService.editServiceCenter(serviceCenterCode, serviceCenterName, labels);
        if (status == Constants.FAILURE_ONE) {
            return StatusDto.buildFailureStatusDto("服务中心名字已存在，请核对！");
        } else if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailureStatusDto("编辑失败！");
        }
        return StatusDto.buildSuccessStatusDto("编辑成功");
    }


    /**
     * 根据服务中心code查询职场
     * @param serviceCenterCode 服务中心code
     * @return 职场信息
     * @author liuduo
     * @date 2018-07-05 13:49:42
     */
    @ApiOperation(value = "根据服务中心code查询职场", notes = "【刘铎】")
    @ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query")
    @GetMapping("/getworkplacebycode")
    public StatusDto getWorkplaceByCode(@RequestParam String serviceCenterCode)  throws TException  {
        return StatusDto.buildDataSuccessStatusDto(serviceCenterService.getWorkplaceByCode(serviceCenterCode));
    }


    /**
     * 编辑职场
     * @param serviceCenterWorkplaceDTO 职场实体
     * @return 编辑状态
     * @author liuduo
     * @date 2018-07-05 14:09:09
     */
    @ApiOperation(value = "编辑服务中心职场", notes = "【刘铎】")
    @PostMapping("/editworkplace")
    public StatusDto editWorkplace(@ApiParam(name = "服务中心职场对象", value = "传入json格式", required = true)ServiceCenterWorkplaceDTO serviceCenterWorkplaceDTO) throws TException {
        int status = serviceCenterService.editWorkplace(serviceCenterWorkplaceDTO);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("编辑成功！");
        }
        return StatusDto.buildFailureStatusDto("编辑失败！");
    }

    /**
     * 查询服务中心列表
     * @param searchListDTO 查询服务中心列表参数
     * @return 服务中心列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    @ApiOperation(value = "服务中心列表", notes = "【刘铎】")
    @PostMapping("/list")
    public StatusDto<Map<String, Object>> queryList(@ApiParam(name = "服务中心查询对象", value = "传入json格式", required = true)SearchListDTO searchListDTO) throws TException, IOException {
        return StatusDto.buildDataSuccessStatusDto(serviceCenterService.queryList(searchListDTO));
    }

    /**
     * 查询可用的服务中心
     * @param province 省
     * @param city 市
     * @param area 区
     * @param name 服务中心名字
     * @return 可用的服务中心
     * @author liuduo
     * @date 2018-07-06 10:00:52
     */
    @ApiOperation(value = "查询可用的服务中心", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "province", value = "省",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "city", value = "市",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "area", value = "区",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "name", value = "服务中心名字",  required = false, paramType = "query")})
    @GetMapping("/findusableservicecenter")
    public StatusDto findUsableServiceCenter(@RequestParam(required = false) String province,
                                             @RequestParam(required = false) String city,
                                             @RequestParam(required = false) String area,
                                             @RequestParam(required = false) String name) throws TException {
        return StatusDto.buildDataSuccessStatusDto(getOrgServer().queryServiceCenter(province, city, area, name));
    }


    /**
     * 服务中心启停
     * @param erviceCenterCode 服务中心code
     * @param serviceCenterStatus 服务中心状态
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-07-06 10:11:00
     */
    @ApiOperation(value = "服务中心启停", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "erviceCenterCode", value = "服务中心code",  required = true, paramType = "query",dataType = "string"),
        @ApiImplicitParam(name = "serviceCenterStatus", value = "状态",  required = true, paramType = "query", dataType = "integer")})
    @GetMapping("/editstatus")
    public StatusDto editOrgStatus(@RequestParam String erviceCenterCode,
                                          @RequestParam Integer serviceCenterStatus) throws TException {
        int status = serviceCenterService.editOrgStatus(erviceCenterCode, serviceCenterStatus);
        if (status == Constants.SUCCESSSTATUS) {
            return StatusDto.buildSuccessStatusDto("编辑成功！");
        }
        return StatusDto.buildFailureStatusDto("编辑失败！");
    }




    /**
     * 获取组织架构服务
     * @return 用户服务中心组织架构服务
     * @author liuduo
     * @date 2018-07-04 10:38:44
     */
    private BasicUserOrganizationService.Iface getOrgServer() {
        return (BasicUserOrganizationService.Iface) ThriftProxyServiceFactory.newInstance(BasicUserOrganizationService.class, SystemPropertyHolder.getUserCoreRpcSerName());
    }
}
