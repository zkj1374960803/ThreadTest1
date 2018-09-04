package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.entity.BizServiceDispatch;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务单派发给相关人的派发记录 dao
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@Repository
public class BizServiceDispatchDao extends BaseDao<BizServiceDispatch> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 服务单派发给相关人的派发记录实体
     * @param entity 服务单派发给相关人的派发记录实体
     * @return int 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int saveBizServiceDispatch(BizServiceDispatch entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_dispatch ( service_orderno,current_flag,")
            .append("previous_id,processor_orgtype,processor_orgno,processor_uuid,")
            .append("confirmed,confirm_time,replace_orgno,replace_orgtype,replace_userid,")
            .append("dispatch_time,creator,create_time,operator,operate_time,delete_flag,")
            .append("remark ) VALUES (  :serviceOrderno, :currentFlag, :previousId,")
            .append(" :processorOrgtype, :processorOrgno, :processorUuid, :confirmed,")
            .append(" :confirmTime, :replaceOrgno, :replaceOrgtype, :replaceUserid,")
            .append(" :dispatchTime, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 服务单派发给相关人的派发记录实体
     * @param entity 服务单派发给相关人的派发记录实体
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int updateBizServiceDispatch(BizServiceDispatch entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_dispatch SET service_orderno = :serviceOrderno,")
            .append("current_flag = :currentFlag,previous_id = :previousId,")
            .append("processor_orgtype = :processorOrgtype,")
            .append("processor_orgno = :processorOrgno,processor_uuid = :processorUuid,")
            .append("confirmed = :confirmed,confirm_time = :confirmTime,")
            .append("replace_orgno = :replaceOrgno,replace_orgtype = :replaceOrgtype,")
            .append("replace_userid = :replaceUserid,dispatch_time = :dispatchTime,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取服务单派发给相关人的派发记录详情
     * @param id  id
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public BizServiceDispatch getBizServiceDispatchById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsd.id,bsd.service_orderno,bsd.current_flag,bsd.previous_id,")
            .append("bsd.processor_orgtype,bsd.processor_orgno,bsd.processor_uuid,")
            .append("bsd.confirmed,bsd.confirm_time,bsd.replace_orgno,bsd.replace_orgtype,")
            .append("bsd.replace_userid,bsd.dispatch_time,bsd.creator,bsd.create_time,")
            .append("bsd.operator,bsd.operate_time,bsd.delete_flag,bsd.remark")
            .append(" FROM biz_service_dispatch AS bsd WHERE bsd.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceDispatch.class, sql.toString(), params);
    }

    /**
     * 删除服务单派发给相关人的派发记录
     * @param id  id
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int deleteBizServiceDispatchById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_dispatch WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
