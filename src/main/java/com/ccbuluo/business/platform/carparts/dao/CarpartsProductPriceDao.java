package com.ccbuluo.business.platform.carparts.dao;

import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
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
     * @param productType 商品的类型
     * @return List<RelProductPrice> 价格列表
     * @author zhangkangjian
     * @date 2018-09-06 16:55:13
     */
    public List<RelProductPrice> queryCarpartsProductList(String productType) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT a.product_no,a.suggested_price,a.price_level FROM rel_product_price a  ")
            .append(" INNER JOIN ( ")
            .append(" SELECT MAX(a.id) AS 'id' FROM rel_product_price a  GROUP BY a.price_level, a.product_no ")
            .append(" ) b ON a.id = b.id WHERE 1 = 1 AND a.end_time IS NULL ");
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND a.product_type = :productType ");
        }
        return queryListBean(RelProductPrice.class, sql.toString(), map);
    }

    /**
     * 更新商品价格的结束时间
     * @param relProductPrice
     * @author zhangkangjian
     * @date 2018-09-06 19:56:01
     */
    public void updateProductEndTime(RelProductPrice relProductPrice) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE (SELECT a.id FROM rel_product_price a WHERE a.product_no = :productNo AND a.product_type = :productType AND a.price_level = :priceLevel ORDER BY a.create_time DESC LIMIT 1) b  ")
            .append(" LEFT JOIN rel_product_price a ON a.id = b.id  SET a.end_time = NOW() ");
        updateForBean(sql.toString(), relProductPrice);
    }

    /**
     * 更新零配件价格结束时间
     * @param updateRelProductPriceList 零配件的信息
     * @author zhangkangjian
     * @date 2018-11-21 14:15:09
     */
    public void batchUpdateProductPrice(List<RelProductPrice> updateRelProductPriceList) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE (SELECT a.id FROM rel_product_price a WHERE a.product_no = :productNo AND a.product_type = :productType AND a.price_level = :priceLevel ORDER BY a.create_time DESC LIMIT 1) b  ")
            .append(" LEFT JOIN rel_product_price a ON a.id = b.id  SET a.end_time = NOW() ");
        batchUpdateForListBean(sql.toString(), updateRelProductPriceList);
    }

    /**
     * 批量插入零配件价格列表
     * @param saveRelProductPriceList 列配件的价格列表
     * @author zhangkangjian
     * @date 2018-11-21 14:31:38
     */
    public void batchSaveProductPrice(List<RelProductPrice> saveRelProductPriceList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO rel_product_price (product_no,product_type,suggested_price,up_rate,down_rate, ")
            .append(" price_level,start_time,creator,create_time,operator,operate_time,delete_flag,remark)  ")
            .append(" VALUES(product_no,product_type,suggested_price,up_rate,down_rate, ")
            .append(" price_level,start_time,creator,create_time, ")
            .append(" operator,operate_time,delete_flag,remark) ");
        batchSaveForListBean(sql.toString(), saveRelProductPriceList);
    }
}
