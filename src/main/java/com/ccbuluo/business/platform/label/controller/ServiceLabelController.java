package com.ccbuluo.business.platform.label.controller;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.business.platform.label.service.LabelService;
import com.ccbuluo.business.platform.multi.DemoService;
import com.ccbuluo.business.platform.multi.MultiExecutor;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 标签controller
 * @author zhangkangjian
 * @version v1.0.0
 * @date 2018-07-02 16:43:46
 */
@Api(tags = "标签(平台端)")
@RestController
@RequestMapping("/label/label")
public class ServiceLabelController extends BaseController {

    @Autowired
    private DemoService demoService;
    @Resource(name = "labelServiceImpl")
    private LabelService labelServiceImpl;

    /**
     * 新增标签
     * @param bizServiceLabel 标签实体
     * @exception
     * @return StatusDto 状态DTO
     * @author zhangkangjian
     * @date 2018-07-02 16:53:50
     */
    @ApiOperation(value = "添加标签(成功返回标签信息)",notes = "【张康健】")
    @PostMapping("/createlabel")
    public StatusDto<ListLabelDTO> createLabel(BizServiceLabel bizServiceLabel){
        return StatusDto.buildDataSuccessStatusDto(labelServiceImpl.createLabel(bizServiceLabel));
    }

    /**
     * 删除标签
     * @param id 标签id
     * @author zhangkangjian
     * @date 2018-07-03 11:33:04
     */
    @ApiOperation(value = "删除标签",notes = "【张康健】")
    @PostMapping("/deletelabel/{id}")
    @ApiImplicitParam(name = "id", value = "标签的id", required = true, paramType = "path")
    public StatusDto deleteLabel(@PathVariable @ApiIgnore Long id){
        labelServiceImpl.deleteLabel(id);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询标签列表
     * @return StatusDto<List<ListLabelDTO>> 状态DTO
     * @author zhangkangjian
     * @date 2018-07-03 11:40:29
     */
    @ApiOperation(value = "查询标签列表（下拉框）",notes = "【张康健】")
    @PostMapping("/findlistlabel")
    public StatusDto<List<ListLabelDTO>> findlistlabel(){
        return StatusDto.buildDataSuccessStatusDto(labelServiceImpl.findListLabel());
    }

    /**
     * 编辑标签
     * @param id 标签id
     * @param labelName 标签名称
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-07-03 11:40:29
     */
    @ApiOperation(value = "编辑标签",notes = "【张康健】")
    @PostMapping("/editlabel")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "标签的id", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "labelName", value = "标签名称", required = true, paramType = "query", dataType = "String")
    })
    public StatusDto<String> editlabel(Long id, String labelName){
        labelServiceImpl.editlabel(id, labelName);
        return StatusDto.buildSuccessStatusDto();
    }



    @ApiOperation(value = "编辑标签",notes = "【张康健】")
    @PostMapping("/test")

    public StatusDto<String> test() throws ExecutionException, InterruptedException {
        ArrayList<BizServiceLabel> noteCheckBalances = new ArrayList<BizServiceLabel>();
        for (int i = 0; i < 10; i++) {

            if(i == 2){
                noteCheckBalances.add(new BizServiceLabel("李二" + "【"+ i +"】"));
            }else if(i == 1){
                noteCheckBalances.add(new BizServiceLabel("王一" + "【"+ i +"】"));
            }else {
                noteCheckBalances.add(new BizServiceLabel("张零" + "【"+ i +"】"));
            }
        }
        String name = Thread.currentThread().getName();
        System.out.println(name);
        MultiExecutor.exec(demoService, noteCheckBalances,3);
        return StatusDto.buildSuccessStatusDto();
    }


}
