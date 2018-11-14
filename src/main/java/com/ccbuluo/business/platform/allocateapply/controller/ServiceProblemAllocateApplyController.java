package com.ccbuluo.business.platform.allocateapply.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProblemAllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.allocateapply.service.ProblemAllocateApply;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  问题件申请管理 controller
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-15 18:51:51
 */
@Api(tags = "问题件申请管理（服务中心端）")
@RestController
@RequestMapping("/platform/serviceproblemallocateapply")
public class ServiceProblemAllocateApplyController extends BaseController {
    @Resource
    private ProblemAllocateApply problemAllocateApply;


    /**
     * 物料问题件申请列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
            */
    @ApiOperation(value = "物料问题件申请列表",notes = "【魏俊标】")
    @GetMapping("/applylist")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyType", value = "申请类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
            @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<QueryAllocateApplyListDTO>> queryProblemApplyList(@RequestParam(required = false) String applyType,
                                                                            @RequestParam(required = false) String applyStatus,
                                                                            @RequestParam(required = false) String applyNo,
                                                                            @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(problemAllocateApply.querySelfProblemApplyList(Constants.PRODUCT_TYPE_EQUIPMENT, applyType, applyStatus, applyNo, offset, pageSize));
    }


    /**
     * 零配件问题件申请列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    @ApiOperation(value = "零配件问题件申请列表",notes = "【魏俊标】")
    @GetMapping("/applyfittingslist")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyType", value = "申请类型", required = false, paramType = "query"),
            @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
            @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<QueryAllocateApplyListDTO>> queryProblemApplyfittingsList(@RequestParam(required = false) String applyType,
                                                                                        @RequestParam(required = false) String applyStatus,
                                                                                        @RequestParam(required = false) String applyNo,
                                                                                        @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                                        @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(problemAllocateApply.querySelfProblemApplyList(Constants.PRODUCT_TYPE_FITTINGS,applyType, applyStatus, applyNo, offset, pageSize));
    }

    /**
     * 查询退换货申请单详情
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 20:02:58
     */
    @ApiOperation(value = "查询退换货申请单详情（申请）", notes = "【魏俊标】")
    @GetMapping("/problemApplydetail/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> getProblemdetailApplyDetail(@PathVariable @ValidateNotNull(message = "applyNo(申请applyNo)不能为空") String applyNo){
        return StatusDto.buildDataSuccessStatusDto(problemAllocateApply.getProblemdetailDetail(applyNo));
    }

}
