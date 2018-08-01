package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.platform.supplier.dto.QueryRelSupplierProduct;
import com.ccbuluo.business.platform.supplier.dto.RelSupplierProduct;
import com.ccbuluo.db.Page;

/**
 * @author zhangkangjian
 * @date 2018-08-01 14:39:45
 */
public interface Product {
    /**
     * 查询供商商品
     * @param qeryRelSupplierProduct 查询条件
     * @return
     * @author zhangkangjian
     * @date 2018-08-01 14:40:39
     */
    Page<QueryRelSupplierProduct> querySupplierProduct(QueryRelSupplierProduct qeryRelSupplierProduct);


}
