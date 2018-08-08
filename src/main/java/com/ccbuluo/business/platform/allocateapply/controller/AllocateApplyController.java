package com.ccbuluo.business.platform.allocateapply.controller;

import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApply;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:10:24
 */
@RestController
@RequestMapping("/platform/allocateapply")
@Api(tags = "品牌")
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
    public StatusDto<String> createAllocateApply(@ApiParam(name = "bizAllocateApply", value = "创建申请json", required = true) BizAllocateApply bizAllocateApply){
        allocateApplyImpl.createAllocateApply(bizAllocateApply);
        return null;
    }
}
