package com.ccbuluo.business.platform.outstock.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.entity.BizOutstockOrder;
import com.ccbuluo.business.entity.BizOutstockorderDetail;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  出库单dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizOutstockOrderDao extends BaseDao<BizOutstockOrder> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }
    @Autowired
    private UserHolder userHolder;

    /**
     * 保存出库单实体
     * @param entity 实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizOutstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstock_order ( outstockorder_no,out_repository_no,outstock_orgno,")
            .append("outstock_operator,trade_docno,outstock_type,outstock_time,")
            .append("transportorder_no,checked,checked_time,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :outstockorderNo,")
            .append(" :outRepositoryNo, :outstockOrgno, :outstockOperator, :tradeDocno, :outstockType,")
            .append(" :outstockTime, :transportorderNo, :checked, :checkedTime, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }


    /**
     * 分页查询出库单列表
     * @param productType 商品类型
     * @param outstockType 入库类型
     * @param outstockNo 入库单号
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    public Page<BizOutstockOrder> queryOutstockList(String productType, String outstockType, String outstockNo, String orgCode, Integer offset, Integer pagesize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productType", productType);
        params.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT boo.id,boo.outstockorder_no,boo.outstock_type,boo.outstock_time,boo.trade_docno,boo.outstock_operator")
            .append("  FROM biz_outstock_order AS boo LEFT JOIN biz_outstockorder_detail AS bod ON bod.outstock_orderno = boo.outstockorder_no")
            .append("   WHERE bod.product_type = :productType AND boo.outstock_orgno = :orgCode");
        if (StringUtils.isNotBlank(outstockType)) {
            params.put("outstockType", outstockType);
            sql.append(" AND  boo.outstock_type = :outstockType");
        }
        if (StringUtils.isNotBlank(outstockNo)) {
            params.put("outstockNo", outstockNo);
            if (userHolder.getLoggedUser().getOrganization().getOrgCode().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                sql.append(" AND  (boo.outstockorder_no like CONCAT('%',:outstockNo,'%') OR boo.trade_docno like CONCAT('%',:outstockNo,'%'))");
            } else {
                sql.append(" AND  boo.outstockorder_no like CONCAT('%',:outstockNo,'%')");
            }
        }
        sql.append(" ORDER BY boo.operate_time DESC");

        return queryPageForBean(BizOutstockOrder.class, sql.toString(), params, offset, pagesize);

    }

    /**
     * 根据出库单号查询出库单详情
     * @param outstockNo 出库单号
     * @return 出库单详情
     * @author liuduo
     * @date 2018-08-13 11:14:39
     */
    public BizOutstockOrderDTO getByOutstockNo(String outstockNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("outstockNo", outstockNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstockorder_no,outstock_type,outstock_time,trade_docno,outstock_operator,transportorder_no")
            .append(" FROM biz_outstock_order WHERE outstockorder_no = :outstockNo");

        return findForBean(BizOutstockOrderDTO.class, sql.toString(), params);
    }

    /**
     * 保存出库单集合
     * @param bizOutstockOrderList 出库单集合
     * @return
     * @author liuduo
     * @date 2018-08-15 20:15:50
     */
    public List<Long> batchBizOutstockOrder(List<BizOutstockorderDetail> bizOutstockOrderList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstock_order ( outstockorder_no,out_repository_no,outstock_orgno,")
            .append("outstock_operator,trade_docno,outstock_type,outstock_time,")
            .append("transportorder_no,checked,checked_time,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :outstockorderNo,")
            .append(" :outRepositoryNo, :outstockOrgno, :outstockOperator, :tradeDocno, :outstockType,")
            .append(" :outstockTime, :transportorderNo, :checked, :checkedTime, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");

        return batchInsertForListBean(sql.toString(), bizOutstockOrderList);
    }

    /**
     * 根据申请单号查询出库单详情
     * @param tradeDocno 申请单号
     * @return 出库单详情
     * @author weijb
     * @date 2018-08-20 15:14:39
     */
    public BizOutstockOrderDTO getByTradeDocno(String tradeDocno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("tradeDocno", tradeDocno);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstockorder_no,outstock_type,outstock_time,trade_docno,outstock_operator,transportorder_no")
                .append(" FROM biz_outstock_order WHERE trade_docno = :tradeDocno");

        return findForBean(BizOutstockOrderDTO.class, sql.toString(), params);
    }
}
