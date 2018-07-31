package com.ccbuluo.business.platform.maintainitem.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.business.platform.maintainitem.service.MultiplepriceService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 地区倍数controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 13:51:29
 */
@Api(tags = "地区倍数")
@RestController
@RequestMapping("/platform/multipleprice")
public class MultiplepriceController extends BaseController {

    @Autowired
    private MultiplepriceService multiplepriceService;

    /**
     * 查询地区倍数列表
     * @param maintainitemCode 服务项code
     * @param provinceCode 省code
     * @param cityCode 市code
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 地区倍数列表
     * @author liuduo
     * @date 2018-07-18 15:35:18
     */
    @ApiOperation(value = "地区倍数列表查询", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "maintainitemCode", value = "服务项code", paramType = "query", required = true),
        @ApiImplicitParam(name = "provinceCode", value = "省code", paramType = "query", required = false),
        @ApiImplicitParam(name = "cityCode", value = "市code", paramType = "query", required = false),
        @ApiImplicitParam(name = "offset", value = "起始数", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "pagesize", value = "每页数", paramType = "query", required = true, dataType = "int"),})
    @GetMapping("/queryList")
    public StatusDto<Page<BizServiceMultipleprice>> queryList(@RequestParam String maintainitemCode,
                                                              @RequestParam(required = false) String provinceCode,
                                                              @RequestParam(required = false) String cityCode,
                                                              @RequestParam(defaultValue = "0") Integer offset,
                                                              @RequestParam(defaultValue = "10") Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(multiplepriceService.queryList(maintainitemCode, provinceCode, cityCode, offset, pagesize));
    }


    /**
     * 根据id删除地区倍数
     * @param id 地区倍数id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-18 16:14:46
     */
    @ApiOperation(value = "地区倍数删除", notes = "【刘铎】")
    @ApiImplicitParam(name = "id", value = "地区倍数id", paramType = "query", required = true, dataType = "int")
    @DeleteMapping("/deletebyid")
    public StatusDto deleteById(@RequestParam Long id) {
        int status = multiplepriceService.deleteById(id);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("删除失败！");
        }
        return StatusDto.buildSuccessStatusDto("删除成功！");
    }
}
