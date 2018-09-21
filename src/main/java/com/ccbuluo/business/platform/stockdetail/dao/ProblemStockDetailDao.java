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
     *  @param type 物料或是零配件
     * @param orgCode 组织机构code
     * @param productNames 物料或是零配件名称
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(boolean category, String type, String orgCode, List<String> productNames, String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t1.id,t1.product_no,t1.product_name,t1.product_type,t1.product_unit,t1.problem_stock as problem_stock,t1.product_categoryname,t2.checked_time as outstock_time,t3.checked_time as instock_time ")
                .append(" from (SELECT id,product_no,product_name,product_type,product_unit,SUM(problem_stock) as problem_stock,product_categoryname,trade_no,create_time,org_no,delete_flag FROM biz_stock_detail WHERE delete_flag = 0 and problem_stock>0 ");
                if(StringUtils.isNotBlank(orgCode)){
                    param.put("orgCode", orgCode);
                    sql.append(" and org_no = :orgCode");
                }
                sql.append(" GROUP BY product_no) as t1  ")
                .append(" LEFT JOIN biz_outstock_order t2 on t1.trade_no=t2.trade_docno LEFT JOIN biz_instock_order t3 on t1.trade_no=t3.trade_docno  ")
                .append(" WHERE t1.delete_flag = :deleteFlag and t1.problem_stock>0 ");
        // 物料类型
        if (null != productNames && productNames.size() > 0) {
            param.put("productNames", productNames);
            sql.append(" AND t1.product_name IN(:productNames)");
        }
        // 关键字查询
        if (StringUtils.isNotBlank(keyword)) {
            param.put("keyword", keyword);
            //根据编号或名称查询
            sql.append(" AND (t1.product_no LIKE CONCAT('%',:keyword,'%') OR t1.product_name LIKE CONCAT('%',:keyword,'%'))");
        }
        // 区分零配件和物料
        if (StringUtils.isNotBlank(type)) {
            param.put("type", type);
            sql.append(" AND t1.product_type = :type ");
        }

        sql.append(" GROUP BY t1.product_no ORDER BY t1.create_time DESC");
        // 如果选择了类型，并且这个类型下没有查询到商品就返回null
        if(category && (null == productNames || productNames.size() == 0)){
            return new Page<>(offset,pageSize);
        }
        return super.queryPageForBean(StockBizStockDetailDTO.class, sql.toString(), param,offset,pageSize);
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
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        param.put("productNo", productNo);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t1.id,t1.trade_no,t2.instock_time,t1.supplier_no,t1.problem_stock,t1.product_unit,ss.supplier_name ")
                .append(" FROM biz_stock_detail t1 LEFT JOIN biz_instock_order t2 on t1.trade_no=t2.trade_docno ")
                .append(" LEFT JOIN  biz_service_supplier ss ON t1.supplier_no = ss.supplier_code ")
                .append(" WHERE t1.delete_flag = :deleteFlag and t1.product_no = :productNo and t1.problem_stock>0 ");
        if (StringUtils.isNotBlank(orgCode)) {
            param.put("orgCode", orgCode);
            sql.append(" and t1.org_no= :orgCode ");
        }
        sql.append(" GROUP BY t1.id  ORDER BY t1.create_time DESC");
        return super.queryListBean(StockDetailDTO.class, sql.toString(), param);
    }

    /**
     *  根据组织机构获取问题件商品详情
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-03 10:22:11
     */
    public List<StockDetailDTO> queryStockDetailListByAppNo(String orgCode){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        param.put("orgCode", orgCode);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT id,trade_no,product_no,cost_price")
                .append(" FROM biz_stock_detail ")
                .append(" WHERE delete_flag = :deleteFlag and org_no= :orgCode and problem_stock>0");
        sql.append(" GROUP BY id ");
        return super.queryListBean(StockDetailDTO.class, sql.toString(), param);
    }

    /**
     * 查询本机构所有零配件的问题库存
     * @param orgCode 组织机构code
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    public List<StockBizStockDetailDTO> getStockBizStockList(String orgCode,List<String> codes){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        param.put("orgCode", orgCode);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,product_no,product_name,product_type,product_unit,SUM(valid_stock) as valid_stock,SUM(problem_stock) as problem_stock,product_categoryname,trade_no,create_time,org_no,delete_flag ")
                .append(" FROM biz_stock_detail WHERE delete_flag = 0  and org_no = :orgCode ");
        if(codes != null && codes.size() > 0){
            param.put("codes", codes);
            sql.append(" AND product_no IN(:codes)  ");
        }
        sql.append(" GROUP BY product_no");
        return super.queryListBean(StockBizStockDetailDTO.class, sql.toString(), param);
    }
}
