package com.ccbuluo.business.constants;

/**
 * 申请状态枚举
 *
 * @author zhangkangjian
 * @date 2018-08-10 14:01:49
 */
public enum ApplyStatusEnum {
    PENDING("10", "待处理"), CANCEL("15", "申请撤销"), WAITINGPAYMENT("20", "等待付款"),
    WAITDELIVERY("30", "等待发货"), OUTSTORE("40", "平台出库"), INSTORE("50", "平台入库"),
    WAITINGRECEIPT("60", "等待收货"), CONFIRMRECEIPT("70", "确认收货"), APPLICATIONCOMPLETED("80", "申请完成");
    // 成员变量
    private String key;
    private String value;

    // 构造方法
    private ApplyStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

}
