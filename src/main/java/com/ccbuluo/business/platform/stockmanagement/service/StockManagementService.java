package com.ccbuluo.business.platform.stockmanagement.service;

import com.ccbuluo.business.platform.stockmanagement.dto.FindStockDetailDTO;
import org.springframework.stereotype.Service;

/**
 * @author zhangkangjian
 * @date 2018-08-16 10:25:01
 */
public interface StockManagementService {
    /**
     * 查看库存详情
     * @param productNo 商品的编号
     * @param productType 商品类型
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    FindStockDetailDTO findStockProductDetail(String productNo, String productType, String type);
}
