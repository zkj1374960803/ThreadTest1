package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 客户经理 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizServiceCustmanagerDao extends BaseDao<BizServiceCustmanager> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 客户经理实体
     * @param entity 客户经理实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizServiceCustmanager entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_custmanager ( office_phone,receiving_address,")
            .append("user_uuid,servicecenter_code,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag ) VALUES (  :officePhone, :receivingAddress,")
            .append(" :userUuid, :servicecenterCode, :remark, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 客户经理实体
     * @param entity 客户经理实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizServiceCustmanager entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_custmanager SET office_phone = :officePhone,")
            .append("receiving_address = :receivingAddress,user_uuid = :userUuid,")
            .append("servicecenter_code = :servicecenterCode,remark = :remark,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取客户经理详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizServiceCustmanager getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,office_phone,receiving_address,user_uuid,")
            .append("servicecenter_code,remark,creator,create_time,operator,operate_time,")
            .append("delete_flag FROM biz_service_custmanager WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceCustmanager.class, sql.toString(), params);
    }

    /**
     * 删除客户经理
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_custmanager WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
