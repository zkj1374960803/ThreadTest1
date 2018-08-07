package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 物料的类型 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizServiceEquiptypeDao extends BaseDao<BizServiceEquiptype> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 物料的类型实体
     * @param entity 物料的类型实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizServiceEquiptype entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_equiptype ( type_name,remark,creator,")
            .append("create_time,operator,operate_time,delete_flag ) VALUES (  :typeName,")
            .append(" :remark, :creator, :createTime, :operator, :operateTime, :deleteFlag")
            .append(" )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 物料的类型实体
     * @param entity 物料的类型实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizServiceEquiptype entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_equiptype SET type_name = :typeName,")
            .append("remark = :remark,creator = :creator,create_time = :createTime,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取物料的类型详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizServiceEquiptype getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,type_name,remark,creator,create_time,operator,operate_time,")
            .append("delete_flag FROM biz_service_equiptype WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceEquiptype.class, sql.toString(), params);
    }

    /**
     * 删除物料的类型
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_equiptype WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
