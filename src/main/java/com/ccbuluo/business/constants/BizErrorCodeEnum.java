package com.ccbuluo.business.constants;

/**
 * @author liupengfei
 * @date 2018-07-10 21:02:42
 */
public enum BizErrorCodeEnum {
    CODE_EXCEPTION("2000", "生成编码时发生异常"),
    CODE_UNKONEPREFIX("2001", "前缀未知"),
    CODE_OVERRANGE("2002", "自增段超出长度限制"),
    PARAMETER_ERROR_CODE("3000", "必填参数不能为空");

    BizErrorCodeEnum(String errCode, String msg) {
        this.errorCode = errCode;
        this.message = msg;
    }

    private String errorCode;
    private String message;

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
