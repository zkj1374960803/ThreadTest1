package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.platform.instock.dao.BizInstockOrderDao;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.core.common.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 入库单service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:06:14
 */
@Service
public class InstockOrderServiceImpl implements InstockOrderService{

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private StoreHouseService storeHouseService;
    @Autowired
    private BizInstockOrderDao bizInstockOrderDao;

    /**
     * 根据申请单号状态查询申请单号集合
     * @param applyStatus 申请单状态
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @Override
    public List<String> queryApplyNo(String applyStatus) {
        // todo 等待康健提交申请单service后实现    刘铎
        return null;
    }

    /**
     * 保存入库单
     * @param bizInstockOrder 入库单实体
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveInstockOrder(BizInstockOrder bizInstockOrder) {
        try {
            // 1、保存入库单
            // todo 等待生成编号方法修改完成后生成编号
            bizInstockOrder.setInstockOperator(userHolder.getLoggedUserId());
            bizInstockOrder.setInstockTime(new Date());
            // 根据入库仓库编号查询入库机构编号
            String orgCodeByStoreHouseCode = storeHouseService.getOrgCodeByStoreHouseCode(bizInstockOrder.getInRepositoryNo());
            bizInstockOrder.setInstockOrgno(orgCodeByStoreHouseCode);
            bizInstockOrderDao.saveEntity(bizInstockOrder);
            // 2、保存入库单详单

            // 3、修改库存明细
            return 0;
        } catch (Exception e) {
            logger.error("保存失败！", e);
            throw e;
        }
    }
}
