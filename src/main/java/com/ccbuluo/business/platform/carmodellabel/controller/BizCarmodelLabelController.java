package com.ccbuluo.business.platform.carmodellabel.controller;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizCarmodelLabel;
import com.ccbuluo.business.platform.carmodellabel.dto.SearchBizCarmodelLabelDTO;
import com.ccbuluo.business.platform.carmodellabel.service.BizCarmodelLabelService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 维修车辆基本信息controller
 * @author weijb
 * @date 2018-07-18 14:59:51
 * @version v 1.0.0
 */
@RestController
@RequestMapping("/afterSales/carmodellabel")
@Api(tags = "车型标签管理")
public class BizCarmodelLabelController extends BaseController {

    @Resource
    private BizCarmodelLabelService bizCarmodelLabelService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;

    /**
     * 车型标签新增
     * @param bizCarmodelLabel 车型标签信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "新增车型标签", notes = "【weijb】")
    @PostMapping("/create")
    public StatusDto create(@ApiParam(name = "bizCarmodelLabel对象", value = "传入json格式", required = true) BizCarmodelLabel bizCarmodelLabel) {
        // 生成编码
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FD);
        //获取code失败
        if(!Constants.SUCCESS_CODE.equals(stringStatusDto.getCode())){
            return stringStatusDto;
        }
        bizCarmodelLabel.setLabelCode(stringStatusDto.getData());
        return bizCarmodelLabelService.saveCarmodelLabel(bizCarmodelLabel);
    }

    /**
     * 车型标签编辑
     * @param bizCarmodelLabel 车型标签信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "编辑车型标签", notes = "【weijb】")
    @PostMapping("/edit")
    public StatusDto edit(@ApiParam(name = "bizServiceMaintaincar对象", value = "传入json格式", required = true) BizCarmodelLabel bizCarmodelLabel) {
        return bizCarmodelLabelService.editCarmodelLabel(bizCarmodelLabel);
    }



    /**
     * 根据标签id查询车型标签详情
     * @param labelCode 车辆id
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "根据标签id查询车型标签详情", notes = "根据标签id查询车型标签详情")
    @ApiImplicitParam(name = "labelCode", value = "车型标签labelCode", required = true, paramType = "query")
    @GetMapping("/queryCarmodelLabelBylabelCode")
    public StatusDto queryCarmodelLabelBylabelId(String labelCode) throws TException {
        return StatusDto.buildDataSuccessStatusDto(bizCarmodelLabelService.queryCarmodelLabelBylabelCode(labelCode));
    }

    /**
     * 删除车型标签
     * @param labelCode 车型标签id
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "删除车型标签",notes = "【魏俊标】")
    @GetMapping("/delete/{labelCode}")
    @ApiImplicitParam(name = "labelCode", value = "车型标签labelCode", required = true, paramType = "path")
    public StatusDto delete(@PathVariable String labelCode) {
        bizCarmodelLabelService.deleteCarcoreInfoBylabelCode(labelCode);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 车型标签列表分页查询
     * @param Keyword (车车型标签名称)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    @ApiOperation(value = "车型标签列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "Keyword", value = "关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<SearchBizCarmodelLabelDTO>> queryCarmodelLabelList(@RequestParam(required = false) String Keyword,
                                                                           @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(bizCarmodelLabelService.queryCarmodelLabelList(Keyword, offset, pageSize));
    }

}
