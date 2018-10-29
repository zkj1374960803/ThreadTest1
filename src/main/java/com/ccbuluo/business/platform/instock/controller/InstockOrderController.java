package com.ccbuluo.business.platform.instock.controller;

import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
import com.ccbuluo.business.platform.instock.dto.SaveInstockorderDetailDTO;
import com.ccbuluo.business.platform.instock.service.InstockOrderService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.validate.ValidateUtils;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库单controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:03:59
 */
@Api(tags = "入库单")
@RestController
@RequestMapping("/platform/instockorder")
public class InstockOrderController extends BaseController {

    @Autowired
    private InstockOrderService instockOrderService;

    /**
     * 查询申请单号集合
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @ApiOperation(value = "申请单号查询", notes = "【刘铎】")
    @ApiImplicitParam(name = "productType", value = "商品类型(物料或者备件)",  required = true, paramType = "query")
    @GetMapping("/queryapplyno")
    public StatusDto<List<String>> queryApplyNo(@RequestParam @ValidateNotBlank(message = "productType(商品类型(物料或者备件))不能为空") String productType) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.queryApplyNo(productType));
    }


    /**
     * 保存入库单
     * @param saveInstockorderDetailDTO 入库单信息
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @ApiOperation(value = "入库单新增", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto<String> saveInstockOrder(@ApiParam(name = "入库单详单集合", value = "传入json格式", required = true)@RequestBody @ValidateGroup SaveInstockorderDetailDTO saveInstockorderDetailDTO) {
        ValidateUtils.validate(saveInstockorderDetailDTO.getBizInstockorderDetailList(), null);
        return instockOrderService.saveInstockOrder(saveInstockorderDetailDTO.getApplyNo(), saveInstockorderDetailDTO.getInRepositoryNo(), saveInstockorderDetailDTO.getBizInstockorderDetailList());
    }


    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    @ApiOperation(value = "根据申请单号查询入库计划", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyNo", value = "申请单号",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "inRepositoryNo", value = "仓库编号",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "productType", value = "商品类型（物料或者备件）",  required = true, paramType = "query")})
    @GetMapping("/queryinstockplan")
    public StatusDto<List<BizInstockplanDetail>> queryInstockplan(@RequestParam @ValidateNotBlank(message = "applyNo(申请单号)不能为空") String applyNo,
                                                                  @RequestParam @ValidateNotBlank(message = "inRepositoryNo(仓库编号)不能为空") String inRepositoryNo,
                                                                  @RequestParam @ValidateNotBlank(message = "productType(商品类型（物料或者备件）)不能为空") String productType) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.queryInstockplan(applyNo, inRepositoryNo, productType));
    }

    /**
     * 分页查询入库单列表
     * @param productType 商品类型
     * @param instockType 入库类型
     * @param instockNo 入库单号
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    @ApiOperation(value = "入库单列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "productType", value = "商品类型(物料或者备件)",  required = true, paramType = "query"),
        @ApiImplicitParam(name = "instockType", value = "入库类型",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "instockNo", value = "入库单单号",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数",  required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页数",  required = true, paramType = "query", dataType = "int")})
    @GetMapping("/queryinstocklist")
    public StatusDto<Page<BizInstockOrder>> queryInstockList(@RequestParam @ValidateNotBlank(message = "productType(商品类型(物料或者备件))不能为空") String productType,
                                                             @RequestParam(required = false) String instockType,
                                                             @RequestParam(required = false) String instockNo,
                                                             @RequestParam(defaultValue = "0") Integer offset,
                                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.queryInstockList(productType, instockType, instockNo, offset, pageSize));
    }


    /**
     * 根据入库单号查询入库单详情
     * @param instockNo 入库单号
     * @return 入库单详情
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @ApiOperation(value = "入库单详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "instockNo", value = "入库单单号",  required = true, paramType = "query")
    @GetMapping("/getbyinstockno")
    public StatusDto<BizInstockOrderDTO> getByInstockNo(@RequestParam @ValidateNotBlank(message = "instockNo(入库单单号)不能为空") String instockNo) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.getByInstockNo(instockNo));
    }

    /**
     * 根据申请单号查询入库仓库
     * @param applyNo 申请单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @ApiOperation(value = "入库仓库", notes = "【刘铎】")
    @ApiImplicitParam(name = "applyNo", value = "申请单号",  required = true, paramType = "query")
    @GetMapping("/getbyapplyno")
    public StatusDto<List<String>> getByApplyNo(@RequestParam @ValidateNotBlank(message = "applyNo(申请单号)不能为空") String applyNo) {
        return StatusDto.buildDataSuccessStatusDto(instockOrderService.getByApplyNo(applyNo));
    }
}
