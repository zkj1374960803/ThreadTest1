package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
@Service
public class ProblemStockDetailServiceImpl implements ProblemStockDetailService {

    @Resource
    private ProblemStockDetailDao problemStockDetailDao;

    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String productType, String keyword, Integer offset, Integer pageSize){
        return problemStockDetailDao.queryStockBizStockDetailDTOList(productType, keyword,offset, pageSize);
    }
}
