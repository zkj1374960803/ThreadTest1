package com.ccbuluo.business.platform.order.service.typeassert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 申请单类型的单据匹配实现类
 * @author liupengfei
 * @date 2018-09-13 15:58:43
 */
@Service
public class ApplyOrderTypeAsserter implements OrderTypeAsserter{

    // todo 魏俊标 注入申请单实体Dao实例

    @Override
    public boolean orderTypeMatched(String docNo) {
        return false;
    }
}
