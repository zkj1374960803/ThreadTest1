package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockorderDetail;

import java.util.List;

/**
 * 入库单详情单service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 20:22:45
 */
public interface InstockorderDetailService {

    /**
    * 保存入库单详单
    * @param bizInstockorderDetailList 入库单详单
    * @return 保存时否成功
    * @author liuduo
    * @date 2018-08-07 20:25:37
    */
    List<Long> save(List<BizInstockorderDetail> bizInstockorderDetailList);

    /**
     * 根据入库单号查询入库单详单集合
     * @param instockNo 入库单号
     * @return 入库单详单集合
     * @author liuduo
     * @date 2018-08-10 10:56:57
     */
    List<BizInstockorderDetail> queryListByinstockNo(String instockNo);

    /**
     * 修改入库单详单的库存id
     * @param bizInstockorderDetailList1 入库单详单
     * @author liuduo
     * @date 2018-08-10 11:04:51
     */
    void updateInstockorderStockId(List<BizInstockorderDetail> bizInstockorderDetailList1);

}
