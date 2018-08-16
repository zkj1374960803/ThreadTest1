package com.ccbuluo.business.platform.stockdetail.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.business.platform.stockdetail.dto.UpdateStockBizStockDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        sql.append("INSERT INTO biz_stock_detail ( repository_no,org_no,product_no,product_categoryname")
            .append("product_type,trade_no,supplier_no,valid_stock,occupy_stock,")
            .append("problem_stock,damaged_stock,transit_stock,freeze_stock,seller_orgno,")
            .append("cost_price,instock_planid,latest_correct_time,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :repositoryNo,")
            .append(" :orgNo, :productNo, :productCategoryname, :productType, :tradeNo, :supplierNo,")
            .append(" :validStock, :occupyStock, :problemStock, :damagedStock,")
            .append(" :transitStock, :freezeStock, :sellerOrgno, :costPrice,")
            .append(" :instockPlanid, :latestCorrectTime, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag, :remark )");
        return super.saveRid(sql.toString(), entity);
    }

    /**
     * 编辑 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
     * @param entity 批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizStockDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET repository_no = :repositoryNo,")
            .append("org_no = :orgNo,product_no = :productNo,product_type = :productType,")
            .append("trade_no = :tradeNo,supplier_no = :supplierNo,")
            .append("valid_stock = :validStock,occupy_stock = :occupyStock,")
            .append("problem_stock = :problemStock,damaged_stock = :damagedStock,")
            .append("transit_stock = :transitStock,freeze_stock = :freezeStock,")
            .append("seller_orgno = :sellerOrgno,cost_price = :costPrice,")
            .append("instock_planid = :instockPlanid,")
            .append("latest_correct_time = :latestCorrectTime,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizStockDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,")
            .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
            .append("transit_stock,freeze_stock,seller_orgno,cost_price,instock_planid,")
            .append("latest_correct_time,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_stock_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizStockDetail.class, sql.toString(), params);
    }

    /**
     * 删除批次库存表，由交易批次号、供应商、仓库等多维度唯一主键 区分的库存表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_stock_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
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
    public Long getByinstockorderDeatil(String supplierNo, String productNo, String inRepositoryNo, String applyNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierNo", supplierNo);
        params.put("productNo", productNo);
        params.put("inRepositoryNo", inRepositoryNo);
        params.put("applyNo", applyNo);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id FROM biz_stock_detail WHERE supplier_no = :supplierNo AND repository_no = :inRepositoryNo")
            .append(" AND trade_no = :applyNo AND product_no = :productNo");

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
     * @param versionNo 版本号
     * @author liuduo
     * @date 2018-08-08 15:24:09
     */
    public void updateValidStock(BizStockDetail bizStockDetail, Long versionNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizStockDetail", bizStockDetail);
        params.put("versionNo", versionNo + Constants.LONG_FLAG_ONE);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `biz_stock_detail` SET valid_stock = :validStock+valid_stock,version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

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
     * @param productOrgNo 卖方机构code
     * @param codes 商品codes（list）
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<BizStockDetail> getStockDetailListByOrgAndProduct(String productOrgNo, List<String> codes){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,repository_no,org_no,product_no,product_type,trade_no,")
                .append("supplier_no,valid_stock,occupy_stock,problem_stock,damaged_stock,")
                .append("transit_stock,freeze_stock,seller_orgno,cost_price,instock_planid,")
                .append("latest_correct_time,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark,version_no FROM biz_stock_detail WHERE delete_flag = :deleteFlag and org_no= :productOrgNo and product_no IN(:codes) and valid_stock > 0")
                .append(" order by create_time");//先进先出排序取出，按创建时间的正序排列
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("productOrgNo", productOrgNo);
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
        String sql = "update biz_stock_detail set valid_stock=:validStock, occupy_stock=:occupyStock, version_no=version_no+1 where version_no=:versionNo and id=:id";
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
    public void updateOccupyStock(List<BizStockDetail> bizStockDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET occupy_stock = :occupyStock,version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizStockDetails);
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
    public void updateOccupyStockById(List<BizStockDetail> bizStockDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_detail SET occupy_stock = valid_stock,valid_stock = 0,version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizStockDetails);
    }

    /**
     * 查询新增物料盘库时用的列表
     * @param equipmentCodes 物料code
     * @return 新增物料盘库时用的列表
     * @author liuduo
     * @date 2018-08-14 17:43:53
     */
    public List<StockAdjustListDTO> queryAdjustList(List<String> equipmentCodes) {
        Map<String, Object> param = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,product_no,product_type,product_categoryname,product_name,IFNULL(SUM(valid_stock + occupy_stock),0) AS dueNum")
            .append("  FROM biz_stock_detail WHERE 1=1");
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
    public List<BizStockDetail> queryStockDetailByProductNo(String productNo) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("productNo", productNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,product_no,product_type,valid_stock,version_no FROM biz_stock_detail WHERE product_no = :productNo ORDER BY create_time DESC");

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
        String sql = "UPDATE biz_stock_detail SET valid_stock = :validStock, version_no = version_no+1 WHERE version_no > :versionNo AND id = :id";
    }
}
