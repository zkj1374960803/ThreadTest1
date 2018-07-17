package com.ccbuluo.business.platform.equipment.dao;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 物料的类型 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
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
     * @date 2018-07-17 13:57:53
     */
    public int saveEntity(BizServiceEquiptype entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_equiptype ( type_code,type_neme,remark,")
            .append("creator,create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :typeCode, :typeNeme, :remark, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 物料的类型实体
     * @param entity 物料的类型实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int update(BizServiceEquiptype entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_equiptype SET type_code = :typeCode,")
            .append("type_neme = :typeNeme,remark = :remark,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取物料的类型详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public BizServiceEquiptype getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,type_code,type_neme,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag FROM biz_service_equiptype WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceEquiptype.class, sql.toString(), params);
    }

    /**
     * 删除物料的类型
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_equiptype WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
