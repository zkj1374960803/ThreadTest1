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
import java.util.List;
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
            .append("service_type,report_orgno,report_orgtype,report_time,customer_name,customer_orgno,")
            .append("customer_phone,reserve_contacter,reserve_phone,order_status,")
            .append("dispatch_times,cur_processor,processor_orgtype,processor_orgno,service_time,")
            .append("order_cost,payed,problem_content,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :serviceOrderno, :carNo,")
            .append(" :carVin, :serviceType, :reportOrgno, :reportOrgtype, :reportTime,")
            .append(" :customerName, :customerOrgno,:customerPhone, :reserveContacter, :reservePhone,")
            .append(" :orderStatus, :dispatchTimes, :curProcessor, :processorOrgtype,:processorOrgno,")
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
            .append("customer_name = :customerName,customer_phone = :customerPhone,customer_orgno = :customerOrgno,")
            .append("reserve_contacter = :reserveContacter,reserve_phone = :reservePhone,")
            .append("cur_processor = :curProcessor,processor_orgtype = :processorOrgtype,processor_orgno = :processorOrgno,")
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
            .append("bso.customer_name,bso.customer_orgno,bso.customer_phone,bso.reserve_contacter,")
            .append("bso.reserve_phone,bso.order_status,bso.dispatch_times,")
            .append("bso.cur_processor,bso.processor_orgtype,bso.processor_orgno,bso.service_time,")
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
            .append(" bso.customer_name,bso.customer_orgno,bso.customer_phone,bso.reserve_contacter,")
            .append(" bso.reserve_phone,bso.order_status,bso.dispatch_times,")
            .append(" bso.cur_processor,bso.processor_orgtype,bso.processor_orgno,bso.service_time,")
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
     * @param pageSize 每页数
     * @return 订单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    public Page<BizServiceOrder> queryList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT bso.id,bso.service_orderno,bso.order_status,bso.car_vin,bso.customer_name,bso.customer_phone,bso.service_type,bso.report_time,")
            .append(" bso.cur_processor,bso.processor_orgno,bso.processor_orgtype")
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

        return queryPageForBean(BizServiceOrder.class, sql.toString(), params, offset, pageSize);
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
     * @param bizServiceOrder 维修单
     * @author liuduo
     * @date 2018-09-08 10:27:10
     */
    public void updateDispatchTimes(BizServiceOrder bizServiceOrder) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_order SET dispatch_times = :dispatchTimes,processor_orgno = :processorOrgno,")
            .append(" processor_orgtype = :processorOrgtype WHERE service_orderno = :serviceOrderno");

        updateForBean(sql.toString(), bizServiceOrder);
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
                .append("bso.customer_name,bso.customer_phone,bso.customer_orgno,bso.reserve_contacter,")
                .append("bso.reserve_phone,bso.order_status,bso.dispatch_times,")
                .append("bso.cur_processor,bso.processor_orgtype,bso.processor_orgno,bso.service_time,")
                .append("bso.order_cost,bso.payed,bso.problem_content,bso.creator,")
                .append("bso.create_time,bso.operator,bso.operate_time,bso.delete_flag,")
                .append("bso.remark FROM biz_service_order AS bso WHERE bso.service_orderno= :serviceOrderno");
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        return super.findForBean(BizServiceOrder.class, sql.toString(), params);
    }

    /**
     * 查询订单列表(门店用)
     * @param orderStatus 订单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 订单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    public Page<BizServiceOrder> queryStoreList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT bso.id,bso.service_orderno,bso.order_status,bso.car_vin,bso.customer_name,bso.customer_phone,bso.service_type,bso.report_time,")
            .append(" bso.cur_processor,bso.processor_orgno,bso.processor_orgtype")
            .append(" FROM biz_service_order AS bso LEFT JOIN biz_service_dispatch AS bsd ON bsd.service_orderno = bso.service_orderno WHERE  1=1 ");
        if (StringUtils.isNotBlank(orderStatus)) {
            params.put("orderStatus", orderStatus);
            sql.append(" AND bso.order_status = :orderStatus ");
        }
        if (StringUtils.isNotBlank(serviceType)) {
            params.put("serviceType", serviceType);
            sql.append(" AND bso.service_type = :serviceType ");
        }
        if (StringUtils.isNotBlank(reportOrgno)) {
            params.put("reportOrgno", reportOrgno);
            sql.append(" AND bso.customer_orgno = :reportOrgno");
        }
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
            sql.append(" AND (bso.service_orderno LIKE CONCAT('%',:keyword,'%') or bso.car_vin LIKE CONCAT('%',:keyword,'%') OR ")
                .append(" bso.customer_name LIKE CONCAT('%',:keyword,'%') OR bso.customer_phone LIKE CONCAT('%',:keyword,'%'))");
        }
        sql.append(" AND bso.delete_flag = :deleteFlag ORDER BY bso.operate_time DESC");

        return queryPageForBean(BizServiceOrder.class, sql.toString(), params, offset, pageSize);
    }

    /**
     * 根据商品的code查询物料是否被申请
     * @param productNo 商品的code
     * @return 物料是否被申请
     * @author liuduo
     * @date 2018-08-23 16:01:38
     */
    public Boolean checkProductRelOrder(String productNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productNo", productNo);
        String sql = "SELECT COUNT(id) > 0 FROM biz_serviceorder_detail WHERE product_no = :productNo";
        return findForObject(sql, params, Boolean.class);
    }

    /**
     * 查询维修单状态数量
     * @return 所有维修单状态
     * @author liuduo
     * @date 2018-09-19 17:27:00
     */
    public List<BizServiceOrder> queryOrderStatusNum(String reportOrgno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("reportOrgno", reportOrgno);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT order_status FROM biz_service_order WHERE 1=1");
        if (StringUtils.isNotBlank(reportOrgno) && !(reportOrgno.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM))) {
            params.put("reportOrgno", reportOrgno);
            sql.append(" AND (report_orgno = :reportOrgno OR processor_orgno = :reportOrgno)");
        }

        return queryListBean(BizServiceOrder.class, sql.toString(), params);
    }

    /**
     * 查询维修单状态数量(门店用)
     * @return 维修单各种状态的数量
     * @author liuduo
     * @date 2018-09-19 17:21:44
     */
    public List<BizServiceOrder> queryStoreOrderStatusNum(String reportOrgno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("reportOrgno", reportOrgno);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT order_status FROM biz_service_order WHERE 1=1");
        if (StringUtils.isNotBlank(reportOrgno)) {
            params.put("reportOrgno", reportOrgno);
            sql.append(" AND customer_orgno = :reportOrgno");
        }

        return queryListBean(BizServiceOrder.class, sql.toString(), params);
    }
}
