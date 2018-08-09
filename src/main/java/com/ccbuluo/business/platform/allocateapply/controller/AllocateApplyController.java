package com.ccbuluo.business.platform.allocateapply.controller;

import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApply;
import com.ccbuluo.core.controller.BaseController;
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
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-08 17:02:58
     */
    @ApiOperation(value = "查询申请单详情", notes = "【张康健】")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "applyNo", value = "申请单号", required = true, paramType = "query")
    public StatusDto<FindAllocateApplyDTO> detail(String applyNo){
        return StatusDto.buildDataSuccessStatusDto(allocateApplyImpl.findDetail(applyNo));
    }
}
