package com.ccbuluo.business.platform.storehouse.service;

import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;

/**
 * 仓库service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 10:15:51
 */
public interface StoreHouseService {

    /**
     * 保存仓库
     * @param saveBizServiceStorehouseDTO 仓库保存用的实体dto
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-07-03 10:20:14
     */
    int saveStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO);

    /**
     * 启停仓库
     * @param id 仓库id
     * @param storeHouseStatus 仓库状态
     * @return  操作是否成功
     * @author liuduo
     * @date 2018-07-03 10:37:55
     */
    int editStoreHouseStatus(Long id, Integer storeHouseStatus);

    /**
     * 编辑仓库
     * @param saveBizServiceStorehouseDTO 仓库实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-03 11:21:20
     */
    int editStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO);

    /**
     * 根据id查询仓库详情
     * @param id 仓库id
     * @return 仓库详情
     * @author liuduo
     * @date 2018-07-03 11:29:10
     */
    BizServiceStorehouse getById(Long id);
}
