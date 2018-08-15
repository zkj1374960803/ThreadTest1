package com.ccbuluo.business.platform.stockdetail.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.UpdateStockBizStockDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 问题件库存 dao
 * @author weijb
 * @date 2018-08-15 08:40:41
 * @version V1.0.0
 */
@Repository
public class ProblemStockDetailDao extends BaseDao<BizStockDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String productType, String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT bsd.id,bsd.product_no,bsd.product_name,bsd.product_type,bse.equip_unit,SUM(bsd.problem_stock) as problem_stock")
                .append(" FROM biz_stock_detail bsd LEFT JOIN biz_service_equipment bse on bse.equip_code=bsd.product_no ")
                .append(" WHERE bsd.delete_flag = :deleteFlag and bsd.problem_stock>0 ");
        // 物料类型
        if (null != productType) {
            param.put("productType", productType);
            sql.append(" AND bsd.product_type = :productType ");
        }
        // 关键字查询
        if (StringUtils.isNotBlank(keyword)) {
            param.put("keyword", keyword);
            //根据编号或名称查询
            sql.append(" AND (bsd.product_no LIKE CONCAT('%',:keyword,'%') OR bsd.product_name LIKE CONCAT('%',:keyword,'%'))");
        }

        sql.append(" GROUP BY bsd.product_no ORDER BY bsd.create_time ");
        Page<StockBizStockDetailDTO> DTOS = super.queryPageForBean(StockBizStockDetailDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }
}
