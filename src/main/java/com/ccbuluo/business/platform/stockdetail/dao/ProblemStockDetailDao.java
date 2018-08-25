package com.ccbuluo.business.platform.stockdetail.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
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
     *  @param type 物料或是零配件
     * @param orgCode 组织机构code
     * @param productCategory 物料类型
     * @param codes 零配件codes
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String type, String orgCode, String productCategory, List<String> codes, String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t1.id,t1.product_no,t1.product_name,t1.product_type,t1.product_unit,SUM(t1.problem_stock) as problem_stock,t1.product_categoryname,t2.checked_time as outstock_time,t3.checked_time as instock_time ")
                .append(" FROM biz_stock_detail t1 LEFT JOIN biz_outstock_order t2 on t1.trade_no=t2.trade_docno ")
                .append(" LEFT JOIN biz_instock_order t3 on t1.trade_no=t3.trade_docno ")
                .append(" WHERE t1.delete_flag = :deleteFlag and t1.problem_stock>0 ");
        // 物料类型
        if (StringUtils.isNotBlank(productCategory)) {
            param.put("productCategory", productCategory);
            sql.append(" AND t1.product_categoryname = :productCategory ");
        }
        // 零配件codes
        if (null != codes && codes.size() > 0) {
            param.put("codes", codes);
            sql.append(" AND t1.product_no IN(:codes) ");
        }
        // 关键字查询
        if (StringUtils.isNotBlank(keyword)) {
            param.put("keyword", keyword);
            //根据编号或名称查询
            sql.append(" AND (t1.product_no LIKE CONCAT('%',:keyword,'%') OR t1.product_name LIKE CONCAT('%',:keyword,'%'))");
        }
        // 组织机构code
        if (StringUtils.isNotBlank(orgCode)) {
            param.put("orgCode", orgCode);
            sql.append(" AND t1.org_no = :orgCode ");
        }
        // 区分零配件和物料
        if (StringUtils.isNotBlank(type)) {
            param.put("type", type);
            sql.append(" AND t1.product_type = :type ");
        }

        sql.append(" GROUP BY t1.product_no ORDER BY t1.create_time DESC");
        Page<StockBizStockDetailDTO> DTOS = super.queryPageForBean(StockBizStockDetailDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }

    /**
     * 根据物料code查询某个物料在当前登录机构的问题件库存
     *  @param type 物料或是零配件
     * @param orgCode 当前机构编号
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    public Page<StockBizStockDetailDTO> getProdectStockBizStockDetailByCode(String type, String orgCode, String productNo, Integer offset, Integer pageSize){
        // 用于调试TODO--------start
        orgCode = "SC000001";
        productNo = "FP000004600";
        // 用于调试TODO--------end

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t1.id,t1.product_no,t1.product_name,t1.product_type,t1.product_unit,SUM(t1.problem_stock) as problem_stock,t2.checked_time as outstock_time,t3.checked_time as instock_time ")
                .append(" FROM biz_stock_detail t1 LEFT JOIN biz_outstock_order t2 on t1.trade_no=t2.trade_docno ")
                .append(" LEFT JOIN biz_instock_order t3 on t1.trade_no=t3.trade_docno ")
                .append(" WHERE t1.delete_flag = :deleteFlag and t1.product_no = :productNo and t1.problem_stock>0 ");
        Map<String, Object> params = Maps.newHashMap();
        // 组织机构code
        if (StringUtils.isNotBlank(orgCode)) {
            params.put("orgCode", orgCode);
            sql.append(" AND t1.org_no = :orgCode ");
        }
        // 区分零配件和物料
        if (StringUtils.isNotBlank(type)) {
            params.put("type", type);
            sql.append(" AND t1.product_type = :type ");
        }
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("productNo", productNo);
        Page<StockBizStockDetailDTO> DTOS = super.queryPageForBean(StockBizStockDetailDTO.class, sql.toString(), params,offset,pageSize);
        return DTOS;
    }

    /**
     * 查询问题库存详情
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    public ProblemStockBizStockDetailDTO getProblemStockDetail(Long id) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,product_no,product_name,product_type,product_categoryname,problem_stock,product_unit")
                .append(" FROM biz_stock_detail  WHERE id = :id ");
        return findForBean(ProblemStockBizStockDetailDTO.class, sql.toString(), map);
    }

    /**
     * 查询某个商品的库存入库记录
     * @param orgCode 组织机构code
     * @param productNo 商品code
     * @author weijb
     * @date 2018-08-23 16:59:51
     */
    public List<StockDetailDTO> queryProblemStockBizStockList(String orgCode, String productNo){

        // 用于调试TODO--------start
        orgCode = "SC000001";
        productNo = "FP000004600";
        // 用于调试TODO--------end
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        param.put("orgCode", orgCode);
        param.put("productNo", productNo);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t1.id,t1.trade_no,t2.instock_time,t1.supplier_no,t1.problem_stock,t1.product_unit ")
                .append(" FROM biz_stock_detail t1 LEFT JOIN biz_instock_order t2 on t1.trade_no=t2.trade_docno ")
                .append(" WHERE t1.delete_flag = :deleteFlag and t1.product_no = :productNo and t1.org_no= :orgCode and t1.problem_stock>0 ");
        return super.queryListBean(StockDetailDTO.class, sql.toString(), param);
    }
}
