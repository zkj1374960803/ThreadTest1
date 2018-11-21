package com.ccbuluo.business.platform.allocateapply.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizAllocateapplyDetailDao extends BaseDao<AllocateapplyDetailDTO> {
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
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(AllocateapplyDetailDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocateapply_detail ( product_name,apply_no,product_no,")
            .append("product_type,product_categoryname,apply_num,unit,sell_price,")
            .append("cost_price,supplier_no,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :product_name,:applyNo, :productNo, :productType,")
            .append(" :productCategoryname, :applyNum, :unit, :sellPrice, :costPrice,")
            .append(" :supplierNo, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(AllocateapplyDetailDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocateapply_detail SET apply_no = :applyNo,")
            .append("product_no = :productNo,product_type = :productType,")
            .append("product_categoryname = :productCategoryname,apply_num = :applyNum,")
            .append("unit = :unit,sell_price = :sellPrice,cost_price = :costPrice,")
            .append("supplier_no = :supplierNo,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public AllocateapplyDetailDTO getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,apply_no,product_no,product_type,product_categoryname,")
            .append("apply_num,unit,sell_price,cost_price,supplier_no,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark")
            .append(" FROM biz_allocateapply_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(AllocateapplyDetailDTO.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_allocateapply_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 根据调拨申请单code获取调拨申请单详情
     * @param applyNo  applyNo
     * @return 影响条数
     * @author weijb
     * @date 2018-08-07 13:55:41
     */
    public List<AllocateapplyDetailBO> getAllocateapplyDetailByapplyNo(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bad.id,bad.apply_no,bad.product_no,bad.product_type,bad.product_categoryname,")
                .append("bad.apply_num,bad.unit,bad.sell_price,bad.cost_price,bad.supplier_no,bad.creator,bad.create_time,")
                .append("bad.operator,bad.operate_time,baa.instock_orgno,baa.in_repository_no,baa.outstock_orgno,baa.process_type,baa.process_orgtype,")
                .append("baa.process_orgno,bad.product_name,bad.stock_type,bad.remark,baa.applyorg_no,baa.apply_type")
                .append(" FROM biz_allocateapply_detail bad LEFT JOIN biz_allocate_apply baa on bad.apply_no=baa.apply_no WHERE bad.delete_flag = :deleteFlag AND  bad.apply_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("applyNo", applyNo);
        return super.queryListBean(AllocateapplyDetailBO.class, sql.toString(), params);
    }

    /**
     * 根据商品的code查询物料是否被申请
     * @param productNo 商品的code
     * @return 物料是否被申请
     * @author liuduo
     * @date 2018-08-23 16:01:38
     */
    public Boolean checkProductRelApply(String productNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productNo", productNo);
        String sql = "SELECT COUNT(id) > 0 FROM biz_allocateapply_detail WHERE product_no = :productNo";
        return findForObject(sql, params, Boolean.class);
    }



    /**
     * 查询客户经理待领取的物料
     * @param completeStatus 状态（PENDING待领取，CONFIRMRECEIPT已领取）
     * @param keyword     物料编号/物料名称
     * @param offset      偏移量
     * @param pageSize    每页显示的数量
     * @return List<QueryPendingMaterialsDTO>
     * @author zhangkangjian
     * @date 2018-08-25 20:40:35
     */
    public Page<QueryPendingMaterialsDTO> queryPendingMaterials(String completeStatus, String keyword, String loggedUserId, Integer offset, Integer pageSize) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("productType", Constants.PRODUCT_TYPE_EQUIPMENT);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.apply_status,b.remark,a.id,a.product_no,a.product_name,a.product_categoryname,c.supplier_name,a.plan_instocknum as 'productNum',a.complete_status ")
            .append(" FROM biz_instockplan_detail a LEFT JOIN biz_allocate_apply b ON a.trade_no = b.apply_no ")
            .append(" LEFT JOIN biz_service_supplier c ON a.supplier_no = c.supplier_code ")
            .append(" WHERE  a.product_type = :productType ");
        map.put("completeStatusList", List.of(StockPlanStatusEnum.COMPLETE.name(),StockPlanStatusEnum.DOING.name()));
        sql.append(" AND a.complete_status in (:completeStatusList) ");
        if(StringUtils.isNotBlank(loggedUserId)){
            map.put("applyer", loggedUserId);
            sql.append(" AND b.applyer = :applyer ");
        }
        if(StringUtils.isNotBlank(completeStatus)){
            map.put("completeStatus", completeStatus);
            sql.append(" AND a.complete_status = :completeStatus ");
        }
        if(StringUtils.isNotBlank(keyword)){
            map.put("keyword", keyword);
            sql.append(" AND (a.product_no like concat('%',:keyword,'%') or a.product_name like concat('%',:keyword,'%')) ");
        }
        return queryPageForBean(QueryPendingMaterialsDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 查询客户经理领取的物料
     * @param useruuid
     * @param productType 商品的类型
     * @param completeStatus 领取的状态
     * @return List<QueryPendingMaterialsDTO>
     * @author zhangkangjian
     * @date 2018-09-17 11:11:05
     */
    public List<QueryPendingMaterialsDTO> queryCustReceiveMaterials(List<String> useruuid, String productType, String completeStatus){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.applyer as 'applyeruuid',b.apply_status,a.product_no,a.product_name,COUNT(a.product_no) ")
            .append(" FROM biz_instockplan_detail a LEFT JOIN biz_allocate_apply b ON a.trade_no = b.apply_no  ")
            .append(" WHERE 1 = 1 ");
        Map<String, Object> map = Maps.newHashMap();
        map.put("completeStatusList", List.of(StockPlanStatusEnum.COMPLETE.name(),StockPlanStatusEnum.DOING.name()));
        sql.append(" AND a.complete_status in (:completeStatusList) ");
        if(useruuid != null && useruuid.size() > 0){
            map.put("useruuid", useruuid);
            sql.append(" AND b.applyer in (:useruuid) ");
        }
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND a.product_type = :productType ");
        }
        if (StringUtils.isNotBlank(completeStatus)){
            map.put("completeStatus", completeStatus);
            sql.append(" AND a.complete_status = :completeStatus ");
        }
        sql.append(" GROUP BY a.product_no ");
        return queryListBean(QueryPendingMaterialsDTO.class, sql.toString(), map);
    }

    /**
     * 根据申请的状态查询申请单的单号
     * @param applyStatusList 申请单状态的列表
     * @param applyType 申请单的类型
     * @return List<String> 申请单的单号
     * @author zhangkangjian
     * @date 2018-11-19 15:36:27
     */
    public List<String> queryApplyNo(List<String> applyStatusList, String applyType, List<String> inStockNoList){
        if(applyStatusList == null || applyStatusList.size() == 0 || StringUtils.isBlank(applyType)){
            return Collections.emptyList();
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyStatusList", applyStatusList);
        map.put("applyType", applyType);
        map.put("inStockNoList", inStockNoList);
        String sql = "SELECT a.apply_no FROM biz_allocate_apply a WHERE a.apply_type = :applyType AND a.apply_status IN (:applyStatusList) AND a.instock_orgno in (:inStockNoList)";
        return querySingColum(String.class, sql, map);
    }
    /**
     *  更新申请单详单的价格
     * @param applyNoList 申请单编号
     * @param productNo 商品的编号
     * @param sellPrice 销售的价格
     * @author zhangkangjian
     * @date 2018-11-19 15:56:15
     */
    public void updateAllocateapplyDetail(List<String> applyNoList, String productNo, Double sellPrice) {
        if(StringUtils.isAnyBlank(productNo) || sellPrice == null || applyNoList == null || applyNoList.size() == 0){
            throw new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyNo", applyNoList);
        map.put("productNo", productNo);
        map.put("sellPrice", sellPrice);
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ")
            .append(" ON a.apply_no = b.apply_no SET b.sell_price = :sellPrice WHERE a.apply_no in (:applyNo) AND b.product_no = :productNo ");
        updateForMap(sql.toString(), map);
    }

    /**
     * 更新入库单的价格
     * @param applyNoList 申请单编号
     * @param productNo 商品的编号
     * @param sellPrice 销售的价格
     * @author zhangkangjian
     * @date 2018-11-19 17:09:16
     */
    public void updateInstockorderDetail(List<String> applyNoList, String productNo, Double sellPrice) {
        if(StringUtils.isAnyBlank(productNo) || sellPrice == null || applyNoList == null || applyNoList.size() == 0){
            throw new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyNo", applyNoList);
        map.put("productNo", productNo);
        map.put("sellPrice", sellPrice);
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_instockplan_detail a LEFT JOIN biz_instockorder_detail b ON a.id = b.instock_planid ")
            .append(" SET a.cost_price = :sellPrice WHERE a.trade_no in (:applyNo)  AND a.product_no = :productNo ");
        updateForMap(sql.toString(), map);
    }

    /**
     * 更新出库单的价格
     * @param applyNoList 申请单编号
     * @param productNo 商品的编号
     * @param sellPrice 销售的价格
     * @author zhangkangjian
     * @date 2018-11-19 17:09:16
     */
    public void updateOutstockorderDetail(List<String> applyNoList, String productNo, Double sellPrice) {
        if(StringUtils.isAnyBlank(productNo) || sellPrice == null || applyNoList == null || applyNoList.size() == 0){
            throw new CommonException(Constants.ERROR_CODE, "必填参数为null");
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyNo", applyNoList);
        map.put("productNo", productNo);
        map.put("sellPrice", sellPrice);
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_outstockplan_detail a LEFT JOIN biz_outstockorder_detail b ON a.id = b.outstock_planid ")
            .append(" SET a.cost_price = :sellPrice WHERE a.trade_no in (:applyNo)  AND a.product_no = :productNo ");
        updateForMap(sql.toString(), map);
    }

    /**
     * 批量更新申请单的详单价格
     * @param updateRelProductPriceList 零配件价格信息
     * @author zhangkangjian
     * @date 2018-11-21 15:03:09
     */
    public void batchUpdateAllocateapplyDetail(List<RelProductPrice> updateRelProductPriceList) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ")
                .append(" ON a.apply_no = b.apply_no SET b.sell_price = :suggestedPrice WHERE a.apply_no in (:applyNoList) AND b.product_no = :productNo ");
            batchUpdateForListBean(sql.toString(), updateRelProductPriceList);
    }
    /**
     * 批量更新入库计划价格
     * @param updateRelProductPriceList 零配件价格信息
     * @author zhangkangjian
     * @date 2018-11-21 15:03:09
     */
    public void batchUpdateInstockorderDetail(List<RelProductPrice> updateRelProductPriceList) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE biz_instockplan_detail a LEFT JOIN biz_instockorder_detail b ON a.id = b.instock_planid ")
                .append(" SET a.cost_price = :suggestedPrice WHERE a.trade_no in (:applyNoList)  AND a.product_no = :productNo ");
            batchUpdateForListBean(sql.toString(), updateRelProductPriceList);
    }

    public void batchUpdateOutstockorderDetail(List<RelProductPrice> updateRelProductPriceList) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE biz_outstockplan_detail a LEFT JOIN biz_outstockorder_detail b ON a.id = b.outstock_planid ")
                .append(" SET a.cost_price = :suggestedPrice WHERE a.trade_no in (:applyNoList)  AND a.product_no = :productNo ");
            batchUpdateForListBean(sql.toString(), updateRelProductPriceList);
    }
}
