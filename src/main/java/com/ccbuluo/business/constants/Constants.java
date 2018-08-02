package com.ccbuluo.business.constants;

/**
 * 业务层 常量类
 * @author chaoshuai
 * @date 2018-05-08 09:34:10
 */
public class Constants {

    //是/否
    public  static final Integer NO = 0;
    public  static final Integer YES = 1;

    // 删除标识
    public static final int DELETE_FLAG_NORMAL = 0;
    public static final int DELETE_FLAG_DELETE = 1;
    // 编码最后的值
    public static final Integer FLAG_ONE = 1;

    public static final int SUCCESSSTATUS = 1;
    public static final int FAILURESTATUS = 0;
    // 自增编码的位数
    public static final int AUTOINCREASEDCODESIZE = 6;
    // 供应商编码前缀
    public static final String SUPPLIER_CODE_PREFIX = "FG";
    // 连接符
    public static final String PER_CENT= "%";
    //错误状态码 1成功，其他失败
    public static final String ERROR_CODE = "0";
    public static final String SUCCESS_CODE = "1";

    // 状态标识
    public static final int STATUS_FLAG_ZERO = 0;
    public static final int STATUS_FLAG_ONE = 1;

    /**
     * 冒号
     */
    public static final String CAR_COLON = ":";
    public static final String COMMA = ",";

    public static final Long LONG_FLAG_ONE = 1L;

    public static final int FAILURE_ONE = -1;
    public static final int FAILURE_TWO = -2;
    public static final int ORG_ERROR = -3;

    public static final long LONG_FLAG_DEFAULT = -1;
    public static final long LONG_ORG_ERROR = -3;

    /**
     * 数字格式化格式
     */
    public static final String FORMAT_NUMBER = "%04d";

    /**
     * 车型配置模块
     */
    public static final String CAR_CONFIGURATION = "car_configuration";
    /**
     * 品牌编号首字母
     */
    public static final String CAR_BRAND_CODING = "P";
    /**
     * 品牌编码长度
     */
    public static final Integer CAR_BRAND_LENGTH = 3;
    /**
     * 车系编号首字母
     */
    public static final String CAR_SERIES_CODING = "CX";
    /**
     * 车型配置编码长度
     */
    public static final Integer CAR_CONFIGURATION_CODING_LENGTH = 6;
    /**
     * 车系编码长度
     */
    public static final Integer CAR_SERIES_LENGTH = 5;
    /**
     * 车型配置编码首字母
     */
    public static final String CAR_CONFIGURATION_CODING = "C";
    /**
     * 车辆管理模块
     */
    public static final String CAR_MANAGE = "car_manage";
    /**
     * 车辆编号首字母
     */
    public static final String CAR_CODING = "CL";
    /**
     * 车辆编码长度
     */
    public static final Integer CAR_CODING_LENGTH = 3;

    /**
     * 内部用户添加
     */
    public static final String USER_SOURCE_INNER = "2001";

    /**
     * 用户状态 1在职 0离职
     */
    public static final String USER_STATUS_INNER = "1";
    public static final String USER_STATUS_OUTER = "0";

    /**
     * 1 激活 0冻结
     */
    public static final int FREEZE_STATUS_YES = 1;
    public static final int FREEZE_STATUS_NO = 0;
    /**
     * 客户经理角色code
     */
    public static final String CUSTMANAGER_ROLE_CODE = "JS024";
    /**
     * 客户经理组织架构code
     */
    public static final String CUSTMANAGER_ORG_CODE = "SC000003";
    /**
     * 组织架构的类型
     */
    public static final String ORG_TYPE = "CUSTMANAGER";
    /**
     * 排序字段
     */
    public static final String SORT_FIELD_ID = "id";
    /**
     * 用户是对内用户、还是对外用户
     */
    public static final String USER_TYPE_INNER = "inner";
    /**
     * 零配件
     */
    public static final String PRODUCT_TYPE_FITTINGS = "FITTINGS";
    /**
     * 物料
     */
    public static final String PRODUCT_TYPE_EQUIPMENT = "EQUIPMENT";
}
