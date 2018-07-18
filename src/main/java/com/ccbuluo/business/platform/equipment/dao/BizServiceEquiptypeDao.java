package com.ccbuluo.business.platform.equipment.dao;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquiptypeDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
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
        sql.append("INSERT INTO biz_service_equiptype ( type_name,remark,")
            .append("creator,create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :typeName, :remark, :creator, :createTime, :operator,")
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
        sql.append("UPDATE biz_service_equiptype SET ")
            .append(" type_name = :typeName,remark = :remark,operator = :operator,")
            .append(" operate_time = :operateTime WHERE id= :id");
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
     * 根据id删除物料类型
     * @param id 物料类型id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-17 15:03:48
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE  biz_service_equiptype SET delete_flag = :deleteFlag WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 检查物料类型是否重复(新增用)
     * @param typeName 名字
     * @return 名字的是否重复
     * @author liuduo
     * @date 2018-07-17 14:40:42
     */
    public Boolean checkName(String typeName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("typeName", typeName);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_equiptype WHERE type_name = :typeName";

        return findForObject(sql, params, Boolean.class);
    }

    /**
     * 查询物料列表
     * @return 物料类型列表
     * @author liuduo
     * @date 2018-07-17 14:48:07
     */
    public List<BizServiceEquiptype> queryList() {
        String sql = "SELECT id,type_name FROM biz_service_equiptype WHERE delete_flag = 0 ORDER BY operate_time DESC ";

        return queryListBean(BizServiceEquiptype.class, sql, Maps.newHashMap());
    }

    /**
     * 检查物料类型是否重复（编辑用）
     * @param saveBizServiceEquiptypeDTO 物料类型实体
     * @return 名字的是否重复
     * @author liuduo
     * @date 2018-07-17 14:40:42
     */
    public Boolean editCheckName(SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("typeName", saveBizServiceEquiptypeDTO.getTypeName());
        params.put("id", saveBizServiceEquiptypeDTO.getId());

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_equiptype WHERE id <> :id AND type_name = :typeName";

        return findForObject(sql, params, Boolean.class);
    }

}
