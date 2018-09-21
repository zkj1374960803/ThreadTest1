package com.ccbuluo.business.platform.servicecenter.dao;

import com.ccbuluo.business.entity.BizServiceCenter;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-09-05 16:09:56
 * @version V1.0.0
 */
@Repository
public class BizServiceCenterDao extends BaseDao<BizServiceCenter> {
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
     * @date 2018-09-05 16:09:56
     */
    public int saveEntity(BizServiceCenter entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_center ( org_code,contact,contact_phone,")
            .append("creator,create_time,operator,operate_time,delete_flag,remark")
            .append(" ) VALUES (  :orgCode, :contact, :contactPhone, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-09-05 16:09:56
     */
    public int update(BizServiceCenter entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_center SET org_code = :orgCode,contact = :contact,")
            .append("contact_phone = :contactPhone,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-09-05 16:09:56
     */
    public BizServiceCenter getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,org_code,contact,contact_phone,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark FROM biz_service_center")
            .append(" WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceCenter.class, sql.toString(), params);
    }


    /**
     * 根据机构code查询联系人
     * @param orgCodes 机构code
     * @return 服务中心联系人和电话
     * @author liuduo
     * @date 2018-09-05 16:15:37
     */
    public List<BizServiceCenter> queryNameByOrgCode(List<String> orgCodes) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orgCodes", orgCodes);

        String sql = "SELECT org_code,contact,contact_phone FROM biz_service_center WHERE org_code IN(:orgCodes)";

        return queryListBean(BizServiceCenter.class, sql, params);
    }
}
