package com.ccbuluo.business.platform.inputstockplan.dao;

import com.ccbuluo.business.thrid.BizInstockplanDetail;
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
public class BizInstockplanDetailDao extends BaseDao<BizInstockplanDetail> {
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
    public int saveEntity(BizInstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instockplan_detail ( instock_type,product_no,")
            .append("product_type,product_categoryname,trade_no,supplier_no,")
            .append("instock_repository_no,cost_price,plan_instocknum,actual_instocknum,")
            .append("complete_status,complete_time,outstock_planid,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :instockType,")
            .append(" :productNo, :productType, :productCategoryname, :tradeNo,")
            .append(" :supplierNo, :instockRepositoryNo, :costPrice, :planInstocknum,")
            .append(" :actualInstocknum, :completeStatus, :completeTime, :outstockPlanid,")
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
    public int update(BizInstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET instock_type = :instockType,")
            .append("product_no = :productNo,product_type = :productType,")
            .append("product_categoryname = :productCategoryname,trade_no = :tradeNo,")
            .append("supplier_no = :supplierNo,")
            .append("instock_repository_no = :instockRepositoryNo,cost_price = :costPrice,")
            .append("plan_instocknum = :planInstocknum,")
            .append("actual_instocknum = :actualInstocknum,")
            .append("complete_status = :completeStatus,complete_time = :completeTime,")
            .append("outstock_planid = :outstockPlanid,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
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
    public BizInstockplanDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("plan_instocknum,actual_instocknum,complete_status,complete_time,")
            .append("outstock_planid,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_instockplan_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizInstockplanDetail.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_instockplan_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
