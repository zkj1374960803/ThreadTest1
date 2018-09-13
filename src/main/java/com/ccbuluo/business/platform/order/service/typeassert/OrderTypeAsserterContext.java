package com.ccbuluo.business.platform.order.service.typeassert;

import com.ccbuluo.business.constants.DocTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liupengfei
 * @date 2018-09-13 16:06:07
 */
@Service
public class OrderTypeAsserterContext {

    private Map<DocTypeEnum, OrderTypeAsserter> asserterChain;

    public OrderTypeAsserterContext(){
        // todo 魏俊标 增加售后服务单类型的asserter
        asserterChain.put(DocTypeEnum.APPLY_DOC, new ApplyOrderTypeAsserter());
    }

    /**
     * 根据单据号确定单据类型
     * @param docNo 单据号
     * @return DocTypeEnum 单据类型的枚举
     * @author liupengfei
     * @date 2018-09-13 16:07:15
     */
    public DocTypeEnum getOrderType(String docNo){
        // 循环遍历 asserterChain
        for(DocTypeEnum docType : asserterChain.keySet()){
            OrderTypeAsserter asserter = asserterChain.get(docType);
            if(asserter.orderTypeMatched(docNo)){
                return docType;
            }
        }
        return DocTypeEnum.UNKNOWN;
    }
}
