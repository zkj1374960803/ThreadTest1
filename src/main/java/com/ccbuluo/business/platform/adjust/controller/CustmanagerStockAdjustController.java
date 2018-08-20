package com.ccbuluo.business.platform.adjust.controller;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.adjust.dto.SaveBizStockAdjustDTO;
import com.ccbuluo.business.platform.adjust.dto.SearchStockAdjustListDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustDetailDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.business.platform.adjust.service.StockAdjustService;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.category.dto.QueryCategoryListDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘库controller
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-14 16:14:25
 */
@Api(tags = "盘库（客户经理端）")
@RestController
@RequestMapping("/adjust/custmanagerstockadjust")
public class CustmanagerStockAdjustController extends BaseController {

    @Autowired
    private StockAdjustService stockAdjustService;
    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsProductService carpartsProductService;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;

    /**
     * 查询物料类型集合
     * @return 物料类型集合
     * @author liuduo
     * @date 2018-08-14 16:36:23
     */
    @ApiOperation(value = "查询物料类型", notes = "【刘铎】")
    @GetMapping("/queryequipmenttype")
    public StatusDto<List<BizServiceEquiptype>> queryEquipmentType() {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.queryEquipmentType());
    }


    /**
     * 根据物料类型id查询物料
     * @param equipTypeId 物料类型id
     * @return 物料
     * @author liuduo
     * @date 2018-08-14 16:41:20
     */
    @ApiOperation(value = "根据物料类型查询物料", notes = "【刘铎】")
    @ApiImplicitParam(name = "equipTypeId", value = "物料类型id",  required = true, paramType = "query", dataType = "int")
    @GetMapping("/queryequipmentbytype")
    public StatusDto<List<DetailBizServiceEquipmentDTO>> queryEquipmentByType(@RequestParam Long equipTypeId) {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.queryEquipmentByType(equipTypeId));
    }


    /**
     * 查询新增物料盘库时用的列表
     * @param equipTypeId 物料类型id
     * @param equipmentcode 物料code
     * @return 新增物料盘库时用的列表
     * @author liuduo
     * @date 2018-08-14 17:43:53
     */
    @ApiOperation(value = "新增盘库时用的列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "equipTypeId", value = "物料类型id",  required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "equipmentcode", value = "物料编号",  required = false, paramType = "query")})
    @GetMapping("/queryadjustlist")
    public StatusDto<List<StockAdjustListDTO>> queryAdjustList(@RequestParam(required = false) Long equipTypeId,
                                                               @RequestParam(required = false) String equipmentcode) {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.queryAdjustList(equipTypeId, equipmentcode));
    }


    /**
     * 保存盘库
     * @param saveBizStockAdjustDTO 盘库实体
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-08-14 19:17:37
     */
    @ApiOperation(value = "保存盘库", notes = "【刘铎】")
    @PostMapping("/save")
    public StatusDto save(@ApiParam(name = "盘库详情", value = "传入json格式", required = true)@RequestBody SaveBizStockAdjustDTO saveBizStockAdjustDTO) {
        return stockAdjustService.save(saveBizStockAdjustDTO);
    }


    /**
     * 根据零配件分类code查询零配件
     * @param categoryCode 零配件分类code
     * @return 零配件类型集合
     * @author liuduo
     * @date 2018-08-15 09:16:23
     */
    @ApiOperation(value = "根据零配件分类code查询零配件", notes = "【刘铎】")
    @ApiImplicitParam(name = "categoryCode", value = "零配件分类code", required = false, paramType = "query")
    @GetMapping("/queryproductbycode")
    public StatusDto<List<BasicCarpartsProductDTO>> queryProductByCode(@RequestParam(required = false) String categoryCode) {
        return StatusDto.buildDataSuccessStatusDto(carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode));
    }


    /**
     * 新增盘库时用的列表中根据分类查询列表
     * @param categoryCode 零配件分类code
     * @param productCode 商品code
     * @return 新增零配件盘库时用的列表
     * @author liuduo
     * @date 2018-08-15 09:23:53
     */
    @ApiOperation(value = "新增盘库时用的列表中根据分类查询列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryCode", value = "零配件分类code",  required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "productCode", value = "商品编号",  required = false, paramType = "query")})
    @GetMapping("/queryadjustlistbycategorycode")
    public StatusDto<List<StockAdjustListDTO>> queryAdjustListByCategoryCode(@RequestParam(required = false) String categoryCode,
                                                               @RequestParam(required = false) String productCode) {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.queryAdjustListByCategoryCode(categoryCode, productCode));
    }

    /**
     * 查询零配件类型集合
     * @return 零配件类型集合
     * @author liuduo
     * @date 2018-08-14 16:36:23
     */
    @ApiOperation(value = "查询零配件类型集合", notes = "【刘铎】")
    @GetMapping("/querycategory")
    public StatusDto<List<QueryCategoryListDTO>> queryCategory() {
        StatusDtoThriftList<QueryCategoryListDTO> queryCategoryListDTOStatusDtoThriftList = carpartsCategoryService.queryCategoryList();
        return StatusDtoThriftUtils.resolve(queryCategoryListDTOStatusDtoThriftList, QueryCategoryListDTO.class);
    }


    /**
     * 查询盘库单列表
     * @param adjustResult 盘库结果
     * @param adjustSource 盘库单来源
     * @param keyWord 关键字（盘库单号/服务中心/客户经理）
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 盘库单列表
     * @author liuduo
     * @date 2018-08-15 11:03:46
     */
    @ApiOperation(value = "盘库单列表", notes = "【刘铎】")
    @ApiImplicitParams({@ApiImplicitParam(name = "adjustResult", value = "盘库结果（有差异、无差异）",  required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "adjustSource", value = "盘库单来源(传code)",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "keyWord", value = "关键字（盘库单号/服务中心/客户经理）",  required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始数",  required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pagesize", value = "每页数",  required = true, paramType = "query", dataType = "int")})
    @GetMapping("/queryadjuststocklist")
    public StatusDto<Page<SearchStockAdjustListDTO>> queryAdjustStockList(@RequestParam(required = false) Integer adjustResult,
                                                                          @RequestParam(required = false) String adjustSource,
                                                                          @RequestParam(required = false) String keyWord,
                                                                          @RequestParam(defaultValue = "0") Integer offset,
                                                                          @RequestParam(defaultValue = "10") Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.queryAdjustStockList(adjustResult, adjustSource, keyWord, offset, pagesize));
    }


    /**
     * 根据盘库单号查询盘库详情
     * @param adjustNo 盘库单号
     * @return 盘库详情
     * @author liuduo
     * @date 2018-08-15 14:37:37
     */
    @ApiOperation(value = "根据盘库单号查询盘库详情", notes = "【刘铎】")
    @ApiImplicitParam(name = "adjustNo", value = "盘库单code", required = true, paramType = "query")
    @GetMapping("/getbyadjustno")
    public StatusDto<StockAdjustDetailDTO> getByAdjustNo(@RequestParam String adjustNo) {
        return StatusDto.buildDataSuccessStatusDto(stockAdjustService.getByAdjustNo(adjustNo));
    }
}
