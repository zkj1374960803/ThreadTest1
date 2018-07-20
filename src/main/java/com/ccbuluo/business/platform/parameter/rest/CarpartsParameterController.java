package com.ccbuluo.business.platform.parameter.rest;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parameter.dto.BasicCarpartsParameterDTO;
import com.ccbuluo.merchandiseintf.carparts.parameter.dto.EditBasicCarpartsParameterDTO;
import com.ccbuluo.merchandiseintf.carparts.parameter.dto.RelCarpartsCateparamDTO;
import com.ccbuluo.merchandiseintf.carparts.parameter.dto.SaveBasicCarpartsParameterDTO;
import com.ccbuluo.merchandiseintf.carparts.parameter.service.CarpartsParameterService;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 零部件参数
 * @author weijb
 * @date 2018-06-29 14:18:47
 */
@Api(tags = "零配件参数管理(平台端)")
@RestController
@RequestMapping("/afterSales/carpartsparameter")
public class CarpartsParameterController extends BaseController {

    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsParameterService carpartsParameterService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    UserHolder userHolder;

    /**
     * 添加零配件
     * @param saveBasicCarpartsParameterDTO 零配件实体
     * @return 是否保存成功
     * @author weijb
     * @date 2018-07-04 19:52:44
     */
    @ApiOperation(value = "添加参数",notes = "【魏俊标】")
    @PostMapping("/saveCarpartsParameter")
    public StatusDto saveCarpartsParameter(@ApiParam(name = "saveBasicCarpartsParameterDTO对象", value = "传入json格式", required = true) SaveBasicCarpartsParameterDTO saveBasicCarpartsParameterDTO)  throws TException {
        // 生成编码
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FM);
        //获取code失败
        if(!Constants.SUCCESS_CODE.equals(stringStatusDto.getCode())){
            return stringStatusDto;
        }
        saveBasicCarpartsParameterDTO.setParameterCode(stringStatusDto.getData());
        saveBasicCarpartsParameterDTO.setCreator(userHolder.getLoggedUserId());
        return StatusDtoThriftUtils.resolve(carpartsParameterService.saveCarpartsParameter(saveBasicCarpartsParameterDTO), String.class);
    }

    /**
     * 编辑零部件参数
     * @param saveBasicCarpartsParameterDTO
     * @author weijb
     * @date 2018-07-05 10:10:43
     */
    @ApiOperation(value = "编辑零配件参数",notes = "【魏俊标】")
    @PostMapping("/editCarpartsParameter")
    public StatusDto editCarpartsParameter(@ApiParam(name = "saveBasicCarpartsParameterDTO", value = "传入json格式", required = true) SaveBasicCarpartsParameterDTO saveBasicCarpartsParameterDTO) {
        saveBasicCarpartsParameterDTO.setOperator(userHolder.getLoggedUserId());
        return StatusDtoThriftUtils.resolve(carpartsParameterService.editCarpartsParameter(saveBasicCarpartsParameterDTO), String.class);
    }

    /**
     * 启停零配件参数
     * @param parameterCode 参数code
     * @param parameterStatus 参数状态
     * @return  操作是否成功
     * @author weijb
     * @date 2018-07-05 10:10:43
     */
    @ApiOperation(value = "零配件参数启停", notes = "【魏俊标】")
    @ApiImplicitParams({@ApiImplicitParam(name = "parameterCode", value = "参数code", required = true, paramType = "query"),
            @ApiImplicitParam(name = "parameterStatus", value = "参数状态", required = true, paramType = "query")})
    @GetMapping("/editParameterStatus")
    public StatusDto editParameterStatus(@RequestParam String parameterCode,
                                         @RequestParam Integer parameterStatus) {
        return StatusDtoThriftUtils.resolve(carpartsParameterService.editParameterStatus(parameterCode, parameterStatus),String.class);
    }
    /**
     * 删除零配件参数
     * @param parameterCode 参数code
     * @return  操作是否成功
     * @author weijb
     * @date 2018-07-05 10:10:43
     */
    @ApiOperation(value = "删除零配件参数", notes = "【魏俊标】")
    @ApiImplicitParam(name = "parameterCode", value = "参数Code", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteCarpartsParameter/{parameterCode}")
    public StatusDto deleteCarpartsParameter(@PathVariable String parameterCode) {
        return StatusDtoThriftUtils.resolve(carpartsParameterService.deleteCarpartsParameter(parameterCode),String.class);
    }

    /**
     * 获取零部件参数详情
     * @param parameterCode 零部件code
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-05 10:50:43
     */
    @ApiOperation(value = "查询零配件参数详情",notes = "【魏俊标】")
    @ApiImplicitParam(name = "parameterCode", value = "零部件参数Code", required = true, dataType = "String", paramType = "path")
    @GetMapping("/findCarpartsParameterdetail/{parameterCode}")
    public StatusDto<EditBasicCarpartsParameterDTO> findCarpartsParameterdetail(@PathVariable String parameterCode){
        return StatusDtoThriftUtils.resolve(carpartsParameterService.findCarpartsParameterdetail(parameterCode),EditBasicCarpartsParameterDTO.class);
    }

    /**
     * 根据分类code（末级）查询参数列表
     * @param categoryCode 零部件分类code
     * @author weijb
     * @date 2018-07-05 14:52:44
     */
    @ApiOperation(value = "根据零部件分类code（末级）查询参数列表",notes = "【魏俊标】")
    @ApiImplicitParam(name = "categoryCode", value = "分类Code", required = true, dataType = "String", paramType = "path")
    @GetMapping("/getParameterListByCategoryCode/{categoryCode}")
    protected StatusDto<List<RelCarpartsCateparamDTO>> getParameterListByCategoryCode(@PathVariable String categoryCode) {
        return StatusDtoThriftUtils.resolve(carpartsParameterService.getParameterListByCategoryCode(categoryCode),RelCarpartsCateparamDTO.class);
    }

    /**
     * 查询通过参数列表
     * @author weijb
     * @date 2018-07-05 14:52:44
     */
    @ApiOperation(value = "查询通过参数列表",notes = "【魏俊标】")
    @GetMapping("/getCommonParameterList")
    protected StatusDto<List<RelCarpartsCateparamDTO>> getCommonParameterList() {
        return StatusDtoThriftUtils.resolve(carpartsParameterService.getCommonParameterList(),RelCarpartsCateparamDTO.class);
    }

    /**
     *零配件参数列表分页查询
     * @param parameterType 类型
     * @param parameterStatus 状态
     * @param parameterName 参数名称
     * @param parameterProperty 属性
     * @param categoryCode 末级分类
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-05 14:52:44
     */
    @ApiOperation(value = "零部件参数列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "parameterType", value = "参数类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "parameterStatus", value = "参数状态", required = false, paramType = "query"),
            @ApiImplicitParam(name = "parameterName", value = "参数名称", required = false, paramType = "query"),
            @ApiImplicitParam(name = "parameterProperty", value = "参数属性", required = false, paramType = "query"),
            @ApiImplicitParam(name = "categoryCode", value = "末级分类code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<BasicCarpartsParameterDTO>> queryCarpartsParameterList(@RequestParam(required = false) String parameterType,
                                                                                 @RequestParam(required = false) String parameterStatus,
                                                                                 @RequestParam(required = false) String parameterName,
                                                                                 @RequestParam(required = false) String parameterProperty,
                                                                                 @RequestParam(required = false) String categoryCode,
                                                                                 @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                 @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDtoThriftUtils.resolve(carpartsParameterService.queryCarpartsParameterList(parameterType, parameterStatus,parameterName, parameterProperty,categoryCode, offset, pageSize),BasicCarpartsParameterDTO.class);
    }
}
