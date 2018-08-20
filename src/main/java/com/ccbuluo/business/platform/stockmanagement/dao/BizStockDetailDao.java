package com.ccbuluo.business.platform.stockmanagement.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.stockmanagement.dto.FindProductDetailDTO;
import com.ccbuluo.business.platform.stockmanagement.dto.FindStockDetailDTO;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存
 * @author zhangkangjian
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizStockDetailDao extends BaseDao<AllocateApplyDTO> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }
    /**
     * 查询库存的详情
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindStockDetailDTO 库存的基本信息
     * @author zhangkangjian
     * @date 2018-08-20 11:15:52
     */
    public FindStockDetailDTO findStockDetail(String productNo, String productType) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.product_no,SUM(a.valid_stock + a.occupy_stock) AS 'totalStock', ")
            .append(" SUM(a.valid_stock + a.occupy_stock) * a.cost_price AS 'totalAmount', ")
            .append(" a.product_name AS 'productName',a.product_categoryname AS 'productCategoryname',a.product_name AS 'unit' ")
            .append(" FROM biz_stock_detail a WHERE a.product_no = :productNo AND a.product_type = :productType")
            .append(" GROUP BY a.product_no ");
        return findForBean(FindStockDetailDTO.class, sql.toString(), map);
    }

    /**
     * 查询正常件的库存
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public FindProductDetailDTO findProductDetail(String productNo, String productType, List<QueryOrgDTO> orgDTOList) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.valid_stock,a.occupy_stock,SUM(a.valid_stock + a.occupy_stock) AS total, ")
            .append(" SUM(a.valid_stock + a.occupy_stock) * a.cost_price AS 'totalAmount',a.product_unit ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        sql.append(" GROUP BY a.product_no ");
        return findForBean(FindProductDetailDTO.class, sql.toString(), map);
    }
    /**
     * 查询可调拨库存的数量
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @param sellerOrgno 库存来源
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public Long findTransferInventory(String productNo, String productType, List<QueryOrgDTO> orgDTOList, String sellerOrgno) {
        if(StringUtils.isNoneBlank(productNo, productType, sellerOrgno)){
            return NumberUtils.LONG_ZERO;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        map.put("sellerOrgno", sellerOrgno);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(a.valid_stock) FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType AND a.seller_orgno = :sellerOrgno ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        sql.append(" GROUP BY a.product_no ");
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, Long.class);
    }

    /**
     * 查询问题件
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public FindProductDetailDTO findProblemStock(String productNo, String productType, List<QueryOrgDTO> orgDTOList) {
        if(StringUtils.isNoneBlank(productNo, productType)){
            new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(a.problem_stock) AS 'totalStock',SUM(a.problem_stock) * a.cost_price AS 'totalAmount',a.product_name AS 'unit' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType  ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        sql.append(" GROUP BY a.product_no ");
        return findForBean(FindProductDetailDTO.class, sql.toString(), map);
    }
    /**
     * 查询损坏件
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public FindProductDetailDTO findDamagedStock(String productNo, String productType, List<QueryOrgDTO> orgDTOList) {
        if(StringUtils.isNoneBlank(productNo, productType)){
            new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(a.damaged_stock) AS 'totalStock',SUM(a.damaged_stock) * a.cost_price AS 'totalAmount',a.product_name AS 'unit' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType  ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        sql.append(" GROUP BY a.product_no ");
        return findForBean(FindProductDetailDTO.class, sql.toString(), map);
    }
}
