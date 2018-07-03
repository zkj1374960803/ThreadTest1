package com.ccbuluo.business.platform.label.rest;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.business.platform.label.service.LabelService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签controller
 * @author zhangkangjian
 * @date 2018-07-02 16:43:46
 */
@Api(tags = "标签(平台端)")
@RestController
@RequestMapping("/label/label")
public class ServiceLabelController extends BaseController {

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
    @ApiOperation(value = "添加标签",notes = "【张康健】")
    @PostMapping("/createLabel")
    public StatusDto createLabel(BizServiceLabel bizServiceLabel){
        labelServiceImpl.createLabel(bizServiceLabel);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 删除标签
     * @param id 标签id
     * @author zhangkangjian
     * @date 2018-07-03 11:33:04
     */
    @ApiOperation(value = "删除标签",notes = "【张康健】")
    @PostMapping("/deleteLabel/{id}")
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


}
