package com.ccbuluo.business.platform.order.service.typeassert;

import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 维修单类型的单据匹配实现类
 * @author weijb
 * @date 2018-09-13 16:58:43
 */
@Service
public class ServiceOrderTypeAsserter implements OrderTypeAsserter{

    @Autowired
    private BizServiceOrderDao bizServiceOrderDao;

    /**
     *  判断是否是维修单
     * @param serviceOrderno 维修单编号
     * @exception 
     * @return 
     * @author weijb
     * @date 2018-09-13 17:05:11
     */
    @Override
    public boolean orderTypeMatched(String serviceOrderno) {
        // 根据申请单获取申请单详情
        BizServiceOrder serviceOrder = bizServiceOrderDao.getBizServiceOrderByServiceOrderno(serviceOrderno);
        if(null == serviceOrder){
            return false;
        }else {
            return true;
        }
    }
}
