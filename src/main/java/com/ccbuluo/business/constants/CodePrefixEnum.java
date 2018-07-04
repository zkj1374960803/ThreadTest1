package com.ccbuluo.business.constants;

/**
 *
 * @author liupengfei
 * @date 2018-07-03 17:27:05
 */
public enum CodePrefixEnum {
    FW("服务中心"),
    FC("仓库"),
    FP("零配件"),
    FK("零配件分类"),
    FM("零配件模板"),
    FS("员工"),
    FG("供应商");


    CodePrefixEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
