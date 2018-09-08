package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceDispatch;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
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

    /**
     * 根据订单号修改订单派发表的接收时间
     * @param serviceOrderno 订单号
     * @author liuduo
     * @date 2018-09-05 16:57:24
     */
    public void updateConfirmed(String serviceOrderno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("confirmed", Constants.FLAG_ONE);
        params.put("confirmTime", new Date());

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_dispatch SET confirmed = :confirmed,confirm_time = :confirmTime WHERE service_orderno = :serviceOrderno");

        updateForMap(sql.toString(), params);
    }


    /**
     * 根据订单号和当前登录人查询服务单派发详情
     * @param serviceOrderno 订单号
     * @param loggedUserId 当前登录用户
     * @return 服务单派发详情
     * @author liuduo
     * @date 2018-09-05 18:41:28
     */
    public Long getByServiceOrderno(String serviceOrderno, String loggedUserId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("loggedUserId", loggedUserId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id FROM biz_service_dispatch  WHERE  service_orderno = :serviceOrderno AND processor_uuid = :loggedUserId");

        return findForObject(sql.toString(), params, Long.class);
    }

    /**
     * 修改是否是当前处理人
     * @param bizServiceDispatch 订单分配详情
     * @return 影响条数
     * @author liuduo
     * @date 2018-09-05 19:02:33
     */
    public int updateCurrentFlag(BizServiceDispatch bizServiceDispatch) {
        String sql = "UPDATE biz_service_dispatch SET current_flag = :currentFlag WHERE id = :id";

        return updateForBean(sql, bizServiceDispatch);
    }

    /**
     * 修改前一个分配单
     * @param updateBizServiceDispatch 需要修改的分配信息
     * @author liuduo
     * @date 2018-09-08 10:16:26
     */
    public int updateReplaceOrgNo(BizServiceDispatch updateBizServiceDispatch) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_service_dispatch SET replace_orgno = :replaceOrgno,replace_orgtype = :replaceOrgtype,dispatch_time = :dispatchTime WHERE id = :id");

        return updateForBean(sql.toString(), updateBizServiceDispatch);
    }
}
