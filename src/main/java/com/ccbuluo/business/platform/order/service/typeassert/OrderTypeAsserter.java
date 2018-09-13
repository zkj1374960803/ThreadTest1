package com.ccbuluo.business.platform.order.service.typeassert;

/**
 * 确定单据类型的接口类
 * @author liupengfei
 * @date 2018-09-13 15:39:35
 */
public interface OrderTypeAsserter {

    /**
     * 判断单据是否为指定类型的单据，类型随实现类定死
     * @param docNo 单据号
     * @return
     * @author liupengfei
     * @date 2018-09-13 15:45:14
     */
    boolean orderTypeMatched(String docNo);
}
