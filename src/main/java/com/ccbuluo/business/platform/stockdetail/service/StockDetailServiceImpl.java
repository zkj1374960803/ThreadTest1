package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存详情service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-08 14:46:41
 */
@Service
public class StockDetailServiceImpl implements StockDetailService{

    @Autowired
    private BizStockDetailDao bizStockDetailDao;

    /**
     * 根据入库详单的  供应商、商品、仓库、批次号  查询在库存中有无记录
     * @param supplierNo 供应商
     * @param productNo 商品编号
     * @param inRepositoryNo 入库仓库编号
     * @param applyNo 交易单号
     * @return 库存明细id
     * @author liuduo
     * @date 2018-08-08 14:55:43
     */
    @Override
    public Long getByinstockorderDeatil(String supplierNo, String productNo, String inRepositoryNo, String applyNo) {
        return bizStockDetailDao.getByinstockorderDeatil(supplierNo, productNo, inRepositoryNo, applyNo);
    }

    /**
     * 修改有效库存
     * @param bizStockDetail 库存明细
     * @param versionNo 版本号
     * @author liuduo
     * @date 2018-08-08 15:24:09
     */
    @Override
    public void updateValidStock(BizStockDetail bizStockDetail, Integer versionNo) {
        bizStockDetailDao.updateValidStock(bizStockDetail, versionNo);
    }

    /**
     * 保存库存明细
     * @param bizStockDetail 库存明细
     * @author liuduo
     * @date 2018-08-08 15:39:58
     */
    @Override
    public void saveStockDetail(BizStockDetail bizStockDetail) {
        bizStockDetailDao.saveEntity(bizStockDetail);
    }

    /**
     * 根据库存明细id查询版本号
     * @param id 库存明细id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    @Override
    public Integer getVersionNoById(Long id) {
        return bizStockDetailDao.getVersionNoById(id);
    }
}
