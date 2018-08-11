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
}
