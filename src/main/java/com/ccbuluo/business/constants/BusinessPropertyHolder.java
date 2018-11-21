package com.ccbuluo.business.constants;

import com.ccbuluo.core.entity.OSSClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 业务相关全局配置属性
 * @author liupengfei
 * @date 2018-05-08 10:25:37
 */
@Component
public class BusinessPropertyHolder {

    @Autowired
    private OSSClientProperties ossClientProperties;

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


    // 文件临时目录
    public static String FILE_PATH;
    @Value("${oss.bucket-name}")
    public void setFilePath(String filePath) {
        FILE_PATH = filePath;
    }


    public String getPathPrefix(String bucket) {
        // 获取base-web-code的oss.endpoint
        String endpoint = ossClientProperties.getEndpoint();
        // 截取 http://
        String substring = endpoint.substring(endpoint.lastIndexOf("/")+1);
        StringBuilder http = new StringBuilder("http://");
        StringBuilder point = new StringBuilder(".");
        StringBuilder slash = new StringBuilder("/");
        return http.append(bucket).append(point).append(substring).append(slash).toString();
    }
}
