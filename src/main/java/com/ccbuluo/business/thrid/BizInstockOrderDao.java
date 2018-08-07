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
public class BizInstockOrderDao extends BaseDao<BizInstockOrder> {
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
    public int saveEntity(BizInstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instock_order ( instock_orderno,trade_docno,")
            .append("in_repository_no,instock_operator,instock_type,instock_time,")
            .append("transportorder_no,checked,ceecked_time,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :instockOrderno,")
            .append(" :tradeDocno, :inRepositoryNo, :instockOperator, :instockType,")
            .append(" :instockTime, :transportorderNo, :checked, :ceeckedTime, :creator,")
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
    public int update(BizInstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instock_order SET instock_orderno = :instockOrderno,")
            .append("trade_docno = :tradeDocno,in_repository_no = :inRepositoryNo,")
            .append("instock_operator = :instockOperator,instock_type = :instockType,")
            .append("instock_time = :instockTime,transportorder_no = :transportorderNo,")
            .append("checked = :checked,ceecked_time = :ceeckedTime,creator = :creator,")
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
    public BizInstockOrder getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_orderno,trade_docno,in_repository_no,")
            .append("instock_operator,instock_type,instock_time,transportorder_no,checked,")
            .append("ceecked_time,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark FROM biz_instock_order WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizInstockOrder.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_instock_order WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
