package com.ccbuluo.business.platform.carparts.controller;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.carparts.service.CarpartsProductPriceService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.vehiclelease.entity.BizOrderChannelprice;
import com.ccbuluo.core.annotation.validate.ValidateNotNull;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.core.validate.ValidateUtils;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.EditBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 零配件
 * @author weijb
 * @date 2018-06-29 14:18:58
 */
@Api(tags = "零配件管理(平台端)")
@RestController
@RequestMapping("/aftersales/carpartsproduct")
public class CarpartsProductController extends BaseController {

    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    private UserHolder userHolder;
    @Resource
    private BasicCarmodelManageService basicCarmodelManageService;
    @Resource(name = "carpartsProductPriceServiceImpl")
    private CarpartsProductPriceService carpartsProductServiceImpl;
    @Resource
    private CarpartsProductPriceService carpartsProductPriceService;

    /**
     * 添加零配件
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return 是否保存成功
     * @author weijb
     * @date 2018-07-2 08:59:35
     */
    @ApiOperation(value = "添加零配件",notes = "【魏俊标】")
    @PostMapping("/savecarpartsproduct")
    public StatusDto<String> saveCarpartsProduct(@ApiParam(name = "saveBasicCarpartsProductDTO对象", value = "传入json格式", required = true)
                                                 @RequestBody SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO){
        return carpartsProductPriceService.saveCarpartsProduct(saveBasicCarpartsProductDTO);
    }

    /**
     * 编辑零部件
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return StatusDto<String>
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    @ApiOperation(value = "编辑零配件",notes = "【张康健】")
    @PostMapping("/editcarpartsproduct")
    public StatusDto<String> editCarpartsProduct(@ApiParam(name = "saveBasicCarpartsProductDTO", value = "传入json格式", required = true)@RequestBody SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO) {
        saveBasicCarpartsProductDTO.setOperator(userHolder.getLoggedUserId());
        return carpartsProductPriceService.editCarpartsProduct(saveBasicCarpartsProductDTO);
    }
    /**
     * 删除零部件
     * @param carpartsCode 零部件code
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    @ApiOperation(value = "删除零配件",notes = "【魏俊标】")
    @GetMapping("/deletecarpartsproduct/{carpartsCode}")
    @ApiImplicitParam(name = "carpartsCode", value = "零部件Code", required = true, dataType = "String", paramType = "path")
    public StatusDto deleteCarpartsProduct(@PathVariable String carpartsCode) {
        return carpartsProductPriceService.deleteCarpartsProduct(carpartsCode);
    }

    /**
     * 获取零部件详情
     * @param carpartsCode 零部件code
     * @return StatusDto<SaveBasicCarpartsProductDTO>
     * @author weijb
     * @date 2018-07-02 19:31:12
     */
    @ApiOperation(value = "查询零配件详情",notes = "【魏俊标】")
    @ApiImplicitParam(name = "carpartsCode", value = "零部件Code", required = true, dataType = "String", paramType = "path")
    @GetMapping("/findcarpartsproductdetail/{carpartsCode}")
    public StatusDto<SaveBasicCarpartsProductDTO> findCarpartsProductdetail(@PathVariable String carpartsCode){
        return carpartsProductServiceImpl.findCarpartsProductdetail(carpartsCode);
    }

    /**
     *零配件列表分页查询
     * @param keyword 零部件名称
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-02 19:52:44
     */
    @ApiOperation(value = "零部件记录列表",notes = "【张康健】")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "零部件编号或名称", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductList(@RequestParam(required = false) String keyword,
                                                                             @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                             @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return carpartsProductServiceImpl.queryCarpartsProductList(keyword, offset, pageSize);

    }

    /**
     * 根据分类code查询零配件list（不分页）
     * @param categoryCode 零部件分类code
     * @author weijb
     * @date 2018-08-02 09:59:51
     */
    @ApiOperation(value = "根据分类code查询零配件list（不分页）",notes = "【魏俊标】")
    @ApiImplicitParam(name = "categoryCode", value = "零部件分类code", required = false, paramType = "query")
    @GetMapping("/querycarpartsproductlistbycategorycode")
    public StatusDto<List<BasicCarpartsProductDTO>> queryCarpartsProductListByCategoryCode(@RequestParam(required = false) String categoryCode) {
        return StatusDto.buildDataSuccessStatusDto(carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode));
    }
    
    /**
     * 设置零配件的价格
     * @param relProductPrice 零配件实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-09-06 11:08:27
     */
    @ApiOperation(value = "设置零配件和物料的价格",notes = "【张康健】")
    @PostMapping("/setprice")
    public StatusDto<String> setPrice(@RequestBody @ApiParam(name = "relProductPrice", value = "传入json格式", required = true) List<RelProductPrice> relProductPrice){
        ValidateUtils.validate(relProductPrice, null);
        carpartsProductServiceImpl.saveProductPrice(relProductPrice);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询零配件价格列表
     * @param queryCarpartsProductDTO 查询条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 状态dto
     * @author zhangkangjian
     * @date 2018-09-06 11:12:44
     */
    @ApiOperation(value = "查询零配件价格列表",notes = "【张康健】")
    @GetMapping("/querycarpartsproductpricelist")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "零配件编号/名称", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductPriceList(@ApiIgnore QueryCarpartsProductDTO queryCarpartsProductDTO) {
        return carpartsProductServiceImpl.queryCarpartsProductPriceList(queryCarpartsProductDTO);
    }

    /**
     * 查询当前机构下所有的零配件（不限制数量，不限制是否设置价格）
     * @param queryCarpartsProductDTO 查询的条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 分页的零配件列表
     * @author zhangkangjian
     * @date 2018-11-05 15:40:42
     */
    @ApiOperation(value = "查询当前机构下所有的零配件（不限制数量，不限制是否设置价格）",notes = "【张康健】")
    @PostMapping("/queryallserviceproductlist")

    public StatusDto<Page<BasicCarpartsProductDTO>> queryAllServiceProductList(@ApiParam(name = "queryCarpartsProductDTO", value = "查询的条件", required = true) @RequestBody QueryCarpartsProductDTO queryCarpartsProductDTO){
        return carpartsProductServiceImpl.queryAllServiceProductList(queryCarpartsProductDTO);
    }

    /**
     * 根据查询所有的零配件
     * @param keyword 查询条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>>
     * @author zhangkangjian
     * @date 2018-11-13 10:02:36
     */
    @ApiOperation(value = "查询零配件信息",notes = "【张康健】")
    @PostMapping("/querycarparts")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "零配件编号/名称", required = false, paramType = "query"),
        @ApiImplicitParam(name = "priceLevelEnum", value = "枚举值", required = true, paramType = "query"),
    })
    public StatusDto<List<BasicCarpartsProductDTO>> queryCarpartsByCode(String keyword, @ValidateNotNull RelProductPrice.PriceLevelEnum priceLevelEnum){
        return carpartsProductServiceImpl.queryCarparts(keyword, priceLevelEnum);
    }

    /**
     * 导入零配件
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-11-16 11:44:02
     */
    @ApiOperation(value = "导入零配件",notes = "【张康健】")
    @PostMapping("/importcarparts")
    public StatusDto<String> importCarparts(MultipartFile multipartFile) throws Exception {
        return carpartsProductServiceImpl.importCarparts(multipartFile);
    }


    /**
     * 导出零配件
     * @author liuduo
     * @date 2018-11-19 09:44:02
     */
    @ApiOperation(value = "导出零配件",notes = "【刘铎】")
    @PostMapping("/exportcarparts")
    public StatusDto exportCarparts()  throws IOException {
        return carpartsProductServiceImpl.exportCarparts();
    }

}
