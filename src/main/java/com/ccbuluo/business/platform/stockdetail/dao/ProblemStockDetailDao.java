package com.ccbuluo.business.platform.stockdetail.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
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
     * @param productType 物料类型
     * @param codes 零配件codes
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String productType, List<String> codes, String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT id,product_no,product_name,product_type,product_unit,SUM(problem_stock) as problem_stock")
                .append(" FROM biz_stock_detail  WHERE delete_flag = :deleteFlag and problem_stock>0 ");
        // 物料类型
        if (StringUtils.isNotBlank(productType)) {
            param.put("productType", productType);
            sql.append(" AND product_type = :productType ");
        }
        // 零配件codes
        if (null != codes && codes.size() > 0) {
            param.put("codes", codes);
            sql.append(" AND product_no IN(:codes) ");
        }
        // 关键字查询
        if (StringUtils.isNotBlank(keyword)) {
            param.put("keyword", keyword);
            //根据编号或名称查询
            sql.append(" AND (product_no LIKE CONCAT('%',:keyword,'%') OR product_name LIKE CONCAT('%',:keyword,'%'))");
        }

        sql.append(" GROUP BY product_no ORDER BY create_time DESC");
        Page<StockBizStockDetailDTO> DTOS = super.queryPageForBean(StockBizStockDetailDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }

    public StockBizStockDetailDTO getProdectStockBizStockDetailByCode(String productNo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,product_no,product_name,product_type,product_unit,SUM(problem_stock) as problem_stock")
                .append(" FROM biz_stock_detail LEFT WHERE delete_flag = :deleteFlag and product_no = :productNo and problem_stock>0 ")
                .append(" ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("productNo", productNo);
        return super.findForBean(StockBizStockDetailDTO.class, sql.toString(), params);
    }
}
