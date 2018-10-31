package com.ccbuluo.business.constants;

/**
 * 机构类型枚举类
 * @author liupengfei
 * @date 2018-08-14 14:26:35
 */
public enum PriceTypeEnum {
    PLATFORM(1L),
    SERVICECENTER(2L),
    CUSTMANAGER(3L),
    USER(4L);

    PriceTypeEnum(Long level) {
        this.level = level;
    }

    private Long level;

    public Long getLabel(){
        return level;
    }
}
