package com.ccbuluo.business.platform.problemstock.service;

import com.ccbuluo.business.platform.problemstock.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.problemstock.dto.StockBizStockDetailDTO;
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

    /**
     * 根据物料code查询某个物料的问题件库存
     * @param productNo 商品编号
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @Override
    public StockBizStockDetailDTO getProdectStockBizStockDetailByCode(String productNo){
        return problemStockDetailDao.getProdectStockBizStockDetailByCode(productNo);
    }
}
