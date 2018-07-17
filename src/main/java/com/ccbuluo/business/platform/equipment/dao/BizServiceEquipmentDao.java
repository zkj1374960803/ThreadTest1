package com.ccbuluo.business.platform.equipment.dao;

import com.ccbuluo.business.entity.BizServiceEquipment;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 物料 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizServiceEquipmentDao extends BaseDao<BizServiceEquipment> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 物料实体
     * @param entity 物料实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int saveEntity(BizServiceEquipment entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_equipment ( equip_code,equip_name,equip_type,")
            .append("remark,creator,create_time,operator,operate_time,delete_flag")
            .append(" ) VALUES (  :equipCode, :equipName, :equipType, :remark, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 物料实体
     * @param entity 物料实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int update(BizServiceEquipment entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_equipment SET equip_code = :equipCode,")
            .append("equip_name = :equipName,equip_type = :equipType,remark = :remark,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取物料详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public BizServiceEquipment getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,equip_code,equip_name,equip_type,remark,creator,")
            .append("create_time,operator,operate_time,delete_flag")
            .append(" FROM biz_service_equipment WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceEquipment.class, sql.toString(), params);
    }

    /**
     * 删除物料
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_equipment WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
