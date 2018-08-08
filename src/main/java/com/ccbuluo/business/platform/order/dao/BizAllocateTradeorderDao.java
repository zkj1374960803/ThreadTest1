package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.thrid.BizAllocateTradeorder;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 调拨申请交易订单 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizAllocateTradeorderDao extends BaseDao<BizAllocateTradeorder> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 调拨申请交易订单实体
     * @param entity 调拨申请交易订单实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizAllocateTradeorder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocate_tradeorder ( order_no,apply_no,")
            .append("purchaser_orgno,seller_orgno,order_status,total_price,payer,")
            .append("pay_method,payed_time,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :orderNo, :applyNo, :purchaserOrgno,")
            .append(" :sellerOrgno, :orderStatus, :totalPrice, :payer, :payMethod,")
            .append(" :payedTime, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 调拨申请交易订单实体
     * @param entity 调拨申请交易订单实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizAllocateTradeorder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocate_tradeorder SET order_no = :orderNo,")
            .append("apply_no = :applyNo,purchaser_orgno = :purchaserOrgno,")
            .append("seller_orgno = :sellerOrgno,order_status = :orderStatus,")
            .append("total_price = :totalPrice,payer = :payer,pay_method = :payMethod,")
            .append("payed_time = :payedTime,creator = :creator,create_time = :createTime,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("delete_flag = :deleteFlag,remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取调拨申请交易订单详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizAllocateTradeorder getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,apply_no,purchaser_orgno,seller_orgno,")
            .append("order_status,total_price,payer,pay_method,payed_time,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark")
            .append(" FROM biz_allocate_tradeorder WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizAllocateTradeorder.class, sql.toString(), params);
    }

    /**
     * 删除调拨申请交易订单
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_allocate_tradeorder WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
