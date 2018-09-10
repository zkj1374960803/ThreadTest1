package com.ccbuluo.business.platform.servicelog.dao;

import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author zhangkangjian
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@Repository
public class BizServiceLogDao extends BaseDao<BizServiceLog> {
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
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int saveBizServiceLog(BizServiceLog entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_log ( model,action,log_content,owner_orgno,")
            .append("owner_orgname,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark ) VALUES (  :model, :action, :logContent, :ownerOrgno,")
            .append(" :ownerOrgname, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int updateBizServiceLog(BizServiceLog entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_log SET model = :model,action = :action,")
            .append("log_content = :logContent,owner_orgno = :ownerOrgno,")
            .append("owner_orgname = :ownerOrgname,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public BizServiceLog getBizServiceLogById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsl.id,bsl.model,bsl.action,bsl.log_content,bsl.owner_orgno,")
            .append("bsl.owner_orgname,bsl.creator,bsl.create_time,bsl.operator,")
            .append("bsl.operate_time,bsl.delete_flag,bsl.remark FROM biz_service_log")
            .append(" AS bsl WHERE bsl.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceLog.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int deleteBizServiceLogById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_log WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 查询维修单日志
     * @param serviceOrderno 维修单编号
     * @param subjectType 操作的主体的类型
     * @return 维修单日志
     * @author liuduo
     * @date 2018-09-10 14:45:37
     */
    public List<BizServiceLog> orderLog(String serviceOrderno, String subjectType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("subjectType", subjectType);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsl.id,bsl.model,bsl.action,bsl.log_content,bsl.owner_orgno,")
            .append(" bsl.owner_orgname,bsl.creator,bsl.create_time FROM biz_service_log AS bsl")
            .append("  WHERE bsl.subject_keyvalue = :serviceOrderno AND subject_type = :subjectType");

        return queryListBean(BizServiceLog.class, sql.toString(), params);
    }
}
