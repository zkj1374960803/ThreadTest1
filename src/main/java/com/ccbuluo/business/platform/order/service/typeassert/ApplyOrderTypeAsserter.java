package com.ccbuluo.business.platform.order.service.typeassert;

import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 申请单类型的单据匹配实现类
 * @author liupengfei
 * @date 2018-09-13 15:58:43
 */
@Service
public class ApplyOrderTypeAsserter implements OrderTypeAsserter{

    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;

    @Override
    public boolean orderTypeMatched(String applyNo) {
        // 根据申请单获取申请单详情
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
        if(null == apply){
            return false;
        }else {
            return true;
        }
    }
}
