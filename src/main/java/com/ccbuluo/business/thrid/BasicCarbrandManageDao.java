package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 品牌管理表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarbrandManageDao extends BaseDao<BasicCarbrandManage> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 品牌管理表实体
     * @param entity 品牌管理表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarbrandManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carbrand_manage ( carbrand_name,initial,")
            .append("carbrand_logo,sort_number,carbrand_number,create_time,creator,")
            .append("operate_time,operator,delete_flag ) VALUES (  :carbrandName,")
            .append(" :initial, :carbrandLogo, :sortNumber, :carbrandNumber, :createTime,")
            .append(" :creator, :operateTime, :operator, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 品牌管理表实体
     * @param entity 品牌管理表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarbrandManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carbrand_manage SET carbrand_name = :carbrandName,")
            .append("initial = :initial,carbrand_logo = :carbrandLogo,")
            .append("sort_number = :sortNumber,carbrand_number = :carbrandNumber,")
            .append("create_time = :createTime,creator = :creator,")
            .append("operate_time = :operateTime,operator = :operator,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取品牌管理表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarbrandManage getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,carbrand_name,initial,carbrand_logo,sort_number,")
            .append("carbrand_number,create_time,creator,operate_time,operator,delete_flag")
            .append(" FROM basic_carbrand_manage WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarbrandManage.class, sql.toString(), params);
    }

    /**
     * 删除品牌管理表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carbrand_manage WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
