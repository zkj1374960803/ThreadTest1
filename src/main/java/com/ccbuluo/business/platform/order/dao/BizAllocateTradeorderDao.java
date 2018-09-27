package com.ccbuluo.business.platform.order.dao;


import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizAllocateTradeorder;
import com.ccbuluo.business.entity.RelOrdstockOccupy;
import com.ccbuluo.business.platform.allocateapply.dto.PerpayAmountDTO;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
     * @param applyNo 申请单号
     * @author zhangkangjian
     * @date 2018-08-07 11:55:41
     */
    public List<BizAllocateTradeorder> queryAllocateTradeorderByApplyNo(String applyNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,apply_no,purchaser_orgno,seller_orgno,")
            .append("order_status,total_price,payer,pay_method,payed_time,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark")
            .append(" FROM biz_allocate_tradeorder WHERE apply_no = :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        return queryListBean(BizAllocateTradeorder.class, sql.toString(), params);
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
    /**
     * 批量新增订单
     * @param list 订单列表
     * @exception
     * @author weijb
     * @Date 2018-08-08 17:37:32
     */
    public List<Long> batchInsertAllocateTradeorder(List<BizAllocateTradeorder> list){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocate_tradeorder ( order_no,apply_no,")
                .append("purchaser_orgno,seller_orgno,order_status,total_price,payer,")
                .append("pay_method,payed_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,trade_type,perpay_amount ) VALUES (  :orderNo, :applyNo, :purchaserOrgno,")
                .append(" :sellerOrgno, :orderStatus, :totalPrice, :payer, :payMethod,")
                .append(" :payedTime, :creator, :createTime, :operator, :operateTime,")
                .append(" :deleteFlag, :remark ,:tradeType, :perpayAmount)");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), list);
        return longs;
    }

    /**
     * 批量新增订单占用库存关系
     * @param list 订单占用库存关系
     * @exception
     * @author weijb
     * @Date 2018-08-08 17:37:32
     */
    public List<Long> batchInsertRelOrdstockOccupy(List<RelOrdstockOccupy> list){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO rel_ordstock_occupy ( order_type,doc_no,")
                .append("stock_id,occupy_num,occupy_status,occupy_starttime,occupy_endtime,")
                .append("creator,create_time,delete_flag )")
                .append(" VALUES (  :orderType, :docNo, :stockId,")
                .append(" :occupyNum, :occupyStatus, :occupyStarttime, :occupyEndtime, :creator,")
                .append(" :createTime, :deleteFlag )");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), list);
        return longs;
    }

    /**
     * 根据申请单编号查询订单占用库存关系表
     * @param applyNo  applyNo
     * @return 影响条数
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<RelOrdstockOccupy> getRelOrdstockOccupyByApplyNo(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT order_type,doc_no,stock_id,occupy_num,occupy_status,occupy_starttime, ")
                .append("occupy_endtime,creator,create_time,delete_flag ")
                .append(" FROM rel_ordstock_occupy WHERE doc_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        return super.queryListBean(RelOrdstockOccupy.class, sql.toString(), params);
    }

    /**
     * 根据申请单编号查询订单占用库存关系表
     * @param applyNo  applyNo
     * @return 影响条数
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<RelOrdstockOccupy> getRelOrdstockOccupyBy(String applyNo,String orgCode){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.order_type,o.doc_no,o.stock_id,o.occupy_num,o.occupy_status,o.occupy_starttime, ")
                .append("o.occupy_endtime,o.creator,o.create_time,o.delete_flag,s.supplier_no,s.cost_price,s.product_no ")
                .append(" FROM rel_ordstock_occupy o,biz_stock_detail s WHERE o.stock_id=s.id and s.org_no= :orgCode and o.doc_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("orgCode", orgCode);
        return super.queryListBean(RelOrdstockOccupy.class, sql.toString(), params);
    }

    /**
     * 根据申请单编号删除订单占用库存关系表
     * @param applyNo  申请单编号
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteRelOrdstockOccupyByApplyNo(String applyNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE rel_ordstock_occupy SET delete_flag = :deleteFlag  WHERE doc_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 删除订单
     * @param applyNo 订单编号
     * @exception
     * @author weijb
     * @Date 2018-08-13 17:37:32
     */
    public int deleteAllocateTradeorderByApplyNo(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocate_tradeorder SET delete_flag = :deleteFlag  WHERE apply_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据申请编号更新交易单的所有商品总价
     * @param applyNo 申请编号
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int updateTradeorderInfo(String applyNo, BigDecimal totalPrice) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocate_tradeorder SET total_price = :totalPrice")
                .append(" WHERE apply_no = :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("totalPrice", totalPrice);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据维修单号删除关系
     * @param serviceOrderno 维修单号
     * @author liuduo
     * @date 2018-09-06 19:55:43
     */
    public int deleteRelation(String serviceOrderno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);

        String sql = "DELETE FROM rel_ordstock_occupy WHERE doc_no = :serviceOrderno";

        return updateForMap(sql, params);
    }

    /**
     * 根据申请编号更新交易单的所有商品总价
     * @param applyNo 申请编号
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int updateTradeorderStatus(String applyNo, String orderStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocate_tradeorder SET order_status = :orderStatus")
                .append(" WHERE apply_no = :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("orderStatus", orderStatus);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据申请单编号查询交易单列表
     * @param applyNo  applyNo
     * @return 影响条数
     * @author weijb
     * @date 2018-09-13 09:55:41
     */
    public List<BizAllocateTradeorder> getAllocateTradeorderByApplyNo(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,apply_no,purchaser_orgno,seller_orgno,")
                .append("order_status,total_price,payer,pay_method,payed_time,creator,")
                .append("create_time,operator,operate_time,delete_flag,remark,perpay_amount")
                .append(" FROM biz_allocate_tradeorder WHERE apply_no= :applyNo AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListBean(BizAllocateTradeorder.class, sql.toString(), params);
    }

    /**
     * 查询采购单的付款信息
     * @param applyNo 采购单号
     * @return List<PerpayAmountDTO>
     * @author zhangkangjian
     * @date 2018-09-13 11:17:58
     */
    public List<PerpayAmountDTO> queryPaymentInfo(String applyNo) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.supplier_code,b.supplier_name,a.perpay_amount,a.total_price  ")
            .append(" FROM biz_allocate_tradeorder a  ")
            .append(" LEFT JOIN biz_service_supplier b ON a.seller_orgno = b.supplier_code ")
            .append(" WHERE 1 = 1 AND a.delete_flag = :deleteFlag");
        if(StringUtils.isNotBlank(applyNo)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        return queryListBean(PerpayAmountDTO.class, sql.toString(), map);
    }

    /**
     * 根据维修单编号和当前登录人查询占用关系
     * @param serviceOrderno 维修单编号
     * @param orgCode 当前登录人的机构code
     * @return 占用关系
     * @author liuduo
     * @date 2018-09-15 18:09:59
     */
    public List<RelOrdstockOccupy> getRelOrdstockOccupy(String serviceOrderno, String orgCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        params.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT roo.order_type,roo.doc_no,roo.stock_id,roo.occupy_num,roo.occupy_status,roo.occupy_starttime,roo.occupy_endtime,")
            .append(" roo.creator,roo.create_time,roo.delete_flag FROM rel_ordstock_occupy AS roo ")
            .append(" WHERE roo.doc_no = :serviceOrderno");

        return queryListBean(RelOrdstockOccupy.class, sql.toString(), params);
    }
}
