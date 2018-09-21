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
    public static String ORGCODE_TOP_SERVICECENTER;
    @Value("${base.topservicecenter}")
    public void setTopStore(String topservicecenter) {
        ORGCODE_TOP_SERVICECENTER = topservicecenter;
    }

    // 客户经理组织机构
    public static String ORGCODE_TOP_CUSMANAGER;
    @Value("${base.custmanager}")
    public void setCustManager(String custManager) {
        BusinessPropertyHolder.ORGCODE_TOP_CUSMANAGER = custManager;
    }

    // custmanagerrolecode 客户经理角色
    public static String ROLECODE_CUSMANAGER;
    @Value("${base.custmanagerrolecode}")
    public void setCustManagerRoleCode(String custManagerRoleCode) {
        BusinessPropertyHolder.ROLECODE_CUSMANAGER = custManagerRoleCode;
    }


    // 售后顶级机构的编号
    public static String ORGCODE_AFTERSALE_PLATFORM;
    @Value("${base.topplatform}")
    public void setOrgcodeAftersalePlatform(String orgcodeAftersalePlatform) {
        ORGCODE_AFTERSALE_PLATFORM = orgcodeAftersalePlatform;
    }
}
