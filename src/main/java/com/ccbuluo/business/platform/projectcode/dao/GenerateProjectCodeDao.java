package com.ccbuluo.business.platform.projectcode.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
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

    public String getMaxCode(String prefix) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("prefix", prefix);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT current_count FROM BIZ_SERVICE_PROJECTCODE WHERE code_prefix = :prefix");
        return findForObject(sql.toString(), params, String.class);
    }
    public void updateMaxCode(String prefix, Integer currentCount){
        String sql = "UPDATE BIZ_SERVICE_PROJECTCODE SET current_count = :currentCount WHERE code_prefix = :prefix";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("prefix", prefix);
        map.put("currentCount", currentCount);
        updateForMap(sql, map);
    }
}
