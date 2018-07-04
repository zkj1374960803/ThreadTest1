package com.ccbuluo.business.platform.projectcode.dao;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 生成编码的dao
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 09:38:13
 */
@Repository
public class GenerateProjectCodeDao extends BaseDao<String> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public String getMaxCode(String fieldName, String tableName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("fieldName", fieldName);
        params.put("tableName", tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append(fieldName)
            .append(" FROM ")
            .append(tableName)
            .append("  ORDER BY id DESC LIMIT 0,1");

        return findForObject(sql.toString(), params, String.class);
    }
}
