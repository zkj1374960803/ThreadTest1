package com.ccbuluo.business.platform.outstock.dao;

import com.ccbuluo.business.entity.BizOutstockOrder;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  出库单dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizOutstockOrderDao extends BaseDao<BizOutstockOrder> {
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
    public int saveEntity(BizOutstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstock_order ( outstockorder_no,out_repository_no,")
            .append("outstock_operator,trade_docno,outstock_type,outstock_time,")
            .append("transportorder_no,checked,checked_time,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :outstockorderNo,")
            .append(" :outRepositoryNo, :outstockOperator, :tradeDocno, :outstockType,")
            .append(" :outstockTime, :transportorderNo, :checked, :checkedTime, :creator,")
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
    public int update(BizOutstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstock_order SET outstockorder_no = :outstockorderNo,")
            .append("out_repository_no = :outRepositoryNo,")
            .append("outstock_operator = :outstockOperator,trade_docno = :tradeDocno,")
            .append("outstock_type = :outstockType,outstock_time = :outstockTime,")
            .append("transportorder_no = :transportorderNo,checked = :checked,")
            .append("checked_time = :checkedTime,creator = :creator,")
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
    public BizOutstockOrder getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstockorder_no,out_repository_no,outstock_operator,")
            .append("trade_docno,outstock_type,outstock_time,transportorder_no,checked,")
            .append("checked_time,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark FROM biz_outstock_order WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizOutstockOrder.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_outstock_order WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
