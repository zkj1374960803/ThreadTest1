package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.core.common.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入库单service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:06:14
 */
@Service
public class InstockOrderServiceImpl implements InstockOrderService{

    @Autowired
    private UserHolder userHolder;
    Logger logger = LoggerFactory.getLogger(getClass());
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
            bizInstockOrder.setInstockOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
            bizInstockOrder.setInstockOperator(userHolder.getLoggedUserId());
            // 2、保存入库单详单
            // 3、修改库存明细
            return 0;
        } catch (Exception e) {
            logger.error("保存失败！", e);
            throw e;
        }
    }
}
