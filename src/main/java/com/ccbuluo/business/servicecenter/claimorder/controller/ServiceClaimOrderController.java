package com.ccbuluo.business.servicecenter.claimorder.controller;

import com.ccbuluo.business.constants.MyGroup;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 索赔单
 * @author zhangkangjian
 * @date 2018-09-08 10:13:49
 */
@Api(tags = "索赔单(服务中心端)")
@RestController
@RequestMapping("/servicecenter/claimorder")
public class ServiceClaimOrderController {

    @Resource(name = "claimOrderServiceImpl")
    private ClaimOrderService claimOrderServiceImpl;

    /**
     * 提交索赔单
     * @param bizServiceClaimorder 索赔单实体
     * @return StatusDto<String> 状态DTO
     * @author zhangkangjian
     * @date 2018-09-08 14:17:49
     */
    @ApiOperation(value = "提交索赔单", notes = "【张康健】")
    @PostMapping("/conmmitclaimorder")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "trackingNo", value = "物流单号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "refundAdress", value = "物流地址", required = true, paramType = "query"),
    })
    public StatusDto<String> conmmitClaimOrder(@ApiIgnore @ValidateGroup(MyGroup.Add.class) BizServiceClaimorder bizServiceClaimorder){
        claimOrderServiceImpl.updateClaimOrder(bizServiceClaimorder);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询索赔单的详情
     * @return StatusDto<Map<String, Object>>
     * @author zhangkangjian
     * @date 2018-09-08 14:35:15
     */
    @ApiOperation(value = "查询索赔单的详情", notes = "【张康健】")
    @GetMapping("/findclaimorderdetail")
    @ApiImplicitParam(name = "claimOrdno", value = "索赔单号", required = true, paramType = "query")
    public StatusDto<BizServiceClaimorder> findClaimOrderDetail(@ApiIgnore @ValidateGroup(MyGroup.Select.class) BizServiceClaimorder bizServiceClaimorder){
        BizServiceClaimorder claimOrderDetail = claimOrderServiceImpl.findClaimOrderDetail(bizServiceClaimorder);
        return StatusDto.buildDataSuccessStatusDto(claimOrderDetail);
    }

    /**
     * 查询索赔单的列表
     * @param keyword 索赔单号/维修单号/车牌号
     * @param offset 索赔单状态
     * @param pageSize 每页显示的数量
     * @return 索赔单的列表
     * @author zhangkangjian
     * @date 2018-09-08 16:12:24
     */
    @ApiOperation(value = "查询索赔单的列表", notes = "【张康健】")
    @GetMapping("/queryclaimorderlist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "索赔单号/维修单号/车牌号", required = false, paramType = "query"),
        @ApiImplicitParam(name = "docStatus", value = "索赔单状态", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query"),
    })
    public StatusDto<Page<QueryClaimorderListDTO>> queryClaimorderList(String docStatus,
                                                                       String keyword,
                                                                       @RequestParam(defaultValue = "0") int offset,
                                                                       @RequestParam(defaultValue = "10") int pageSize){
        return claimOrderServiceImpl.queryClaimorderList(null, keyword, docStatus, offset, pageSize);
    }

    /**
     * 查询维修单各种状态数据的数量
     * @return StatusDto<Map<String, Long>>
     * @author zhangkangjian
     * @date 2018-09-19 17:02:50
     */
    @ApiOperation(value = "查询维修单各种状态数据的数量", notes = "【张康健】")
    @GetMapping("/countclaimorderstatusnum")
    public StatusDto<Map<String, Long>> countClaimorderStatusNum(){
        return StatusDto.buildDataSuccessStatusDto(claimOrderServiceImpl.countClaimorderStatusNum());
    }



}