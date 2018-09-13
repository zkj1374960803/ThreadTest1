package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
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
    @Autowired
    private UserHolder userHolder;

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
        sql.append("UPDATE biz_service_order SET ")
            .append("car_no = :carNo,car_vin = :carVin,service_type = :serviceType,")
            .append("customer_name = :customerName,customer_phone = :customerPhone,")
            .append("reserve_contacter = :reserveContacter,reserve_phone = :reservePhone,")
            .append("cur_processor = :curProcessor,processor_orgtype = :processorOrgtype,")
            .append("service_time = :serviceTime,problem_content = :problemContent,")
            .append("operator = :operator,operate_time = :operateTime WHERE service_orderno= :serviceOrderno");
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

    /**
     * 根据订单编号查询订单详情
     * @param orderNo 订单编号
     * @return 订单详情
     * @author liuduo
     * @date 2018-09-04 14:10:01
     */
    public BizServiceOrder getByOrderNo(String orderNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orderNo", orderNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bso.id,bso.service_orderno,bso.car_no,bso.car_vin,")
            .append(" bso.service_type,bso.report_orgno,bso.report_orgtype,bso.report_time,")
            .append(" bso.customer_name,bso.customer_phone,bso.reserve_contacter,")
            .append(" bso.reserve_phone,bso.order_status,bso.dispatch_times,")
            .append(" bso.cur_processor,bso.processor_orgtype,bso.service_time,")
            .append(" bso.order_cost,bso.payed,bso.problem_content ")
            .append(" FROM biz_service_order AS bso WHERE bso.service_orderno= :orderNo");
        return super.findForBean(BizServiceOrder.class, sql.toString(), params);
    }


    /**
     * 查询订单列表
     * @param orderStatus 订单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 订单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    public Page<BizServiceOrder> queryList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pagesize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT bso.id,bso.service_orderno,bso.order_status,bso.car_vin,bso.customer_name,bso.customer_phone,bso.service_type,bso.report_time")
            .append(" FROM biz_service_order AS bso LEFT JOIN biz_service_dispatch AS bsd ON bsd.service_orderno = bso.service_orderno WHERE  1=1 ");
        if (StringUtils.isNotBlank(orderStatus)) {
            params.put("orderStatus", orderStatus);
            sql.append(" AND bso.order_status = :orderStatus ");
        }
        if (StringUtils.isNotBlank(serviceType)) {
            params.put("serviceType", serviceType);
            sql.append(" AND bso.service_type = :serviceType ");
        }
        if (StringUtils.isNotBlank(reportOrgno) && !(reportOrgno.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM))) {
            params.put("reportOrgno", reportOrgno);
            sql.append(" AND (bso.report_orgno = :reportOrgno OR bsd.processor_orgno = :reportOrgno)");
        }
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
            sql.append(" AND (bso.service_orderno LIKE CONCAT('%',:keyword,'%') or bso.car_vin LIKE CONCAT('%',:keyword,'%') OR ")
                .append(" bso.customer_name LIKE CONCAT('%',:keyword,'%') OR bso.customer_phone LIKE CONCAT('%',:keyword,'%'))");
        }
        sql.append(" AND bso.delete_flag = :deleteFlag ORDER BY bso.operate_time DESC");

        return queryPageForBean(BizServiceOrder.class, sql.toString(), params, offset, pagesize);
    }

    /**
     * 修改订单状态
     * @param serviceOrderno 订单编号
     * @param orderStatus 订单状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-09-04 15:37:36
     */
    public void editStatus(String serviceOrderno, String orderStatus) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("orderStatus", orderStatus);
        params.put("operator", userHolder.getLoggedUserId());
        params.put("operateTime", new Date());

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_order SET order_status = :orderStatus,operator = :operator,operate_time = :operateTime WHERE service_orderno= :serviceOrderno");

        updateForMap(sql.toString(), params);
    }

    /**
     * 根据订单编号查询车牌号
     * @param serviceOrderno 订单编号
     * @return 车牌号
     * @author liuduo
     * @date 2018-09-05 09:58:31
     */
    public String getCarNoByServiceOrderno(String serviceOrderno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);

        String sql = "SELECT car_no FROM biz_service_order WHERE service_orderno = :serviceOrderno";

        return findForObject(sql, params, String.class);
    }

    /**
     * 修改维修单的分配次数
     * @param serviceOrderno 维修单单号
     * @param longFlagTwo 分配次数
     * @author liuduo
     * @date 2018-09-08 10:27:10
     */
    public void updateDispatchTimes(String serviceOrderno, Long longFlagTwo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("longFlagTwo", longFlagTwo);

        String sql = "UPDATE biz_service_order SET dispatch_times = :longFlagTwo WHERE service_orderno = :serviceOrderno";

        updateForMap(sql, params);
    }

    /**
     * 获取售后系统的服务单详情
     * @param serviceOrderno  服务单号
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public BizServiceOrder getBizServiceOrderByServiceOrderno(String serviceOrderno) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bso.id,bso.service_orderno,bso.car_no,bso.car_vin,")
                .append("bso.service_type,bso.report_orgno,bso.report_orgtype,bso.report_time,")
                .append("bso.customer_name,bso.customer_phone,bso.reserve_contacter,")
                .append("bso.reserve_phone,bso.order_status,bso.dispatch_times,")
                .append("bso.cur_processor,bso.processor_orgtype,bso.service_time,")
                .append("bso.order_cost,bso.payed,bso.problem_content,bso.creator,")
                .append("bso.create_time,bso.operator,bso.operate_time,bso.delete_flag,")
                .append("bso.remark FROM biz_service_order AS bso WHERE bso.service_orderno= :serviceOrderno");
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        return super.findForBean(BizServiceOrder.class, sql.toString(), params);
    }
}
