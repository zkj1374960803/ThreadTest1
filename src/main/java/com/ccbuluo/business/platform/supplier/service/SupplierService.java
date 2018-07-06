package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.supplier.dto.EditSupplierDTO;
import com.ccbuluo.business.platform.supplier.dto.QuerySupplierListDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultFindSupplierDetailDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultSupplierListDTO;
import com.ccbuluo.db.Page;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

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
    void createSupplier(BizServiceSupplier bizServiceSupplier) throws TException;
    /**
     * 编辑供应商
     * @param editSupplierDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    void editsupplier(EditSupplierDTO editSupplierDTO);
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
}
