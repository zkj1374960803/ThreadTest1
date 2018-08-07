package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizOutstockorderDetailDao extends BaseDao<BizOutstockorderDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 实体
     * @param entity 实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizOutstockorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstockorder_detail ( outstock_orderno,")
            .append("outstock_planid,product_no,product_name,product_type,")
            .append("product_categoryname,supplier_no,outstock_num,unit,cost_price,")
            .append("actual_price,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark ) VALUES (  :outstockOrderno, :outstockPlanid, :productNo,")
            .append(" :productName, :productType, :productCategoryname, :supplierNo,")
            .append(" :outstockNum, :unit, :costPrice, :actualPrice, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizOutstockorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockorder_detail SET ")
            .append("outstock_orderno = :outstockOrderno,")
            .append("outstock_planid = :outstockPlanid,product_no = :productNo,")
            .append("product_name = :productName,product_type = :productType,")
            .append("product_categoryname = :productCategoryname,")
            .append("supplier_no = :supplierNo,outstock_num = :outstockNum,unit = :unit,")
            .append("cost_price = :costPrice,actual_price = :actualPrice,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizOutstockorderDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstock_orderno,outstock_planid,product_no,product_name,")
            .append("product_type,product_categoryname,supplier_no,outstock_num,unit,")
            .append("cost_price,actual_price,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_outstockorder_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizOutstockorderDetail.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_outstockorder_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
