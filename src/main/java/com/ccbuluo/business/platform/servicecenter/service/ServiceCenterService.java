package com.ccbuluo.business.platform.servicecenter.service;

import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchListDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchServiceCenterDTO;
import com.ccbuluo.usercoreintf.ServiceCenterWorkplaceDTO;
import org.apache.thrift.TException;
import java.io.IOException;
import java.util.Map;

/**
 * 服务中心service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 19:20:03
 */
public interface ServiceCenterService {
    /**
     * 保存服务中心
     * @param saveServiceCenterDTO 服务中心实体
     * @return 保存状态
     * @author liuduo
     * @date 2018-07-04 09:40:53
     */
    int saveServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO)  throws TException;

    /**
     * 服务中心详情
     * @param serviceCenterCode 服务中心code
     * @return 服务中心详情
     * @author liuduo
     * @date 2018-07-05 09:15:47
     */
    SearchServiceCenterDTO getByCode(String serviceCenterCode) throws TException;

    /**
     * 编辑服务中心
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterName 服务中心名称
     * @param labels 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:10:30
     */
    int editServiceCenter(String serviceCenterCode, String serviceCenterName, String labels) throws TException;

    /**
    * 根据服务中心code查询职场
    * @param serviceCenterCode 服务中心code
    * @return 职场信息
    * @author liuduo
    * @date 2018-07-05 13:49:42
    */
    ServiceCenterWorkplaceDTO getWorkplaceByCode(String serviceCenterCode)  throws TException ;

    /**
     * 编辑职场
     * @param serviceCenterWorkplaceDTO 职场实体
     * @return 编辑状态
     * @author liuduo
     * @date 2018-07-05 14:09:09
     */
    int editWorkplace(ServiceCenterWorkplaceDTO serviceCenterWorkplaceDTO) throws TException;

    /**
     * 查询服务中心列表
     * @param searchListDTO 查询服务中心列表参数
     * @return 服务中心列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    Map<String, Object> queryList(SearchListDTO searchListDTO) throws TException, IOException;

    /**
     * 服务中心启停
     * @param erviceCenterCode 服务中心code
     * @param serviceCenterStatus 服务中心状态
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-07-06 10:11:00
     */
    int editOrgStatus(String erviceCenterCode, Integer serviceCenterStatus) throws TException;
}
