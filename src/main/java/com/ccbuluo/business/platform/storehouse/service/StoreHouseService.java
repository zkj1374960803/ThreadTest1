package com.ccbuluo.business.platform.storehouse.service;

import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SearchStorehouseListDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.apache.thrift.TException;

import java.util.List;

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
    int saveStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO)  throws TException;

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
    BizServiceStorehouse getById(Long id) throws TException;

    /**
     * 查询仓库列表
     * @param provinceName 省
     * @param cityName 市
     * @param areaName 区
     * @param storeHouseStatus 状态
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 仓库列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    Page<SearchStorehouseListDTO> queryList(String provinceName, String cityName, String areaName, Integer storeHouseStatus, String keyword, Integer offset, Integer pagesize)throws TException ;

    /**
    * 根据服务中心code查询仓库
    * @param serviceCenterCode 服务中心code
    * @return 服务中心关联的仓库
    * @author liuduo
    * @date 2018-07-05 10:27:40
    */
    List<BizServiceStorehouse> getStorehousrByCode(String serviceCenterCode);

    /**
    * 根据仓库code查询机构code
    * @param storeHouseCode 仓库code
    * @return 机构code
    * @author liuduo
    * @date 2018-08-07 16:08:52
    */
    String getOrgCodeByStoreHouseCode(String storeHouseCode);
    /**
     * 根据服务中心查询启用的仓库列表（下拉框）
     * @param serviceCenterCode 据服务中心code
     * @return StatusDto<QueryStorehouseDTO>
     * @author zhangkangjian
     * @date 2018-08-07 14:32:09
     */
    StatusDto<List<QueryStorehouseDTO>> queryStorehouseByServiceCenterCode(String serviceCenterCode);

    /**
     * 根据仓库code查询仓库信息
     * @param codes 仓库code
     * @return 仓库信息
     * @author liuduo
     * @date 2018-08-13 11:58:35
     */
    List<QueryStorehouseDTO> queryByCode(List<String> codes);

}
