package com.ccbuluo.business.constants;

/**
 *
 * @author liupengfei
 * @date 2018-07-03 17:27:05
 */
public enum ProductUnitEnum {
    ITEM("件"),SET("套"),BOX("箱"),TYPE("类"),BATCH("批"),PIECE("个");

    ProductUnitEnum(String label){
        this.label = label;
    }

    private String label;

    public String getLabel(){
        return label;
    }
}
