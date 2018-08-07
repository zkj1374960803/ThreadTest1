package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车型具体配置表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarmodelConfigurationDao extends BaseDao<BasicCarmodelConfiguration> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车型具体配置表实体
     * @param entity 车型具体配置表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarmodelConfiguration entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_configuration ( carmodel_id,")
            .append("carmodel_parameter_id,parameter_name,parameter_value ) VALUES ( ")
            .append(" :carmodelId, :carmodelParameterId, :parameterName, :parameterValue )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车型具体配置表实体
     * @param entity 车型具体配置表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarmodelConfiguration entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_configuration SET carmodel_id = :carmodelId,")
            .append("carmodel_parameter_id = :carmodelParameterId,")
            .append("parameter_name = :parameterName,parameter_value = :parameterValue")
            .append(" WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车型具体配置表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarmodelConfiguration getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,carmodel_id,carmodel_parameter_id,parameter_name,")
            .append("parameter_value FROM basic_carmodel_configuration WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarmodelConfiguration.class, sql.toString(), params);
    }

    /**
     * 删除车型具体配置表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carmodel_configuration WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
