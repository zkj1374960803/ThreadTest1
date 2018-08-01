package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.business.platform.supplier.dto.QueryRelSupplierProduct;
import com.ccbuluo.business.platform.supplier.dto.RelSupplierProduct;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;

import javax.annotation.Resource;

/**
 * 零配件
 * @author zhangkangjian
 * @date 2018-08-01 14:50:33
 */
public class FittingsProductImpl implements Product{
    @Resource
    BizServiceSupplierDao bizServiceSupplierDao;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;

    /**
     * 查询供商商品
     *
     * @param queryRelSupplierProduct 查询条件
     * @return Page<RelSupplierProduct> 分页的商品信息
     * @author zhangkangjian
     * @date 2018-08-01 14:40:39
     */
    @Override
    public Page<QueryRelSupplierProduct> querySupplierProduct(QueryRelSupplierProduct queryRelSupplierProduct) {
        Page<RelSupplierProduct> pageinfo = bizServiceSupplierDao.querySupplierProduct(relSupplierProduct);

        bizServiceSupplierDao.queryFittingsProduct(relSupplierProduct);

        return null;
    }
}
