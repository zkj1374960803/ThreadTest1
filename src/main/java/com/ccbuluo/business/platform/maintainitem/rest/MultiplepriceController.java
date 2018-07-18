package com.ccbuluo.business.platform.maintainitem.rest;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMultiplepriceDTO;
import com.ccbuluo.business.platform.maintainitem.service.MultiplepriceService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地区倍数controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 13:51:29
 */
@Api(tags = "工时")
@RestController
@RequestMapping("/platform/multipleprice")
public class MultiplepriceController extends BaseController {

    @Autowired
    private MultiplepriceService multiplepriceService;

    /**
     * 保存地区倍数
     * @param saveBizServiceMultiplepriceDTO 地区倍数dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    @ApiOperation(value = "地区倍数保存", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto saveMultipleprice(@ApiParam(name = "工时对象", value = "传入json格式", required = true)SaveBizServiceMultiplepriceDTO saveBizServiceMultiplepriceDTO) {
        int status = multiplepriceService.save(saveBizServiceMultiplepriceDTO);
        if (status == Constants.FAILURESTATUS) {
            return StatusDto.buildFailure("保存失败！");
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }
}
