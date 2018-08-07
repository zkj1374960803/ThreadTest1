package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 维修服务项 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizServiceMaintainitemDao extends BaseDao<BizServiceMaintainitem> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 维修服务项实体
     * @param entity 维修服务项实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizServiceMaintainitem entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_maintainitem ( maintainitem_code,")
            .append("maintainitem_name,unit_price,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag ) VALUES (  :maintainitemCode,")
            .append(" :maintainitemName, :unitPrice, :remark, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 维修服务项实体
     * @param entity 维修服务项实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizServiceMaintainitem entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintainitem SET ")
            .append("maintainitem_code = :maintainitemCode,")
            .append("maintainitem_name = :maintainitemName,unit_price = :unitPrice,")
            .append("remark = :remark,creator = :creator,create_time = :createTime,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取维修服务项详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizServiceMaintainitem getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,maintainitem_code,maintainitem_name,unit_price,remark,")
            .append("creator,create_time,operator,operate_time,delete_flag")
            .append(" FROM biz_service_maintainitem WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceMaintainitem.class, sql.toString(), params);
    }

    /**
     * 删除维修服务项
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_maintainitem WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
