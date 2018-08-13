package com.ccbuluo.business.platform.storehouse.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SearchStorehouseListDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.core.thrift.proxy.ThriftProxyServiceFactory;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 仓库service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 10:16:18
 */
@Service
public class StoreHouseServiceImpl implements StoreHouseService{

    @Autowired
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;
    @Autowired
    private UserHolder userHolder;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;


    Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 保存仓库
     * @param saveBizServiceStorehouseDTO 仓库保存用的实体dto
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-07-03 10:20:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) throws TException {
        try {
            // 名字验重
            Boolean aboolean = bizServiceStorehouseDao.storeHouseNameCheck(saveBizServiceStorehouseDTO.getStorehouseName());
            if (aboolean) {
                return Constants.FAILURE_ONE;
            }
            // 生成编码
            String code = null;
            StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FC);
            if (stringStatusDto.getCode().equals(Constants.SUCCESS_CODE)) {
                code = stringStatusDto.getData();
            } else {
                return Constants.FAILURESTATUS;
            }
            BizServiceStorehouse bizServiceStorehouse = create(saveBizServiceStorehouseDTO);
            bizServiceStorehouse.setStorehouseCode(code);
            bizServiceStorehouse.preInsert(userHolder.getLoggedUserId());
            return bizServiceStorehouseDao.saveEntity(bizServiceStorehouse);
        } catch (Exception e) {
            logger.error("保存仓库失败！",e);
            throw e;
        }
    }

    /**
     * 启停仓库
     * @param id 仓库id
     * @param storeHouseStatus 仓库状态
     * @return  操作是否成功
     * @author liuduo
     * @date 2018-07-03 10:37:55
     */
    @Override
    public int editStoreHouseStatus(Long id, Integer storeHouseStatus) {
        return bizServiceStorehouseDao.editStoreHouseStatus(id, storeHouseStatus,userHolder.getLoggedUserId());
    }

    /**
     * 编辑仓库
     * @param saveBizServiceStorehouseDTO 仓库实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-03 11:21:20
     */
    @Override
    public int editStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) {
        try {
            // 名字验重
            Boolean aboolean = bizServiceStorehouseDao.checkName(saveBizServiceStorehouseDTO.getId(),saveBizServiceStorehouseDTO.getStorehouseName());
            if (aboolean) {
                return Constants.FAILURE_ONE;
            }
            BizServiceStorehouse bizServiceStorehouse = create(saveBizServiceStorehouseDTO);
            bizServiceStorehouse.setId(saveBizServiceStorehouseDTO.getId());
            bizServiceStorehouse.preUpdate(userHolder.getLoggedUserId());
            return bizServiceStorehouseDao.update(bizServiceStorehouse);
        } catch (Exception e) {
            logger.error("编辑仓库失败！", e);
            throw e;
        }
    }

    /**
     * 根据id查询仓库详情
     * @param id 仓库id
     * @return 仓库详情
     * @author liuduo
     * @date 2018-07-03 11:29:10
     */
    @Override
    public BizServiceStorehouse getById(Long id) throws TException {
        return bizServiceStorehouseDao.getById(id);
    }

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
    @Override
    public Page<SearchStorehouseListDTO> queryList(String provinceName, String cityName, String areaName, Integer storeHouseStatus, String keyword, Integer offset, Integer pagesize)throws TException  {
        // 调用服务查询服务中心名称
        List<String> serviceCenterCode = new ArrayList<>();
        Map<String, String>  serviceCenterByCodes = Maps.newHashMap();
        serviceCenterByCodes = orgService.getServiceCenterByCodes(keyword);
        if (serviceCenterByCodes.size() > 0) {
            serviceCenterByCodes.forEach((key, value) ->serviceCenterCode.add(key));
        }

        Page<SearchStorehouseListDTO> storehouseList =  bizServiceStorehouseDao.queryList(provinceName, cityName, areaName, storeHouseStatus, keyword, serviceCenterCode, offset, pagesize);
        List<SearchStorehouseListDTO> rows = storehouseList.getRows();
        if (!rows.isEmpty()) {
            // 查到所有的服务中心名称map，用来拼接服务中心的名字
            Map<String, String> allServiceCenters = orgService.getServiceCenterByCodes(null);
            for (SearchStorehouseListDTO storeHouse : rows) {
                storeHouse.setServiceCenterName(allServiceCenters.get(storeHouse.getServicecenterCode()));
            }
        }
        return storehouseList;
    }

    private BizServiceStorehouse create(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) {
        BizServiceStorehouse bizServiceStorehouse = new BizServiceStorehouse();
        bizServiceStorehouse.setStorehouseName(saveBizServiceStorehouseDTO.getStorehouseName());
        bizServiceStorehouse.setStorehouseAcreage(saveBizServiceStorehouseDTO.getStorehouseAcreage());
        bizServiceStorehouse.setStorehouseStatus(saveBizServiceStorehouseDTO.getStorehouseStatus());
        bizServiceStorehouse.setServicecenterCode(saveBizServiceStorehouseDTO.getServicecenterCode());
        bizServiceStorehouse.setProvinceName(saveBizServiceStorehouseDTO.getProvinceName());
        bizServiceStorehouse.setCityName(saveBizServiceStorehouseDTO.getCityName());
        bizServiceStorehouse.setAreaName(saveBizServiceStorehouseDTO.getAreaName());
        bizServiceStorehouse.setLongitude(saveBizServiceStorehouseDTO.getLongitude());
        bizServiceStorehouse.setLatitude(saveBizServiceStorehouseDTO.getLatitude());
        bizServiceStorehouse.setStorehouseAddress(saveBizServiceStorehouseDTO.getStorehouseAddress());
        return bizServiceStorehouse;
    }


    /**
     * 根据服务中心code查询仓库
     * @param serviceCenterCode 服务中心code
     * @return 服务中心关联的仓库
     * @author liuduo
     * @date 2018-07-05 10:27:40
     */
    @Override
    public List<BizServiceStorehouse> getStorehousrByCode(String serviceCenterCode) {
        return bizServiceStorehouseDao.getStorehousrByCode(serviceCenterCode);
    }

    /**
     * 根据仓库code查询机构code
     * @param storeHouseCode 仓库code
     * @return 机构code
     * @author liuduo
     * @date 2018-08-07 16:08:52
     */
    @Override
    public String getOrgCodeByStoreHouseCode(String storeHouseCode) {
        return bizServiceStorehouseDao.getOrgCodeByStoreHouseCode(storeHouseCode);
    }

    /**
     * 根据服务中心查询启用的仓库列表（下拉框）
     * @param serviceCenterCode 据服务中心code
     * @return StatusDto<QueryStorehouseDTO>
     * @author zhangkangjian
     * @date 2018-08-07 14:32:09
     */
    @Override
    public StatusDto<List<QueryStorehouseDTO>> queryStorehouseByServiceCenterCode(String serviceCenterCode) {
        List<QueryStorehouseDTO> queryStorehouseDTOList = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(serviceCenterCode);
        return StatusDto.buildDataSuccessStatusDto(queryStorehouseDTOList);
    }

    /**
     * 根据仓库code查询仓库信息
     * @param codes 仓库code
     * @return 仓库信息
     * @author liuduo
     * @date 2018-08-13 11:58:35
     */
    @Override
    public List<QueryStorehouseDTO> queryByCode(List<String> codes) {
        return bizServiceStorehouseDao.queryByCode(codes);
    }
}
