package com.ccbuluo.business.constants;

/**
 * 出入库计划状态
 * @author weijb
 * @date 2018-08-09 16:00:05
 */
public enum StockPlanEnum {
    NOTEFFECTIVE("未生效"),DOING("计划执行中"),COMPLETE("执行完成"),CANCEL("作废");

    StockPlanEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
