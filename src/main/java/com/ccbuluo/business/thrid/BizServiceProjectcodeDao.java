package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizServiceProjectcodeDao extends BaseDao<BizServiceProjectcode> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 实体
     * @param entity 实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizServiceProjectcode entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_projectcode ( code_prefix,current_count,")
            .append("create_time,update_time,code_status ) VALUES (  :codePrefix,")
            .append(" :currentCount, :createTime, :updateTime, :codeStatus )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizServiceProjectcode entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_projectcode SET code_prefix = :codePrefix,")
            .append("current_count = :currentCount,create_time = :createTime,")
            .append("update_time = :updateTime,code_status = :codeStatus WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizServiceProjectcode getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,code_prefix,current_count,create_time,update_time,")
            .append("code_status FROM biz_service_projectcode WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceProjectcode.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_projectcode WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
