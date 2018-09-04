package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 售后系统的服务单 dao
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@Repository
public class BizServiceOrderDao extends BaseDao<BizServiceOrder> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 售后系统的服务单实体
     * @param entity 售后系统的服务单实体
     * @return int 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int saveBizServiceOrder(BizServiceOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_order ( service_orderno,car_no,car_vin,")
            .append("service_type,report_orgno,report_orgtype,report_time,customer_name,")
            .append("customer_phone,reserve_contacter,reserve_phone,order_status,")
            .append("dispatch_times,cur_processor,processor_orgtype,service_time,")
            .append("order_cost,payed,problem_content,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :serviceOrderno, :carNo,")
            .append(" :carVin, :serviceType, :reportOrgno, :reportOrgtype, :reportTime,")
            .append(" :customerName, :customerPhone, :reserveContacter, :reservePhone,")
            .append(" :orderStatus, :dispatchTimes, :curProcessor, :processorOrgtype,")
            .append(" :serviceTime, :orderCost, :payed, :problemContent, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 售后系统的服务单实体
     * @param entity 售后系统的服务单实体
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int updateBizServiceOrder(BizServiceOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_order SET service_orderno = :serviceOrderno,")
            .append("car_no = :carNo,car_vin = :carVin,service_type = :serviceType,")
            .append("report_orgno = :reportOrgno,report_orgtype = :reportOrgtype,")
            .append("report_time = :reportTime,customer_name = :customerName,")
            .append("customer_phone = :customerPhone,")
            .append("reserve_contacter = :reserveContacter,reserve_phone = :reservePhone,")
            .append("order_status = :orderStatus,dispatch_times = :dispatchTimes,")
            .append("cur_processor = :curProcessor,processor_orgtype = :processorOrgtype,")
            .append("service_time = :serviceTime,order_cost = :orderCost,payed = :payed,")
            .append("problem_content = :problemContent,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取售后系统的服务单详情
     * @param id  id
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public BizServiceOrder getBizServiceOrderById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bso.id,bso.service_orderno,bso.car_no,bso.car_vin,")
            .append("bso.service_type,bso.report_orgno,bso.report_orgtype,bso.report_time,")
            .append("bso.customer_name,bso.customer_phone,bso.reserve_contacter,")
            .append("bso.reserve_phone,bso.order_status,bso.dispatch_times,")
            .append("bso.cur_processor,bso.processor_orgtype,bso.service_time,")
            .append("bso.order_cost,bso.payed,bso.problem_content,bso.creator,")
            .append("bso.create_time,bso.operator,bso.operate_time,bso.delete_flag,")
            .append("bso.remark FROM biz_service_order AS bso WHERE bso.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceOrder.class, sql.toString(), params);
    }

    /**
     * 删除售后系统的服务单
     * @param id  id
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int deleteBizServiceOrderById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_order WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
