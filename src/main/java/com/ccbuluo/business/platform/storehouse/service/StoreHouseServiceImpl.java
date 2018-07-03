package com.ccbuluo.business.platform.storehouse.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.thrift.proxy.ThriftProxyServiceFactory;
import com.ccbuluo.usercoreintf.BasicUserOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    Logger logger = LoggerFactory.getLogger(getClass());

    // 编码前缀
    private static final String PREFIX = "FC";
    // 列名
    private static final String FIELDNAME = "storehouse_code";
    // 表名
    private static final String TRABLENAME = "biz_service_storehouse";
    // 是否有随机码
    private static final Boolean ISTANDOMCODE = false;

    /**
     * 保存仓库
     * @param saveBizServiceStorehouseDTO 仓库保存用的实体dto
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-07-03 10:20:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveStoreHouse(SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO) {
        try {
            // 生成编码
            String code = generateProjectCodeService.grantCode(CodePrefixEnum.FC);
            BizServiceStorehouse bizServiceStorehouse = create(saveBizServiceStorehouseDTO);
            bizServiceStorehouse.setServicecenterCode(code);
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
        return bizServiceStorehouseDao.editStoreHouseStatus(id, storeHouseStatus);
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
    public BizServiceStorehouse getById(Long id) {
        BizServiceStorehouse bizServiceStorehouse = bizServiceStorehouseDao.getById(id);
        // 调用服务，查询该仓库是属于哪个服务中心
//        getServer().getServiceCenterByCode(bizServiceStorehouse.getServicecenterCode());
        return null;
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
        return bizServiceStorehouse;
    }

    private BasicUserOrganizationService.Iface getServer() {
        return (BasicUserOrganizationService.Iface) ThriftProxyServiceFactory.newInstance(BasicUserOrganizationService.class, SystemPropertyHolder.getUserCoreRpcSerName());
    }
}
