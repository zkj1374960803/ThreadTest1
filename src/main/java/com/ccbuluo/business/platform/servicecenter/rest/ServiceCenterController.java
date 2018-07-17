package com.ccbuluo.business.platform.servicecenter.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchListDTO;
import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterDTO;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterListDTO;
import com.ccbuluo.usercoreintf.dto.ServiceCenterWorkplaceDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
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
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;

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
        return serviceCenterService.saveServiceCenter(saveServiceCenterDTO);
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
     * @param labelIds 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:10:30
     */
    @ApiOperation(value = "编辑服务中心", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "serviceCenterName", value = "服务中心名字",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "labelIds", value = "标签ids",  required = true, paramType = "query"),})
    @GetMapping("/edit")
    public StatusDto<String> editServiceCenter(@RequestParam String serviceCenterCode,
                                       @RequestParam String serviceCenterName,
                                       @RequestParam String labelIds) throws TException {

        return serviceCenterService.editServiceCenter(serviceCenterCode, serviceCenterName, labelIds);
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
    public StatusDto<ServiceCenterWorkplaceDTO> getWorkplaceByCode(@RequestParam String serviceCenterCode)  throws TException  {
        StatusDtoThriftBean<ServiceCenterWorkplaceDTO> workplaceByCode = serviceCenterService.getWorkplaceByCode(serviceCenterCode);
        return StatusDtoThriftUtils.resolve(workplaceByCode, ServiceCenterWorkplaceDTO.class);
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
        StatusDto<String> stringStatusDto = serviceCenterService.editWorkplace(serviceCenterWorkplaceDTO);
        String code = stringStatusDto.getCode();
        if (code.equals(Constants.ERROR_CODE)) {
            return StatusDto.buildFailureStatusDto("编辑失败！");
        }
        return StatusDto.buildSuccessStatusDto("编辑成功！");
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
    public StatusDto<Page<QueryServiceCenterDTO>> queryList(@ApiParam(name = "服务中心查询对象", value = "传入json格式", required = true)SearchListDTO searchListDTO) {
        StatusDtoThriftPage<QueryServiceCenterDTO> queryServiceCenterDTOStatusDtoThriftPage = serviceCenterService.queryList(searchListDTO);
        return StatusDtoThriftUtils.resolve(queryServiceCenterDTOStatusDtoThriftPage, QueryServiceCenterDTO.class);
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
    public StatusDto<List<QueryServiceCenterListDTO>> findUsableServiceCenter(@RequestParam(required = false) String province,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String area,
                                                   @RequestParam(required = false) String name) {
        StatusDtoThriftList<QueryServiceCenterListDTO> queryServiceCenterListDTOStatusDtoThriftList = orgService.queryServiceCenter(province, city, area, name);
        return StatusDtoThriftUtils.resolve(queryServiceCenterListDTOStatusDtoThriftList, QueryServiceCenterListDTO.class);
    }


    /**
     * 服务中心启停
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterStatus 服务中心状态
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-07-06 10:11:00
     */
    @ApiOperation(value = "服务中心启停", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "serviceCenterCode", value = "服务中心code",  required = true, paramType = "query",dataType = "string"),
        @ApiImplicitParam(name = "serviceCenterStatus", value = "状态",  required = true, paramType = "query", dataType = "integer")})
    @GetMapping("/editstatus")
    public StatusDto editOrgStatus(@RequestParam String serviceCenterCode,
                                          @RequestParam Integer serviceCenterStatus) throws TException {
        StatusDto<String> stringStatusDto = serviceCenterService.editOrgStatus(serviceCenterCode, serviceCenterStatus);
        if (stringStatusDto.equals(Constants.ERROR_CODE)) {
            return StatusDto.buildSuccessStatusDto("编辑失败！");
        }
        return StatusDto.buildFailureStatusDto("编辑成功！");
    }
}
