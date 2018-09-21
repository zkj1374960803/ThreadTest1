package com.ccbuluo.business.platform.stockdetail.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.stockdetail.dto.UpdateStockBizStockDetailDTO;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizStockDetailDao extends BaseDao<BizStockDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
     * @param entity 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public Long saveEntity(BizStockDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_stock_detail ( repository_no,org_no,product_no,product_name,product_unit,product_categoryname,")
            .append("product_type,trade_no,supplier_no,valid_stock,occupy_stock,")
            .append("problem_stock,damaged_stock,transit_stock,freeze_stock,seller_orgno,")
            .append("cost_price,instock_planid,latest_correct_time,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :repositoryNo,")
            .append(" :orgNo, :productNo, :productName,:productUnit,:productCategoryname, :productType, :tradeNo, :supplierNo,")
            .append(" :validStock, :occupyStock, :problemStock, :damagedStock,")
            .append(" :transitStock, :freezeStock, :sellerOrgno, :costPrice,")
            .append(" :instockPlanid, :latestCorrectTime, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag, :remark )");
        return super.saveRid(sql.toString(), entity);
    }



    /**
     * 根据入库详单的  供应商、商品、仓库、批次号  查询在库存中有无记录
     * @param supplierNo 供应商
     * @param productNo 商品编号
     * @param inRepositoryNo 入库仓库编号
     * @param applyNo 交易单号
     * @return 库存明细id
     * @author liuduo
     * @date 2018-08-08 14:55:43
     */
    public Long getByinstockorderDeatil(String supplierNo, String productNo, BigDecimal costPrice, String inRepositoryNo, String applyNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierNo", supplierNo);
        params.put("productNo", productNo);
        params.put("inRepositoryNo", inRepositoryNo);
        params.put("applyNo", applyNo);
        params.put("costPrice", costPrice);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id FROM biz_stock_detail WHERE supplier_no = :supplierNo AND repository_no = :inRepositoryNo")
            .append(" AND trade_no = :applyNo AND product_no = :productNo AND cost_price = :costPrice");

        return findForObject(sql.toString(), params, Long.class);
    }



//    public List<UpdateNumByVersionNo> getByinstockorderDeatil1(List<InstockOrderServiceImpl.BatchUpdateDto> batchUpdateDtos) {
//        Map<String, Object> params = Maps.newHashMap();
//
//        StringBuilder sql = new StringBuilder("SELECT id,version_no FROM biz_stock_detail WHERE 1=1");
//        for (int i = 0;i<batchUpdateDtos.size();i++) {
//            InstockOrderServiceImpl.BatchUpdateDto batchUpdateDto = batchUpdateDtos.get(i);
////            sql.append(" AND (supplier_no = :supplierNo+"+i+" AND repository_no = :inRepositoryNo"+i+"  AND trade_no = :applyNo"+i+" AND product_no = :productNo"+i+")");
//            sql.append(" AND (supplier_no = :supplierNo+").append(i)
//                .append(" AND repository_no = :inRepositoryNo").append(i)
//                .append(" AND trade_no = :applyNo").append(i)
//                .append(" AND product_no = :productNo").append(i).append(")");
//            params.put("supplierNo"+i, batchUpdateDto.getSupplierNo());
//            params.put("productNo"+i, batchUpdateDto.getProductNo());
//            params.put("inRepositoryNo"+i, batchUpdateDto.getInRepositoryNo());
//            params.put("applyNo"+i, batchUpdateDto.getApplyNo());
//            if (i < batchUpdateDtos.size() - 1) {
//                sql.append(" OR ");
//            }
//        }
//
//        return queryListBean(UpdateNumByVersionNo.class, sql.toString(), params);
//    }

    /**
     * 修改有效库存
     * @param bizStockDetail 库存明细
     * @author liuduo
     * @date 2018-08-08 15:24:09
     */
    public void updateValidStock(BizStockDetail bizStockDetail) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizStockDetail", bizStockDetail);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `biz_stock_detail` SET valid_stock = IFNULL(:validStock,0) + IFNULL(valid_stock,0),problem_stock = IFNULL(:problemStock,0) + IFNULL(problem_stock,0),")
            .append("  version_no = version_no+1 WHERE id = :id AND :versionNo > version_no");

        updateForBean(sql.toString(), bizStockDetail);
    }

    /**
     * 根据库存明细id查询版本号
     * @param id 库存明细id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    public Integer getVersionNoById(Long id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);

        String sql = "SELECT version_no FROM biz_stock_detail WHERE id = :id";

        return findForObject(sql, params, Integer.class);
    }

    /**
     * 根据卖方code和商品code（list）查出库存列表
     * @param sellerOrgNo 卖方机构code
     * @param codes 商品codes（list）
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<BizStockDetail> getStockDetailListByOrgAndProduct(String sellerOrgNo, List<String> codes){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,product_categoryname,product_name,product_unit,")
                .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
                .append("transit_stock,freeze_stock,seller_orgno,IFNULL(cost_price,0) AS costPrice,instock_planid,")
                .append("latest_correct_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,version_no FROM biz_stock_detail WHERE delete_flag = :deleteFlag and org_no= :sellerOrgNo and product_no IN(:codes) and (valid_stock > 0 or problem_stock > 0)")
                .append(" order by create_time");//先进先出排序取出，按创建时间的正序排列
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("sellerOrgNo", sellerOrgNo);
        params.put("codes", codes);
        return super.queryListBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 根据卖方code和商品code（list）查出问题件库存列表
     * @param sellerOrgNo 卖方机构code
     * @param codes 商品codes（list）
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<BizStockDetail> getProblemStockDetailListByOrgAndProduct(String sellerOrgNo, List<String> codes){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,")
                .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
                .append("transit_stock,freeze_stock,seller_orgno,cost_price,instock_planid,")
                .append("latest_correct_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,version_no FROM biz_stock_detail WHERE delete_flag = :deleteFlag and org_no= :sellerOrgNo and product_no IN(:codes) and problem_stock > 0")
                .append(" order by create_time");//先进先出排序取出，按创建时间的正序排列
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("sellerOrgNo", sellerOrgNo);
        params.put("codes", codes);
        return super.queryListBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 根据仓库明细id更新仓库的有效库存和占用库存
     * @param stockDetailList 仓库详情list
     * @exception
     * @author weijb
     * @date 2018-08-08 19:59:51
     */
    public int batchUpdateStockDetil(List<BizStockDetail> stockDetailList){
        String sql = "update biz_stock_detail set valid_stock= IFNULL(valid_stock, 0) - :occupyStock, occupy_stock= IFNULL(occupy_stock, 0) + :occupyStock, version_no=version_no+1 where version_no=:versionNo and id=:id";
        return batchUpdateForListBean(sql, stockDetailList);
    }

    /**
     * 根据ids查询list
     * @param sList id list
     * @exception
     * @author weijb
     * @date 2018-08-11 13:59:51
     */
    public List<BizStockDetail> getStockDetailListByIds(List<Long> sList){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,")
                .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
                .append("transit_stock,freeze_stock,seller_orgno,cost_price,instock_planid,")
                .append("latest_correct_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,version_no FROM biz_stock_detail WHERE id IN(:sList) ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("sList", sList);
        return super.queryListBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 根据库存明细id查询所有库存明细的占用库存
     * @param ids 库存明细id
     * @return 库存明细
     * @author liuduo
     * @date 2018-08-08 14:55:43
     */
    public List<UpdateStockBizStockDetailDTO> getOutstockDetail(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);

        String sql = " SELECT id,occupy_stock,problem_stock,version_no FROM biz_stock_detail WHERE id IN(:ids)";

        return queryListBean(UpdateStockBizStockDetailDTO.class, sql.toString(), params);
    }

    /**
     * 修改库存明细的占用库存
     * @param bizStockDetails 库存明细
     * @author liuduo
     * @date 2018-08-09 19:14:33
     */
    public int updateOccupyStock(List<BizStockDetail> bizStockDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET occupy_stock = IFNULL(occupy_stock,0) - IFNULL(:occupyStock,0),problem_stock = IFNULL(problem_stock,0) - IFNULL(:problemStock,0),version_no = version_no+1,")
            .append(" operator = :operator,operate_time = :operateTime WHERE id = :id AND :versionNo > version_no");

        return batchUpdateForListBean(sql.toString(), bizStockDetails);
    }

    /**
     * 根据库存明细id查询到控制的版本号（乐观锁使用）
     * @param stockIds 库存明细id
     * @return 版本号对组
     * @author liuduo
     * @date 2018-08-10 15:02:51
     */
    public List<Pair<Long,Long>> queryVersionNoById(List<Long> stockIds) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("stockIds", stockIds);

        String sql = "SELECT id,version_no FROM biz_stock_detail  WHERE id IN(:stockIds)";

        return super.queryListPair(sql, param, Long.class, Long.class);
    }

    /**
     * 把库存明细中的有效库存更新库到占用库存
     * @param bizStockDetails 库存明细
     * @author liuduo
     * @date 2018-08-10 11:48:21
     */
    public int updateOccupyStockById(List<BizStockDetail> bizStockDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET occupy_stock = valid_stock,valid_stock = 0,version_no = version_no+1,")
            .append(" operator = :operator,operate_time = :operateTime WHERE id = :id AND :versionNo > version_no");

        return batchUpdateForListBean(sql.toString(), bizStockDetails);
    }

    /**
     * 查询新增物料盘库时用的列表
     * @param equipmentCodes 物料code
     * @return 新增物料盘库时用的列表
     * @author liuduo
     * @date 2018-08-14 17:43:53
     */
    public List<StockAdjustListDTO> queryAdjustList(List<String> equipmentCodes, String orgCode, String productType) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("orgCode", orgCode);
        param.put("productType", productType);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,product_no,product_type,product_categoryname,product_name,SUM(IFNULL(valid_stock,0) + IFNULL(occupy_stock,0)) AS dueNum")
            .append("  FROM biz_stock_detail WHERE org_no = :orgCode AND product_type = :productType");
        if (null != equipmentCodes && equipmentCodes.size() > 0) {
            param.put("equipmentCodes", equipmentCodes);
            sql.append(" AND product_no IN(:equipmentCodes)");
        }
        sql.append(" GROUP BY product_no");

        return queryListBean(StockAdjustListDTO.class, sql.toString(), param);
    }

    /**
     * 根据商品编码查询库存
     * @param productNo 商品编码
     * @return 库存集合
     * @author liuduo
     * @date 2018-08-14 20:22:08
     */
    public List<BizStockDetail> queryStockDetailByProductNo(String productNo, String orgCode) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("productNo", productNo);
        param.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,product_no,product_type,IFNULL(valid_stock,0) AS validStock,version_no FROM biz_stock_detail WHERE product_no = :productNo AND org_no = :orgCode ORDER BY create_time DESC");

        return queryListBean(BizStockDetail.class, sql.toString(), param);
    }

    /**
     * 更改盘库后的库存的有效库存
     * @param bizStockDetailList1 库存集合
     * @return
     * @author liuduo
     * @date 2018-08-14 21:38:16
     */
    public void updateAdjustValidStock(List<BizStockDetail> bizStockDetailList1) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET valid_stock = :validStock, version_no = version_no+1,operator = :operator,")
            .append(" operate_time = :operateTime WHERE :versionNo > version_no AND id = :id");

        batchUpdateForListBean(sql.toString(), bizStockDetailList1);
    }

    /**
     * 查询库存的详情
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindStockDetailDTO 库存的基本信息
     * @author zhangkangjian
     * @date 2018-08-20 11:15:52
     */
    public FindStockDetailDTO findStockDetail(String productNo, String productType, List<String> orgDTOList, String code) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.product_no,SUM(ifnull(a.valid_stock,0)) AS 'totalStock', ")
            .append(" SUM(ifnull(a.valid_stock,0)) * a.cost_price AS 'totalAmount', ")
            .append(" a.product_name AS 'productName',a.product_categoryname AS 'productCategoryname',a.product_unit AS 'unit' ")
            .append(" FROM biz_stock_detail a WHERE a.product_no = :productNo AND a.product_type = :productType");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code", code);
            sql.append(" AND a.org_no = :code ");
        }
        sql.append(" GROUP BY a.product_no ");
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
    public FindProductDetailDTO findProductDetail(String productNo, String productType, List<String> orgDTOList, String code) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(IFNULL(a.valid_stock,0)) AS 'validStock',SUM(IFNULL(a.occupy_stock,0)) AS 'occupyStock',SUM(ifnull(a.valid_stock,0) + ifnull(a.occupy_stock,0)) AS 'totalStock', ")
            .append(" SUM(ifnull(a.valid_stock,0) + ifnull(a.occupy_stock,0)) * a.cost_price AS 'totalAmount',a.product_unit AS 'unit' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code", code);
            sql.append(" AND a.org_no = :code ");
        }
        sql.append(" GROUP BY a.product_no ");
        FindProductDetailDTO forBean = findForBean(FindProductDetailDTO.class, sql.toString(), map);
        if(forBean == null){
            return new FindProductDetailDTO();
        }
        return forBean;
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
    public Long findTransferInventory(String productNo, String productType, List<String> orgDTOList, String sellerOrgno, String code) {
        if(StringUtils.isAnyBlank(productNo, productType, sellerOrgno)){
            return NumberUtils.LONG_ZERO;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        map.put("sellerOrgno", sellerOrgno);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(ifnull(a.valid_stock,0)) FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType AND a.seller_orgno = :sellerOrgno ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code", code);
            sql.append(" AND a.org_no = :code ");
        }
        sql.append(" GROUP BY a.product_no ");
        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, Long.class);
        }catch (Exception e){
            return 0L;
        }

    }

    /**
     * 查询问题件
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public FindProductDetailDTO findProblemStock(String productNo, String productType, List<String> orgDTOList, String code) {
        if(StringUtils.isAnyBlank(productNo, productType)){
            throw  new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(ifnull(a.problem_stock,0)) AS 'totalStock',SUM(ifnull(a.problem_stock,0)) * a.cost_price AS 'totalAmount',a.product_unit AS 'unit' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType  ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code", code);
            sql.append(" AND a.org_no = :code ");
        }
        sql.append(" GROUP BY a.product_no ");
        FindProductDetailDTO forBean = findForBean(FindProductDetailDTO.class, sql.toString(), map);
        if(forBean == null){
            return new FindProductDetailDTO();
        }
        return forBean;
    }
    /**
     * 查询损坏件
     * @param productNo 商品的编号
     * @param productType 商品的类型
     * @return FindProductDetailDTO 商品库存的详情
     * @author zhangkangjian
     * @date 2018-08-20 11:34:48
     */
    public FindProductDetailDTO findDamagedStock(String productNo, String productType, List<String> orgDTOList, String code) {
        if(StringUtils.isAnyBlank(productNo, productType)){
            throw new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(ifnull(a.damaged_stock,0)) AS 'totalStock',SUM(ifnull(a.damaged_stock,0)) * a.cost_price AS 'totalAmount',a.product_unit AS 'unit' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType  ");
        if(orgDTOList != null && orgDTOList.size() > 0){
            map.put("orgDTOList", orgDTOList);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code", code);
            sql.append(" AND a.org_no = :code ");
        }
        sql.append(" GROUP BY a.product_no ");
        FindProductDetailDTO forBean = findForBean(FindProductDetailDTO.class, sql.toString(), map);
        if(forBean == null){
            return new FindProductDetailDTO();
        }
        return forBean;
    }


    /**
     * 根据申请单查询库存
     * @param applyNo 申请单编码
     * @return 库存集合
     * @author weijb
     * @date 2018-08-20 16:22:08
     */
    public List<BizStockDetail> getStockDetailListByApplyNo(String applyNo) {
        if(StringUtils.isBlank(applyNo)){
            throw new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,")
                .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
                .append("transit_stock,freeze_stock,seller_orgno,cost_price,instock_planid,")
                .append("latest_correct_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,version_no FROM biz_stock_detail WHERE delete_flag = :deleteFlag and trade_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("applyNo", applyNo);
        return super.queryListBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 查看库存详情（批次库存列表查询）
     * @param findStockListDTO
     * @return Page<FindBatchStockListDTO>
     * @return Page<FindBatchStockListDTO>
     * @author zhangkangjian
     * @date 2018-08-21 10:53:48
     */
    public Page<FindBatchStockListDTO> findBatchStockList(FindStockListDTO findStockListDTO, List<String> orgCodes) {
        String productNo = findStockListDTO.getProductNo();
        String productType = findStockListDTO.getProductType();
        if(StringUtils.isAnyBlank(productNo, productType)){
            return new Page<>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("productType", productType);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.create_time,a.trade_no,a.supplier_no,a.cost_price, a.repository_no,b.supplier_name,c.storehouse_name, ")
            .append(" SUM(IFNULL(a.valid_stock,0) + IFNULL(a.occupy_stock,0) + IFNULL(a.problem_stock,0) + IFNULL(a.damaged_stock,0)) AS 'instockNum' ")
            .append(" FROM biz_stock_detail a  ")
            .append(" LEFT JOIN  biz_service_supplier b ON a.supplier_no = b.supplier_code ")
            .append(" LEFT JOIN biz_service_storehouse c ON a.repository_no = c.storehouse_code ")
            .append(" WHERE a.product_no = :productNo AND a.product_type = :productType  ");
        if(orgCodes != null && orgCodes.size() > 0){
            map.put("orgDTOList", orgCodes);
            sql.append(" AND a.org_no in (:orgDTOList) ");
        }
        if(StringUtils.isNotBlank(findStockListDTO.getOrgNo())){
            map.put("orgNo", findStockListDTO.getOrgNo());
            sql.append(" AND a.org_no = :orgNo ");
        }
        sql.append(" GROUP BY a.trade_no having SUM(IFNULL(a.valid_stock,0) + IFNULL(a.occupy_stock,0) + IFNULL(a.problem_stock,0) + IFNULL(a.damaged_stock,0)) > 0");
        return queryPageForBean(FindBatchStockListDTO.class, sql.toString(), map, findStockListDTO.getOffset(), findStockListDTO.getPageSize());
    }

    /**
     * 修改实际库存和占用库存（还库）
     * @param bizStockDetailList 库存明细
     * @author liuduo
     * @date 2018-09-06 19:49:20
     */
    public int updateValidAndOccupy(List<BizStockDetail> bizStockDetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET valid_stock = IFNULL(valid_stock,0) + :validStock,")
            .append(" occupy_stock = IFNULL(occupy_stock,0) - :occupyStock WHERE id = :id");

        return batchUpdateForListBean(sql.toString(), bizStockDetailList);
    }

    /**
     * 根据商品编号和机构code查询库存
     * @param productNo 商品编号
     * @param orgCode 机构code
     * @return 库存列表
     * @author liuduo
     * @date 2018-09-06 20:23:01
     */
    public List<BizStockDetail> getStockDetailByOrder(String productNo, String orgCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productNo", productNo);
        params.put("orgCode", orgCode);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,IFNULL(valid_stock,0) AS valid_stock FROM biz_stock_detail WHERE org_no = :orgCode AND product_no = :productNo AND delete_flag = :deleteFlag");

        return queryListBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 维修单占用库存
     * @param bizStockDetailList 库存详情集合
     * @author liuduo
     * @date 2018-09-07 15:16:46
     */
    public int batchUpdateValidStock(List<BizStockDetail> bizStockDetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET valid_stock = IFNULL(valid_stock,0) - :validStock,")
            .append(" occupy_stock = IFNULL(occupy_stock,0) + :occupyStock WHERE id = :id");

        return batchUpdateForListBean(sql.toString(), bizStockDetailList);
    }
}
