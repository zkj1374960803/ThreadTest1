package com.ccbuluo.business.platform.outstockplan.dao;

import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  出库计划表实体
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizOutstockplanDetailDao extends BaseDao<BizOutstockplanDetail> {
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
    public int saveEntity(BizOutstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstockplan_detail ( outstock_type,stock_id,")
            .append("product_no,product_type,trade_no,supplier_no,apply_detail_id,")
            .append("cost_price,sales_price,out_repository_no,plan_outstocknum,")
            .append("actual_outstocknum,plan_status,complete_time,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark,product_categoryname")
            .append(" ) VALUES (  :outstockType, :stockId, :productNo, :productType,")
            .append(" :tradeNo, :supplierNo, :applyDetailId, :costPrice, :salesPrice,")
            .append(" :outRepositoryNo, :planOutstocknum, :actualOutstocknum, :planStatus,")
            .append(" :completeTime, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark, :productCategoryname )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizOutstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET outstock_type = :outstockType,")
            .append("stock_id = :stockId,product_no = :productNo,")
            .append("product_type = :productType,trade_no = :tradeNo,")
            .append("supplier_no = :supplierNo,apply_detail_id = :applyDetailId,")
            .append("cost_price = :costPrice,sales_price = :salesPrice,")
            .append("out_repository_no = :outRepositoryNo,")
            .append("plan_outstocknum = :planOutstocknum,")
            .append("actual_outstocknum = :actualOutstocknum,plan_status = :planStatus,")
            .append("complete_time = :completeTime,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark,product_categoryname = :productCategoryname")
            .append(" WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizOutstockplanDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstock_type,stock_id,product_no,product_type,trade_no,")
            .append("supplier_no,apply_detail_id,cost_price,sales_price,out_repository_no,")
            .append("plan_outstocknum,actual_outstocknum,plan_status,complete_time,")
            .append("creator,create_time,operator,operate_time,delete_flag,remark,")
            .append("product_categoryname FROM biz_outstockplan_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizOutstockplanDetail.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_outstockplan_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
