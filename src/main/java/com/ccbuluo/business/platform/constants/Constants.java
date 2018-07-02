package com.ccbuluo.business.platform.constants;

/**
 * 业务层 常量类
 * @author chaoshuai
 * @date 2018-05-08 09:34:10
 */
public class Constants {

    // 删除标识
    public static final int DELETE_FLAG_NORMAL = 0;
    public static final int DELETE_FLAG_DELETE = 1;
    public static final int DELETE_FLAG_SECOND = 2;

    // 是/否
    public static final Integer NO = 0;
    public static final Integer YES = 1;

    public static final int INT_FLAG_DEFAULT = -1;
    public static final int PLATNUMBER_CHECK = -2;

    // 是/否
    public static final long LONG_NO = 0;
    public static final long LONG_YES = 1;

    // 是/否
    public static final String FLAG_ZERO = "0";
    public static final String FLAG_ONE = "1";
    public static final String FLAG_TWO = "2";

    //是/否
    public static final String SUCCESS = "ture";
    public static final String FAILURE = "false";

    /**
     * 车辆共享模块
     */
    public static final String CAR_SHARE= "car_share";

    /**
     * 车型配置模块
     */
    public static final String CAR_CONFIGURATION = "car_configuration";

    /**
     * 车辆管理模块
     */
    public static final String CAR_MANAGE = "car_manage";

    /**
     * 调拨单模块
     */
    public static final String RESERVATION = "reservation";

    /**
     * 车辆服务模块
     */
    public static final String SERVICE = "reservation";

    /**
     * 车型配置编码首字母
     */
    public static final String CAR_CONFIGURATION_CODING = "C";

    /**
     * 车辆共享模块编码首字母
     */
    public static final String CAR_SHARE_CODING = "GX";

    /**
     * 车辆编号首字母
     */
    public static final String CAR_CODING = "CL";

    /**
     * 品牌编号首字母
     */
    public static final String CAR_BRAND_CODING = "P";

    /**
     * 车系编号首字母
     */
    public static final String CAR_SERIES_CODING = "CX";
    /**
     * 调拨单编码首字母
     */
    public static final String RESERVATION_CODING = "D";

    /**
     * 调拨单编码首字母
     */
    public static final String MAINTAIN_CODING = "ZLSM";
    /**
     * 保险记录编码首字母
     */
    public static final String INSURANCE_CODING = "ZLSI";

    /**
     * 车辆编码长度
     */
    public static final Integer CAR_CODING_LENGTH = 3;


    /**
     * 车辆共享编码长度
     */
    public static final Integer CAR_SHARE_LENGTH = 6;

    /**
     * 品牌编码长度
     */
    public static final Integer CAR_BRAND_LENGTH = 3;

    /**
     * 车系编码长度
     */
    public static final Integer CAR_SERIES_LENGTH = 5;

    /**
     * 车型配置编码长度
     */
    public static final Integer CAR_CONFIGURATION_CODING_LENGTH = 6;

    /**
     * 调拨单编码长度
     */
    public static final Integer RESERVATION_CODE = 6;

    /**
     * 保养单编码长度
     */
    public static final Integer MAINTAIN_CODE = 4;
    /**
     * 保险记录编码长度
     */
    public static final Integer INSURANCE_CODE = 4;

    /**
     * 车辆来源
     */
    public static final String CAR_SOURCE_TYPE_OTHER = "OTHER";

    /**
     * 冒号
     */
    public static final String CAR_COLON = ":";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 百分号
     */
    public  static final String PER_CENT = "%";


    /**
     * inner 对内用户 outer 对外用户
     */
    public static final String USER_TYPE_INNER = "inner";
    public static final String USER_TYPE_OUTER = "outer";

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
     * 1 冻结 0激活
     */
    public static final int FREEZE_STATUS_YES = 1;
    public static final int FREEZE_STATUS_NO = 0;

    /**
     * COMMON 普通的 ADDED附加的
     */
    public static final String WORKPLACE_TYPE_COMMON = "COMMON";
    public static final String WORKPLACE_TYPE_ADDED = "ADDED";
    /**
     * 用户管理平台 服务名
     */
    public static final String USERMANAGER_PLATFORM = "UserCoreSerService";
    /**
     * 身份证
     */
    public static final String USER_CERTIFICATE_TYPE = "1";

    /**
     * 增值业务类型常量
     * @author     weihao.xi
     * @date       2018/5/22 19:53
     */
    public static final String RULE_TYPE = "1"; //免赔服务


    // 增值业务状态
    public static final int RULE_TYPE_CLOSE = 0; //关闭
    public static final int RULE_TYPE_OPEN = 1; //开启

    /**
     * 店长角色名称
     */
    public static final String STORE_MANAGER_NAME = "门店店长";
    /**
     * 店长角色名称
     */
    public static final String STORE_SERVICE_NAME = "门店服务代表";

    /**
     * 门店类型
     */
    public static final String STORE_TYPE = "STORE";

    /**
     * 1 正常，开启   2 禁用，停用，作废，关闭
     */
    public static final int DEFAULT_STATUS_NORMAL = 1;
    public static final int DEFAULT_STATUS_DISABLE = 0;
    /**
     * 字典 订单流程的 type
     */
    public static final String ORDER_FLOW_NODE ="ORDER_FLOW_NODE" ;
    public static final Long LONG_TWO = 2L;

    /**
     * 被保险对象,1为公司2为个人
     */
    public static final Integer INSURED_OBJECT_COMPANY  = 1;
    public static final Integer INSURED_OBJECT_PERSONAL = 2;

    /**
     * 保险类型：1为交强险，2为商业保险
     */
    public static final Integer INSURANCE_TYPE_STRONG  = 1;
    public static final Integer INSURANCE_TYPE_COMMERCIAL = 2;

    /**
     * 保险即将到期天数
     */
    public static final String INSURANCE_EXPIRE_DATE = "30";

    /**
     * 参数为空
     */
    public static final String PARAMETER_NULL_VERIFY_CODE = "50001";
    public static final String PARAMETER_NULL_VERIFY = "参数为空！";


    /**
     * 车辆服务相关
     */
    public static final String ACCIDNET_CODE = "ZLSA";
    public static final String ACCIDNET_CODE_END = "0001";
    public static final String REPAIR_CODE = "ZLSR";
    public static final String ACCIDNET_NO = "accident_no";
    public static final String BIZ_CARACCIDENT_INFO = "biz_caraccident_info";
    public static final String REPAIR_NO = "repair_no";
    public static final String BIZ_CARREPAIR_INFO = "biz_carrepair_info";

    /**
     * 数字格式化格式
     */
    public static final String FORMAT_NUMBER = "%04d";

    /**
     * 车辆违章前缀
     */
    public static final String CAR_VIOLATION_PREFIX = "ZLSV";
    /**
     *
     * 车牌号不存在
     */
    public static final String PLAT_NUMBER_VERIFY_CODE = "50004";

    /**
     * 车牌号和车辆id不匹配！
     */
    public static final String PLAT_NUMBER_VERIFY = "车牌号和车辆id不匹配！";

    /**
     * 车辆服务中用到的日期格式化格式
     */
    public static final String DATEFORMAT = "yyMMdd";
    /**
     * 是否客户在租赁期间违章
     * 1.是，0.否
     */
    public static final int CUSTOMER_DID_ONE = 1;
    public static final int CUSTOMER_DID_ZERO = 0;

}
