package com.ccbuluo.business.platform.maintain.controller;

import com.ccbuluo.business.entity.BizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SaveBizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SearchBizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.service.MainTainService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 保养controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-10-30 10:06:32
 */
@Api(tags = "保养")
@RestController
@RequestMapping("/platform/maintain")
public class MainTainController extends BaseController {

    @Autowired
    private MainTainService mainTainService;

    /**
     * 保存保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    @ApiOperation(value = "保养套餐新增", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveMainTain(@ApiParam(name = "保养套餐信息", value = "传入json格式", required = true)@RequestBody @ValidateGroup SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        return mainTainService.saveMainTain(bizServiceMaintaingroup);
    }


    /**
     * 编辑保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    @ApiOperation(value = "编辑保养套餐", notes = "【刘铎】")
    @PostMapping("/edit")
    public StatusDto editMainTain(@ApiParam(name = "保养套餐信息", value = "传入json格式", required = true)@RequestBody @ValidateGroup SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        return mainTainService.editMainTain(bizServiceMaintaingroup);
    }

    /**
     * 修改保修套餐状态
     * @param groupNo 保修套餐编号
     * @param groupStatus 保修套餐状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-10-30 14:20:22
     */
    @ApiOperation(value = "更改保修状态", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "groupNo", value = "保修套餐编号",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "groupStatus", value = "保修套餐状态(ENABLE或DISABLE)",  required = true, paramType = "query")})
    @PostMapping("/editstatus")
    public StatusDto editStatus(@RequestParam @ValidateNotBlank(message = "保修套餐编号不能为空") String groupNo,
                                @RequestParam @ValidateNotBlank(message = "保修套餐状态不能为空") String groupStatus) {
        return mainTainService.editStatus(groupNo, groupStatus);
    }


    /**
     * 查询保养套餐列表
     * @param groupStatus 保养套餐状态
     * @param keyword 保养套餐编号或名字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 保养列表
     * @author liuduo
     * @date 2018-10-30 14:32:21
     */
    @ApiOperation(value = "查询保养套餐列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "groupStatus", value = "保修套餐状态(ENABLE或DISABLE)",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "保养套餐编号或名字",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数",  required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页数",  required = true, paramType = "query", dataType = "int")})
    @GetMapping("/list")
    public StatusDto<Page<BizServiceMaintaingroup>> list(@RequestParam(required = false) String groupStatus,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(defaultValue = "0") Integer offset,
                                                         @RequestParam(defaultValue = "10") Integer pageSize ) {

        return mainTainService.list(groupStatus, keyword, offset, pageSize);
    }


    /**
     * 根据保养套餐编号查询保养套餐详情
     * @param groupNo 保养套餐编号
     * @return 保养详情
     * @author liuduo
     * @date 2018-10-30 15:16:26
     */
    @ApiOperation(value = "查询保养套餐详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "groupNo", value = "保修套餐编号",  required = true, paramType = "query")
    @GetMapping("/getdetailbygroupno")
    public StatusDto<SearchBizServiceMaintaingroup> getDetailByGroupNo(@RequestParam @ValidateNotBlank(message = "保修套餐编号不能为空") String groupNo) {
        return mainTainService.getDetailByGroupNo(groupNo);
    }

}
