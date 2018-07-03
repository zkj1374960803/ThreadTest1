package com.ccbuluo.business.platform.label.dao;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-07-03 09:14:06
 * @version V1.0.0
 */
@Repository
public class BizServiceLabelDao extends BaseDao<BizServiceLabel> {
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
     * @date 2018-07-03 09:14:06
     */
    public int saveEntity(BizServiceLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_label ( label_name,creator,create_time,")
            .append("operator,operate_time,delete_flag ) VALUES (  :labelName, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int update(BizServiceLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_label SET label_name = :labelName,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public BizServiceLabel getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_name,creator,create_time,operator,operate_time,")
            .append("delete_flag FROM biz_service_label WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceLabel.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_label WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
