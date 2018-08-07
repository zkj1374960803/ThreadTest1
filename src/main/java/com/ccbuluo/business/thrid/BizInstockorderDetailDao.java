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
public class BizInstockorderDetailDao extends BaseDao<BizInstockorderDetail> {
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
    public int saveEntity(BizInstockorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instockorder_detail ( instock_orderno,instock_planid,")
            .append("product_no,product_name,product_type,product_categoryname,")
            .append("supplier_no,instock_num,unit,cost_price,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :instockOrderno,")
            .append(" :instockPlanid, :productNo, :productName, :productType,")
            .append(" :productCategoryname, :supplierNo, :instockNum, :unit, :costPrice,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark")
            .append(" )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizInstockorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockorder_detail SET instock_orderno = :instockOrderno,")
            .append("instock_planid = :instockPlanid,product_no = :productNo,")
            .append("product_name = :productName,product_type = :productType,")
            .append("product_categoryname = :productCategoryname,")
            .append("supplier_no = :supplierNo,instock_num = :instockNum,unit = :unit,")
            .append("cost_price = :costPrice,creator = :creator,create_time = :createTime,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("delete_flag = :deleteFlag,remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizInstockorderDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_orderno,instock_planid,product_no,product_name,")
            .append("product_type,product_categoryname,supplier_no,instock_num,unit,")
            .append("cost_price,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark FROM biz_instockorder_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizInstockorderDetail.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_instockorder_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
