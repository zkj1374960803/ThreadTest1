package com.ccbuluo.business.platform.allocateapply.controller;

import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApply;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:10:24
 */
@RestController
@RequestMapping("/platform/allocateapply")
@Api(tags = "申请管理")
public class AllocateApplyController extends BaseController {
    @Resource(name = "allocateApplyImpl")
    private AllocateApply allocateApplyImpl;

    /**
     * 创建物料或者零配件申请
     * @param bizAllocateApply 申请单实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @ApiOperation(value = "创建物料或者零配件申请", notes = "【张康健】")
    @PostMapping("/create")
    public StatusDto<String> createAllocateApply(@ApiParam(name = "bizAllocateApply", value = "创建申请json", required = true) @RequestBody BizAllocateApply bizAllocateApply){
        allocateApplyImpl.createAllocateApply(bizAllocateApply);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return StatusDto<FindAllocateApplyDTO>
     * @author zhangkangjian
     * @date 2018-08-08 17:02:58
     */
    @ApiOperation(value = "查询申请单详情", notes = "【张康健】")
    @GetMapping("/detail/{applyNo}")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "path")
    public StatusDto<FindAllocateApplyDTO> detail(@PathVariable String applyNo){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyImpl.findDetail(applyNo));
    }

    /**
     * 查询申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @ApiOperation(value = "查询申请列表", notes = "【张康健】")
    @GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processType", value = "申请类型", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> list(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyImpl.findApplyList(processType, applyStatus, applyNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 查询处理申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @ApiOperation(value = "查询处理申请列表", notes = "【张康健】")
    @GetMapping("/processlist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processType", value = "申请类型", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyStatus", value = "申请状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "applyNo", value = "申请单号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryAllocateApplyListDTO>> processList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<QueryAllocateApplyListDTO> page = allocateApplyImpl.findProcessApplyList(processType, applyStatus, applyNo, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

}
