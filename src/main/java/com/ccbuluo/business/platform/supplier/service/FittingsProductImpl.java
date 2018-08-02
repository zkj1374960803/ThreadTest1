package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.business.platform.supplier.dto.QueryRelSupplierProduct;
import com.ccbuluo.business.platform.supplier.dto.RelSupplierProduct;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.category.dto.RelSupplierProductDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 零配件
 * @author zhangkangjian
 * @date 2018-08-01 14:50:33
 */
@Service
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
        Page<QueryRelSupplierProduct> queryRelSupplierProductPage = bizServiceSupplierDao.querySupplierProduct(queryRelSupplierProduct);
//        bizServiceSupplierDao.queryFittingsProduct(relSupplierProduct);
        List<QueryRelSupplierProduct> products = queryRelSupplierProductPage.getRows();
        List<String> productCodes = products.stream().map(QueryRelSupplierProduct::getProductCode).collect(Collectors.toList());
        StatusDtoThriftList<RelSupplierProductDTO> productDto = carpartsCategoryService.queryCarpartsByProductCode(productCodes);
        StatusDto<List<RelSupplierProductDTO>> resolve = StatusDtoThriftUtils.resolve(productDto, RelSupplierProductDTO.class);
        List<RelSupplierProductDTO> data = resolve.getData();
        Map<String, RelSupplierProductDTO> dataMap = data.stream().collect(Collectors.toMap(RelSupplierProductDTO::getProductCode, a -> a,(k1, k2)->k1));
        products.stream().forEach(a ->{
            RelSupplierProductDTO relSupplierProductDTO = dataMap.get(a.getProductCode());
            a.setCategoryName(relSupplierProductDTO.getCategoryName());
            a.setProductName(relSupplierProductDTO.getProductName());
        });
        return queryRelSupplierProductPage;
    }

    /**
     *  查询供商商品
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-01 20:01:27
     */
    public Page<QueryRelSupplierProduct> queryEquipmentProduct(QueryRelSupplierProduct queryRelSupplierProduct) {
        Page<QueryRelSupplierProduct> queryRelSupplierProductPage = bizServiceSupplierDao.queryEquipmentProduct(queryRelSupplierProduct);
        return queryRelSupplierProductPage;
    }

}
