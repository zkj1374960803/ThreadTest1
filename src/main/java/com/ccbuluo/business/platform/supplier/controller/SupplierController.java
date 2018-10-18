package com.ccbuluo.business.platform.supplier.controller;

import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.supplier.dto.*;
import com.ccbuluo.business.platform.supplier.service.SupplierService;
import com.ccbuluo.core.annotation.validate.ValidateGroup;
import com.ccbuluo.core.annotation.validate.ValidateMax;
import com.ccbuluo.core.annotation.validate.ValidateNotBlank;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.validate.Group;
import com.ccbuluo.core.validate.ValidateUtils;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangkangjian
 * @version v1.0.0
 * @date 2018-07-03 13:50:33
 */
@Api(tags = "供应商(平台端)")
@RestController
@RequestMapping("/supplier/supplier")
public class SupplierController extends BaseController {

    @Resource(name = "supplierServiceImpl")
    private SupplierService supplierServiceImpl;

    /**
     * 添加供应商
     * @param bizServiceSupplier 供应商信息
     * @return StatusDto 状态DTO
     * @author zhangkangjian
     * @date 2018-07-03 14:24:25
     */
    @PostMapping("/createsupplier")
    @ApiOperation(value = "添加供应商",notes = "【张康健】")
    public StatusDto createSupplier(@ValidateGroup(value = Group.Add.class) BizServiceSupplier bizServiceSupplier) throws IOException {
        return supplierServiceImpl.createSupplier(bizServiceSupplier);
    }
    
    /**
     *  编辑供应商
     * @param editSupplierDTO 供应商信息
     * @return StatusDto 状态DTO
     * @author zhangkangjian
     * @date 2018-07-03 14:54:08
     */
    @PostMapping("/editsupplier")
    @ApiOperation(value = "编辑供应商",notes = "【张康健】")
    public StatusDto editsupplier(@ValidateGroup(value = Group.Update.class)EditSupplierDTO editSupplierDTO) throws IOException {
        supplierServiceImpl.editsupplier(editSupplierDTO);
        return StatusDto.buildSuccessStatusDto();
    }
    
    /**
     *  供应商启用/停用接口
     * @param id 供应商id
     * @param supplierStatus 停用启用状态 1：启用 0：停用
     * @return StatusDto 状态DTO
     * @author zhangkangjian
     * @date 2018-07-03 17:09:11
     */
    @ApiOperation(value = "供应商启用/停用接口",notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "供应商id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "supplierStatus", value = "停用启用状态 1：启用 0：停用", required = true, paramType = "query")
    })
    @PostMapping("/disableandactivation")
    public StatusDto disableAndActivation(@ValidateMax(value = Long.MAX_VALUE) Long id, @ValidateMax(value = Integer.MAX_VALUE)Integer supplierStatus){
        supplierServiceImpl.updateSupplierStatus(id, supplierStatus);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 分页查询供应商列表接口
     * @param querySupplierListDTO 查询条件信息
     * @return  StatusDto<List<ResultSupplierListDTO>> 状态dto
     * @author zhangkangjian
     * @date 2018-07-03 17:27:02
     */
    @ApiOperation(value = "分页查询供应商列表接口",notes = "【张康健】")
    @GetMapping("/querysupplierlist")
    public StatusDto<Page<ResultSupplierListDTO>> querySupplierList(@ValidateGroup QuerySupplierListDTO querySupplierListDTO){
        return StatusDto.buildDataSuccessStatusDto(supplierServiceImpl.querySupplierList(querySupplierListDTO));
    }

    /**
     * 查询供应商详情接口
     * @param id 供应商id
     * @return StatusDto<ResultSupplierListDTO> 状态dto
     * @author zhangkangjian
     * @date 2018-07-04 10:31:57
     */
    @ApiOperation(value = "查询供应商详情接口",notes = "【张康健】")
    @GetMapping("/findsupplierdetail/{id}")
    @ApiImplicitParam(name = "id", value = "供应商id", required = true, paramType = "path", dataType = "int")
    public StatusDto<ResultFindSupplierDetailDTO> findSupplierDetail(@PathVariable Long id){
        return StatusDto.buildDataSuccessStatusDto(supplierServiceImpl.findSupplierDetail(id));
    }

    /**
     * 添加关联商品
     * @param saveRelSupplierProductDTO 关联商品DTO
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-08-01 10:06:19
     */
    @ApiOperation(value = "添加关联商品",notes = "【张康健】")
    @PostMapping("/createrelsupplierproduct")
    public StatusDto<String> createRelSupplierProduct(@ApiParam(name = "saveRelSupplierProductDTO", value = "保存供应商关联商品关系json", required = true)@RequestBody @ValidateNotBlank SaveRelSupplierProductDTO saveRelSupplierProductDTO){
        ValidateUtils.validate(saveRelSupplierProductDTO.getSupplierProductList(),null);
        return supplierServiceImpl.createRelSupplierProduct(saveRelSupplierProductDTO);
    }

    /**
     * 查询供应商的商品（零配件，物料）
     * @param queryRelSupplierProductDTO 查询条件
     * @return StatusDto<Page<RelSupplierProduct>> 分页信息
     * @author zhangkangjian
     * @date 2018-08-01 11:46:53
     */
    @ApiOperation(value = "查询供应商的商品",notes = "【张康健】")
    @GetMapping("/findsupplierproduct")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "supplierCode", value = "供应商code", required = true, paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "productType", value = "商品类型(注：FITTINGS零配件，EQUIPMENT物料)", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "offset", value = "偏移量", required = true, paramType = "query",dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示的数量", required = true, paramType = "query", dataType = "int")
    })
    public StatusDto<Page<QueryRelSupplierProductDTO>> findSupplierProduct(@ApiIgnore @ValidateGroup QueryRelSupplierProductDTO queryRelSupplierProductDTO){
        Page<QueryRelSupplierProductDTO> page = supplierServiceImpl.findSupplierProduct(queryRelSupplierProductDTO);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 删除供应商关联关系
     * @param id 关联id
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-01 20:09:52
     */
    @ApiOperation(value = "删除供应商关联商品关系",notes = "【张康健】")
    @ApiImplicitParam(name = "id", value = "供应商关联商品关系id", required = true, paramType = "path",dataType = "int")
    @PostMapping("/deletesupplierproduct/{id}")
    public StatusDto<String> deleteSupplierProduct(@ApiIgnore @PathVariable Long id){
         supplierServiceImpl.deleteSupplierProduct(id);
        return StatusDto.buildSuccessStatusDto();
    }
    
    /**
     * 根据商品的code查询供应商的信息（下拉框）
     * @param code 商品的code
     * @return StatusDto<List<RelSupplierProduct>>
     * @author zhangkangjian
     * @date 2018-08-07 15:32:17
     */
    @ApiOperation(value = "根据商品的code查询供应商的信息（下拉框）",notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "code", value = "商品的code", required = true, paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "type", value = "商品类型（注：FITTINGS零配件，EQUIPMENT物料）", required = true, paramType = "query",dataType = "String")
    })
    @GetMapping("/querysupplierinfo")
    public StatusDto<List<QuerySupplierInfoDTO>> querySupplierInfo(@ValidateNotBlank String code, @ValidateNotBlank String type){
        List<QuerySupplierInfoDTO> list = supplierServiceImpl.querySupplierInfo(code, type);
        return StatusDto.buildDataSuccessStatusDto(list);
    }


}
