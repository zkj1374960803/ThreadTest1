package com.ccbuluo.business.platform.carparts.controller;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.EditBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 零配件
 * @author weijb
 * @date 2018-06-29 14:18:58
 */
@Api(tags = "零配件管理(平台端)")
@RestController
@RequestMapping("/afterSales/carpartsProduct")
public class CarpartsProductController extends BaseController {

    @ThriftRPCClient("BasicMerchandiseSer")
    CarpartsProductService carpartsProductService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    UserHolder userHolder;

    // 编码前缀
    private static final String PREFIX = "FP";
    // 列名
    private static final String FIELDNAME = "carparts_code";
    // 表名
    private static final String TRABLENAME = "basic_carparts_product";
    // 是否有随机码
    private static final Boolean ISTANDOMCODE = false;

    /**
     * 添加零配件
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return 是否保存成功
     * @author weijb
     * @date 2018-07-2 08:59:35
     */
    @ApiOperation(value = "添加零配件",notes = "【魏俊标】")
    @PostMapping("/savecarpartsproduct")
    public StatusDto<String> saveCarpartsProduct(@ApiParam(name = "saveBasicCarpartsProductDTO对象", value = "传入json格式", required = true)@RequestBody SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO){
        // 生成编码
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FP);
        //获取code失败
        if(!Constants.SUCCESS_CODE.equals(stringStatusDto.getCode())){
            return stringStatusDto;
        }
        saveBasicCarpartsProductDTO.setCarpartsCode(stringStatusDto.getData());
        saveBasicCarpartsProductDTO.setCreator(userHolder.getLoggedUserId());
        return carpartsProductService.saveCarpartsProduct(saveBasicCarpartsProductDTO);
    }
    /**
     * 编辑零部件
     * @param saveBasicCarpartsProductDTO
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    @ApiOperation(value = "编辑零配件",notes = "【魏俊标】")
    @PostMapping("/editcarpartsproduct")
    public StatusDto<String> editCarpartsProduct(@ApiParam(name = "saveBasicCarpartsProductDTO", value = "传入json格式", required = true)@RequestBody SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO) {
        saveBasicCarpartsProductDTO.setOperator(userHolder.getLoggedUserId());
        return carpartsProductService.editCarpartsProduct(saveBasicCarpartsProductDTO);
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
        carpartsProductService.deleteCarpartsProduct(carpartsCode);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 获取零部件详情
     * @param carpartsCode 零部件code
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-02 19:31:12
     */
    @ApiOperation(value = "查询零配件详情",notes = "【魏俊标】")
    @ApiImplicitParam(name = "carpartsCode", value = "零部件Code", required = true, dataType = "String", paramType = "path")
    @GetMapping("/findcarpartsproductdetail/{carpartsCode}")
    public StatusDto<EditBasicCarpartsProductDTO> findCarpartsProductdetail(@PathVariable String carpartsCode){
        return StatusDtoThriftUtils.resolve(carpartsProductService.findCarpartsProductdetail(carpartsCode),EditBasicCarpartsProductDTO.class);
    }

    /**
     *零配件列表分页查询
     * @param categoryCode 零部件分类code
     * @param keyword 零部件名称
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-02 19:52:44
     */
    @ApiOperation(value = "零部件记录列表",notes = "【魏俊标】")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryCode", value = "零部件分类code", required = false, paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "零部件编号或名称", required = false, paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "起始数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int")})
    public StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductList(@RequestParam(required = false) String categoryCode,
                                                                             @RequestParam(required = false) String keyword,
                                                                             @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                             @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return StatusDtoThriftUtils.resolve(carpartsProductService.queryCarpartsProductList(categoryCode, keyword, offset, pageSize),BasicCarpartsProductDTO.class);
    }
    /**
     * 根据分类code查询零配件list（不分页）
     * @param categoryCode 零部件分类code
     * @author weijb
     * @date 2018-08-02 09:59:51
     */
    @ApiOperation(value = "查询未分配的车辆列表",notes = "【魏俊标】")
    @ApiImplicitParam(name = "vinNumber", value = "车辆vin", required = false, paramType = "query")
    @GetMapping("/querycarpartsproductlistbycategorycode")
    public StatusDto<List<BasicCarpartsProductDTO>> queryCarpartsProductListByCategoryCode(@RequestParam(required = false) String categoryCode) {
        return StatusDto.buildDataSuccessStatusDto(carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode));
    }

}
