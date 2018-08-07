package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车系管理表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarseriesManageDao extends BaseDao<BasicCarseriesManage> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车系管理表实体
     * @param entity 车系管理表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarseriesManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carseries_manage ( carseries_name,carbrand_id,")
            .append("sort_number,carseries_number,create_time,creator,operate_time,")
            .append("operator,delete_flag ) VALUES (  :carseriesName, :carbrandId,")
            .append(" :sortNumber, :carseriesNumber, :createTime, :creator, :operateTime,")
            .append(" :operator, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车系管理表实体
     * @param entity 车系管理表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarseriesManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carseries_manage SET carseries_name = :carseriesName,")
            .append("carbrand_id = :carbrandId,sort_number = :sortNumber,")
            .append("carseries_number = :carseriesNumber,create_time = :createTime,")
            .append("creator = :creator,operate_time = :operateTime,operator = :operator,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车系管理表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarseriesManage getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,carseries_name,carbrand_id,sort_number,carseries_number,")
            .append("create_time,creator,operate_time,operator,delete_flag")
            .append(" FROM basic_carseries_manage WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarseriesManage.class, sql.toString(), params);
    }

    /**
     * 删除车系管理表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carseries_manage WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
