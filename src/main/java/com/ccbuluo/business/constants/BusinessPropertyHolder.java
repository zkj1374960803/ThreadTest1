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

    // region 微服务名称配置
    // endregion

    // region redis key值前缀

    // 用户中心token key前缀名称
    public static String PROJECTCODE_REDIS_KEYPERFIX;
    @Value("${redis.perfix.projectcode}")
    public void setProjectcodeRedisKeyperfix(String projectcodeRedisKeyperfix) {
        PROJECTCODE_REDIS_KEYPERFIX = projectcodeRedisKeyperfix;
    }
    // endregion
}
