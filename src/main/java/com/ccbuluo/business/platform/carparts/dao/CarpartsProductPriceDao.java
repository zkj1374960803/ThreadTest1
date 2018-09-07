package com.ccbuluo.business.platform.carparts.dao;

import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.dao.BaseDao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.weakref.jmx.internal.guava.collect.Maps;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 零配件价格dao
 * @author zhangkangjian
 * @date 2018-09-06 16:30:11
 */
@Repository
public class CarpartsProductPriceDao extends BaseDao<CarpartsProductPriceDao> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 查询零配件列表
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-06 16:55:13
     */
    public List<RelProductPrice> queryCarpartsProductList() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM (SELECT a.id,a.create_time,a.suggested_price FROM rel_product_price a ORDER BY a.create_time) a  GROUP BY a.create_time ");
        return queryListBean(RelProductPrice.class, sql.toString(), Maps.newHashMap());
    }

    /**
     * 更新商品价格的结束时间
     * @param relProductPrice
     * @author zhangkangjian
     * @date 2018-09-06 19:56:01
     */
    public void updateProductEndTime(RelProductPrice relProductPrice) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE (SELECT a.id FROM rel_product_price a WHERE a.product_no = :productNo AND a.product_type = :productType ORDER BY a.create_time DESC LIMIT 1) b  ")
            .append(" LEFT JOIN rel_product_price a ON a.id = b.id  SET a.end_time = NOW() ");
        updateForBean(sql.toString(), relProductPrice);
    }
}
