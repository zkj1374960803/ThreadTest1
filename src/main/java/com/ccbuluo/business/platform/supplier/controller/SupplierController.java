package com.ccbuluo.business.platform.supplier.controller;

import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.supplier.dto.EditSupplierDTO;
import com.ccbuluo.business.platform.supplier.dto.QuerySupplierListDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultFindSupplierDetailDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultSupplierListDTO;
import com.ccbuluo.business.platform.supplier.service.SupplierService;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

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
    public StatusDto createSupplier(BizServiceSupplier bizServiceSupplier) throws IOException {
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
    public StatusDto editsupplier(EditSupplierDTO editSupplierDTO) throws IOException {
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
    public StatusDto disableAndActivation(Long id, Integer supplierStatus){
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
    public StatusDto<Page<ResultSupplierListDTO>> querySupplierList(QuerySupplierListDTO querySupplierListDTO){
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
    @PostMapping("/findsupplierdetail/{id}")
    @ApiImplicitParam(name = "id", value = "供应商id", required = true, paramType = "path", dataType = "int")
    public StatusDto<ResultFindSupplierDetailDTO> findSupplierDetail(@PathVariable Long id){
        return StatusDto.buildDataSuccessStatusDto(supplierServiceImpl.findSupplierDetail(id));
    }




}
