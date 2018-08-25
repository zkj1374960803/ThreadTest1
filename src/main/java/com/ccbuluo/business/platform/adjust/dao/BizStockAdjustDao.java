package com.ccbuluo.business.platform.adjust.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockAdjust;
import com.ccbuluo.business.entity.BizStockAdjustdetail;
import com.ccbuluo.business.platform.adjust.dto.SearchStockAdjustListDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-14 16:10:21
 * @version V1.0.0
 */
@Repository
public class BizStockAdjustDao extends BaseDao<BizStockAdjust> {
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
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public int saveEntity(BizStockAdjust entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_stock_adjust ( adjust_docno,adjust_orgno,")
            .append("adjust_userid,adjust_time,adjust_reson,adjust_result,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark ) VALUES ( ")
            .append(" :adjustDocno, :adjustOrgno, :adjustUserid, :adjustTime,")
            .append(" :adjustReson, :adjustResult, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public int update(BizStockAdjust entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_adjust SET adjust_docno = :adjustDocno,")
            .append("adjust_orgno = :adjustOrgno,adjust_userid = :adjustUserid,")
            .append("adjust_time = :adjustTime,adjust_reson = :adjustReson,")
            .append("adjust_result = :adjustResult,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public BizStockAdjust getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,adjust_docno,adjust_orgno,adjust_userid,adjust_time,")
            .append("adjust_reson,adjust_result,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_stock_adjust WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizStockAdjust.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_stock_adjust WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }


    /**
     * 查询盘库单列表
     * @param adjustResult 盘库结果
     * @param adjustSource 盘库单来源
     * @param keyWord 关键字（盘库单号/服务中心/客户经理）
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 盘库单列表
     * @author liuduo
     * @date 2018-08-15 11:03:46
     */
    public Page<SearchStockAdjustListDTO> queryAdjustStockList(Integer adjustResult, String adjustSource, String keyWord, Integer offset, Integer pagesize, String orgCode, String productType) {
        Map<String, Object> params = Maps.newHashMap();


        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT bsa.adjust_docno,bsa.adjust_orgno,bsa.adjust_userid,bsa.adjust_time,bsa.adjust_result FROM biz_stock_adjust AS bsa")
            .append(" LEFT JOIN biz_stock_adjustdetail AS bsaa ON bsaa.adjust_docno = bsa.adjust_docno WHERE 1=1");
        if (null != adjustResult) {
            params.put("adjustResult", adjustResult);
            if (adjustResult.equals(Constants.FLAG_ONE)) {
                sql.append(" AND bsa.adjust_result > 0");
            } else {
                sql.append(" AND bsa.adjust_result = 0");
            }
        }
        if (StringUtils.isNotBlank(adjustSource)) {
            params.put("adjustSource", adjustSource);
            sql.append(" AND bsa.adjust_orgno = :adjustSource");
        }
        if (StringUtils.isNotBlank(keyWord)) {
            params.put("keyWord", keyWord);
            sql.append(" AND (bsa.adjust_orgno LIKE CONCAT('%',:keyWord,'%') OR bsa.adjust_docno LIKE CONCAT('%',:keyWord,'%'))");
        }
        if (StringUtils.isNotBlank(orgCode) && !(orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM))) {
            params.put("orgCode", orgCode);
            sql.append(" AND bsa.adjust_orgno = :orgCode");
        }
        if (StringUtils.isNotBlank(productType)) {
            params.put("productType", productType);
            sql.append(" AND bsaa.product_type = :productType");
        }
        sql.append("  ORDER BY bsa.operate_time DESC");

        return queryPageForBean(SearchStockAdjustListDTO.class, sql.toString(), params, offset, pagesize);
    }

    /**
     * 根据盘库单号查询盘库详情
     * @param adjustNo 盘库单号
     * @return 盘库详情
     * @author liuduo
     * @date 2018-08-15 14:37:37
     */
    public StockAdjustDetailDTO getByAdjustNo(String adjustNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("adjustNo", adjustNo);

        String sql = "SELECT adjust_docno,adjust_orgno,adjust_userid,adjust_time,adjust_reson FROM biz_stock_adjust WHERE adjust_docno = :adjustNo";

        return findForBean(StockAdjustDetailDTO.class, sql, params);
    }
}
