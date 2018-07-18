package com.ccbuluo.business.platform.projectcode.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-07-06 12:42:43
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
     * @date 2018-07-06 12:42:43
     */
    public int saveEntity(BizServiceProjectcode entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_projectcode ( code_prefix,current_count,")
            .append("create_time) VALUES (  :codePrefix,")
            .append(" :currentCount, :createTime)");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-06 12:42:43
     */
    public int update(BizServiceProjectcode entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_projectcode SET code_prefix = :codePrefix,")
            .append("current_count = :currentCount,update_time = :updateTime WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 根据前缀更新业务主体编号记录
     * @param entity
     * @return
     */
    public int updateByPrifix(BizServiceProjectcode entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_projectcode SET current_count = :currentCount,")
                .append("update_time = :updateTime WHERE code_prefix= :codePrefix");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param codePrefix  前缀
     * @author liuduo
     * @date 2018-07-06 12:42:43
     */
    public BizServiceProjectcode getMaxCode(String codePrefix) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("count", Constants.FLAG_ONE);
        params.put("codePrefix", codePrefix);

        String sql = "SELECT id,code_prefix,current_count + :count as current_count FROM biz_service_projectcode WHERE code_prefix= :codePrefix LIMIT 1 ";

        return super.findForBean(BizServiceProjectcode.class, sql, params);
    }

}
