package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车型参数配置表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarmodelParameterDao extends BaseDao<BasicCarmodelParameter> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车型参数配置表实体
     * @param entity 车型参数配置表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarmodelParameter entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_parameter ( parameter_name,value_type,")
            .append("optional_list,manual_add_flag,required_flag,sort_number,")
            .append("carmodel_label_id,create_time,creator,operate_time,operator,")
            .append("delete_flag ) VALUES (  :parameterName, :valueType, :optionalList,")
            .append(" :manualAddFlag, :requiredFlag, :sortNumber, :carmodelLabelId,")
            .append(" :createTime, :creator, :operateTime, :operator, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车型参数配置表实体
     * @param entity 车型参数配置表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarmodelParameter entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_parameter SET parameter_name = :parameterName,")
            .append("value_type = :valueType,optional_list = :optionalList,")
            .append("manual_add_flag = :manualAddFlag,required_flag = :requiredFlag,")
            .append("sort_number = :sortNumber,carmodel_label_id = :carmodelLabelId,")
            .append("create_time = :createTime,creator = :creator,")
            .append("operate_time = :operateTime,operator = :operator,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车型参数配置表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarmodelParameter getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,parameter_name,value_type,optional_list,manual_add_flag,")
            .append("required_flag,sort_number,carmodel_label_id,create_time,creator,")
            .append("operate_time,operator,delete_flag FROM basic_carmodel_parameter")
            .append(" WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarmodelParameter.class, sql.toString(), params);
    }

    /**
     * 删除车型参数配置表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carmodel_parameter WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
