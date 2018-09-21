package com.ccbuluo.business.constants;

import com.ccbuluo.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangkangjian
 * @date 2018-07-12 17:16:25
 */
public abstract class Assert {

    /**
     * 断言对象不为空,若对象为空则报异常
     * @param obj     待校验对象
     * @param message 异常信息
     * @author zhangkangjian
     * @date 2018-07-12 17:16:25
     */
    public static void notNull(Object obj, String message) {
        if (obj == null)
            throw new CommonException(BizErrorCodeEnum.PARAMETER_ERROR_CODE.getErrorCode(), message);
    }

    /**
     * 断言对象不为空,若对象为空则报异常
     *
     * @param obj 待校验对象
     */
    public static void notNull(Object obj) {
        Assert.notNull(obj, BizErrorCodeEnum.PARAMETER_ERROR_CODE.getMessage());
    }

    /**
     * 断言数字不能为零，若数字为零则报异常
     *
     * @param num     待校验数字
     * @param message 异常信息
     */
    public static void notZero(Integer num, String message) {
        Assert.notNull(num);
        if (num.intValue() == 0)
            throw new CommonException(BizErrorCodeEnum.PARAMETER_ERROR_CODE.getErrorCode(), message);
    }

    /**
     * 断言数字不能为零，若数字为零则报异常
     *
     * @param num 待校验数字
     */
    public static void notZero(Integer num) {
        Assert.notZero(num, BizErrorCodeEnum.PARAMETER_ERROR_CODE.getMessage());
    }

    /**
     * 断言字符串不能为空，若字符串为空则报异常
     *
     * @param string  待校验字符串
     * @param message 异常信息
     */
    public static void notEmpty(String string, String message) {
        if (StringUtils.isEmpty(string))
            throw new CommonException(BizErrorCodeEnum.PARAMETER_ERROR_CODE.getErrorCode(), message);

    }

    /**
     * 断言字符串不能为空，若字符串为空则报异常
     *
     * @param string 待校验字符串
     */
    public static void notEmpty(String string) {
        Assert.notEmpty(string, BizErrorCodeEnum.PARAMETER_ERROR_CODE.getMessage());
    }

    /**
     * 断言该布尔值为true，若为false则抛异常
     *
     * @param expression 待校验布尔值
     * @param message    异常信息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression)
            throw new CommonException(BizErrorCodeEnum.PARAMETER_ERROR_CODE.getErrorCode(), message);
    }

    /**
     * 断言该布尔值为true，若为false则抛异常
     *
     * @param expression 待校验布尔值
     */
    public static void isTrue(boolean expression) {
        Assert.isTrue(expression, BizErrorCodeEnum.PARAMETER_ERROR_CODE.getMessage());
    }
}
