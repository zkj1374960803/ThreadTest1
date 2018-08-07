package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车型标签管理 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarmodelLabelDao extends BaseDao<BasicCarmodelLabel> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车型标签管理实体
     * @param entity 车型标签管理实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarmodelLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_label ( label_code,label_name,sort,")
            .append("creator,create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :labelCode, :labelName, :sort, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车型标签管理实体
     * @param entity 车型标签管理实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarmodelLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_label SET label_code = :labelCode,")
            .append("label_name = :labelName,sort = :sort,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车型标签管理详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarmodelLabel getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_code,label_name,sort,creator,create_time,operator,")
            .append("operate_time,delete_flag FROM basic_carmodel_label WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarmodelLabel.class, sql.toString(), params);
    }

    /**
     * 删除车型标签管理
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carmodel_label WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
