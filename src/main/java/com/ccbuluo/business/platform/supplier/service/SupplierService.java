package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.supplier.dto.*;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.io.IOException;
import java.util.List;

/**
 * 供应商service
 * @author zhangkangjian
 * @date 2018-07-03 13:55:02
 */
public interface SupplierService {
    /**
     * 添加供应商
     * @param bizServiceSupplier 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    StatusDto<String> createSupplier(BizServiceSupplier bizServiceSupplier) throws IOException;
    /**
     * 编辑供应商
     * @param editSupplierDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    void editsupplier(EditSupplierDTO editSupplierDTO) throws IOException;
    /**
     *  供应商启用/停用接口
     * @param id 供应商id
     * @param supplierStatus 停用启用状态 1：启用 0：停用
     * @author zhangkangjian
     * @date 2018-07-03 17:09:11
     */
    void updateSupplierStatus(Long id, Integer supplierStatus);
    /**
     * 查询供应商列表
     * @param querySupplierListDTO 查询条件
     * @return List<ResultSupplierListDTO> 供应商列表
     * @author zhangkangjian
     * @date 2018-07-04 09:57:21
     */
    Page<ResultSupplierListDTO> querySupplierList(QuerySupplierListDTO querySupplierListDTO);
    /**
     * 查询供应商详情
     * @param id 供应商id
     * @return ResultSupplierListDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-04 10:41:23
     */
    ResultFindSupplierDetailDTO findSupplierDetail(Long id);

    /**
     * 添加关联商品
     * @param saveRelSupplierProductDTO 关联商品DTO
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-08-01 10:06:19
     */
    StatusDto<String> createRelSupplierProduct(SaveRelSupplierProductDTO saveRelSupplierProductDTO);

    /**
     * 查询供应商的商品（零配件，物料）
     * @param relSupplierProduct 查询条件
     * @return StatusDto<Page<RelSupplierProduct>> 分页信息
     * @author zhangkangjian
     * @date 2018-08-01 11:46:53
     */
    Page<QueryRelSupplierProductDTO> findSupplierProduct(QueryRelSupplierProductDTO relSupplierProduct);
    /**
     * 删除供应商关系
     * @param id
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-01 20:12:26
     */
    void deleteSupplierProduct(Long id);

    /**
     * 查询供商零配件商品
     * @param qeryRelSupplierProduct 查询条件
     * @return Page<QueryRelSupplierProduct> 分页零配件信息
     * @author zhangkangjian
     * @date 2018-08-01 14:40:39
     */
    Page<QueryRelSupplierProductDTO> queryFittingsProduct(QueryRelSupplierProductDTO qeryRelSupplierProduct);
    /**
     * 查询供商物料商品
     * @param qeryRelSupplierProduct 查询条件
     * @return Page<QueryRelSupplierProduct> 分页物料信息
     * @author zhangkangjian
     * @date 2018-08-01 14:40:39
     */
    Page<QueryRelSupplierProductDTO> queryEquipmentProduct(QueryRelSupplierProductDTO qeryRelSupplierProduct);
    /**
     * 根据商品的code查询供应商的信息（下拉框）
     * @param code 商品的code
     * @return StatusDto<List<RelSupplierProduct>>
     * @author zhangkangjian
     * @date 2018-08-07 15:32:17
     */
    List<QuerySupplierInfoDTO> querySupplierInfo(String code, String type);
}
