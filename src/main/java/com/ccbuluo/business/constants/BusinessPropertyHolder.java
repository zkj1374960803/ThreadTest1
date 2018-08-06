package com.ccbuluo.business.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 业务相关全局配置属性
 * @author liupengfei
 * @date 2018-05-08 10:25:37
 */
@Component

public class BusinessPropertyHolder {

    // 用户中心token key前缀名称
    public static String PROJECTCODE_REDIS_KEYPERFIX;
    @Value("${redis.perfix.projectcode}")
    public void setProjectcodeRedisKeyperfix(String projectcodeRedisKeyperfix) {
        PROJECTCODE_REDIS_KEYPERFIX = projectcodeRedisKeyperfix;
    }

    // 顶级服务中心code
    public static String TOP_SERVICECENTER;
    @Value("${base.topservicecenter}")
    public void setTopStore(String topservicecenter) {
        TOP_SERVICECENTER = topservicecenter;
    }

    // 客户经理
    public static String custManager;
    @Value("${base.custmanager}")
    public void setCustManager(String custManager) {
        BusinessPropertyHolder.custManager = custManager;
    }
}
