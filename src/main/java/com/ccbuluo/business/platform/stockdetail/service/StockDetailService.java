package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.stockdetail.dto.UpdateStockBizStockDetailDTO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 库存详情service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-08 14:45:31
 */
public interface StockDetailService {


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
    Long getByinstockorderDeatil(String supplierNo, String productNo, String inRepositoryNo, String applyNo);

    /**
    * 修改有效库存
    * @param bizStockDetail 库存明细
    * @param versionNo 版本号
    * @author liuduo
    * @date 2018-08-08 15:24:09
    */
    void updateValidStock(BizStockDetail bizStockDetail, Long versionNo);

    /**
     * 保存库存明细
     * @param bizStockDetail 库存明细
     * @author liuduo
     * @date 2018-08-08 15:39:58
     */
    Long saveStockDetail(BizStockDetail bizStockDetail);

    /**
     * 根据库存明细id查询版本号
     * @param id 库存明细id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    Integer getVersionNoById(Long id);

    /**
     * 根据库存明细id查询所有库存明细的占用库存
     * @param collect1 库存明细id
     * @return 库存明细
     * @author liuduo
     * @date 2018-08-08 14:55:43
     */
    List<UpdateStockBizStockDetailDTO> getOutstockDetail(List<Long> collect1);

    /**
     * 修改库存明细的占用库存
     * @param bizStockDetails 库存明细
     * @author liuduo
     * @date 2018-08-09 19:14:33
     */
    void updateOccupyStock(List<BizStockDetail> bizStockDetails);

    /**
     * 把库存明细中的有效库存更新库到占用库存
     * @param bizStockDetails 库存明细
     * @author liuduo
     * @date 2018-08-10 11:48:21
     */
    void updateOccupyStockById(List<BizStockDetail> bizStockDetails);

    /**
    * 根据库存明细id查询到控制的版本号（乐观锁使用）
    * @param stockIds 库存明细id
    * @return 版本号对组
    * @author liuduo
    * @date 2018-08-10 15:02:51
    */
    List<Pair<Long,Long>> queryVersionNoById(List<Long> stockIds);

}
