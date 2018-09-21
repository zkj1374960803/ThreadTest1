package com.ccbuluo.business.platform.instock.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
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
import java.util.Date;
import java.util.Map;

/**
 *  入库单dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizInstockOrderDao extends BaseDao<BizInstockOrder> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }
    @Autowired
    private UserHolder userHolder;


    /**
     * 根据申请单号查询出库单详情
     * @param tradeDocno 申请单号
     * @return 出库单详情
     * @author weijb
     * @date 2018-08-20 15:14:39
     */
    public BizInstockOrderDTO getByTradeDocno(String tradeDocno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("tradeDocno", tradeDocno);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_orderno,instock_orgno,trade_docno,instock_type,instock_time,instock_operator,transportorder_no")
            .append("  FROM biz_instock_order WHERE trade_docno = :tradeDocno");
        return findForBean(BizInstockOrderDTO.class, sql.toString(), params);
    }
    /**
     * 保存 实体
     * @param entity 实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizInstockOrder entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instock_order ( instock_orderno,trade_docno,")
            .append("in_repository_no,instock_orgno,instock_operator,instock_type,instock_time,")
            .append("transportorder_no,checked,checked_time,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :instockOrderno,")
            .append(" :tradeDocno, :inRepositoryNo, :instockOrgno, :instockOperator, :instockType,")
            .append(" :instockTime, :transportorderNo, :checked, :checkedTime, :creator,")
            .append(" :createTime, :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 分页查询入库单列表
     * @param productType 商品类型
     * @param instockType 入库类型
     * @param instockNo 入库单号
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    public Page<BizInstockOrder> queryInstockList(String productType, String instockType, String instockNo, String orgCode, Integer offset, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productType", productType);
        params.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT bio.id,bio.instock_orderno,bio.instock_type,bio.instock_time,bio.trade_docno,bio.instock_operator,baa.apply_type")
            .append("  FROM biz_instock_order AS bio LEFT JOIN biz_instockorder_detail AS bid ON bid.instock_orderno = bio.instock_orderno")
            .append("  LEFT JOIN biz_allocate_apply AS baa ON baa.apply_no = bio.trade_docno")
            .append("    WHERE bid.product_type = :productType AND bio.instock_orgno = :orgCode");
        if (StringUtils.isNotBlank(instockType)) {
            params.put("instockType", instockType);
            sql.append(" AND  bio.instock_type = :instockType");
        }
        if (StringUtils.isNotBlank(instockNo)) {
            params.put("instockNo", instockNo);
            if (userHolder.getLoggedUser().getOrganization().getOrgCode().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                sql.append(" AND  (bio.instock_orderno like CONCAT('%',:instockNo,'%') OR bio.trade_docno like CONCAT('%',:instockNo,'%'))");
            } else {
                sql.append(" AND  bio.instock_orderno like CONCAT('%',:instockNo,'%')");
            }
        }
        sql.append(" ORDER BY bio.operate_time DESC");

        return queryPageForBean(BizInstockOrder.class, sql.toString(), params, offset, pageSize);
    }

    /**
     * 根据入库单号查询入库单详情
     * @param instockNo 入库单号
     * @return 入库单详情
     * @author liuduo
     * @date 2018-08-13 15:38:42
     */
    public BizInstockOrderDTO getByInstockNo(String instockNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("instockNo", instockNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_orderno,trade_docno,instock_type,instock_time,instock_operator,transportorder_no")
            .append("  FROM biz_instock_order WHERE instock_orderno = :instockNo");

        return findForBean(BizInstockOrderDTO.class, sql.toString(), params);
    }

    /**
     * 更改入库单复核状态
     * @param instockNo 入库单
     * @param checked 复核状态
     * @param date 日期
     * @author liuduo
     * @date 2018-08-29 15:17:46
     */
    public void updateChecked(String instockNo, Integer checked, Date date) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("instockNo", instockNo);
        params.put("checked", checked);
        params.put("date", date);

        String sql = "UPDATE biz_instock_order SET checked = :checked,checked_time = :date WHERE  instock_orderno = :instockNo";

        updateForMap(sql, params);
    }
}
