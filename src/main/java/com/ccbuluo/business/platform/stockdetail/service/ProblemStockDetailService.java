package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
public interface ProblemStockDetailService {

    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String productType, String keyword, Integer offset, Integer pageSize);
}
