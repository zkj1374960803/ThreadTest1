package com.ccbuluo.business.platform.outstockplan.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.outstock.dto.updatePlanStatusDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  出库计划表实体
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizOutstockplanDetailDao extends BaseDao<BizOutstockplanDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 实体
     * @param entity 实体
     * @return Long id 新增返回
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public Long saveEntity(BizOutstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstockplan_detail ( outstock_type,stock_id,")
            .append("product_no,product_type,trade_no,supplier_no,apply_detail_id,")
            .append("cost_price,sales_price,out_repository_no,plan_outstocknum,")
            .append("actual_outstocknum,plan_status,complete_time,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark,product_categoryname")
            .append(" ) VALUES (  :outstockType, :stockId, :productNo, :productType,")
            .append(" :tradeNo, :supplierNo, :applyDetailId, :costPrice, :salesPrice,")
            .append(" :outRepositoryNo, :planOutstocknum, :actualOutstocknum, :planStatus,")
            .append(" :completeTime, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark, :productCategoryname )");
        return super.saveRid(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizOutstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET outstock_type = :outstockType,")
            .append("stock_id = :stockId,product_no = :productNo,")
            .append("product_type = :productType,trade_no = :tradeNo,")
            .append("supplier_no = :supplierNo,apply_detail_id = :applyDetailId,")
            .append("cost_price = :costPrice,sales_price = :salesPrice,")
            .append("out_repository_no = :outRepositoryNo,")
            .append("plan_outstocknum = :planOutstocknum,")
            .append("actual_outstocknum = :actualOutstocknum,plan_status = :planStatus,")
            .append("complete_time = :completeTime,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark,product_categoryname = :productCategoryname")
            .append(" WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizOutstockplanDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstock_type,stock_id,product_no,product_type,trade_no,")
            .append("supplier_no,apply_detail_id,cost_price,sales_price,out_repository_no,")
            .append("plan_outstocknum,actual_outstocknum,plan_status,complete_time,")
            .append("creator,create_time,operator,operate_time,delete_flag,remark,")
            .append("product_categoryname FROM biz_outstockplan_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizOutstockplanDetail.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_outstockplan_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 批量新增出库计划详情
     * @param list 入库计划详情
     * @exception
     * @author weijb
     * @Date 2018-08-10 17:37:32
     */
    public List<Long> batchOutstockplanDetail(List<BizOutstockplanDetail> list){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_outstockplan_detail ( outstock_type,stock_id,")
                .append("product_no,product_type,trade_no,supplier_no,apply_detail_id,")
                .append("cost_price,sales_price,out_repository_no,plan_outstocknum,")
                .append("actual_outstocknum,plan_status,complete_time,creator,create_time,")
                .append("operator,operate_time,delete_flag,remark,product_categoryname,product_unit,product_name,stock_type,out_orgno")
                .append(" ) VALUES (  :outstockType, :stockId, :productNo, :productType,")
                .append(" :tradeNo, :supplierNo, :applyDetailId, :costPrice, :salesPrice,")
                .append(" :outRepositoryNo, :planOutstocknum, :actualOutstocknum, :planStatus,")
                .append(" :completeTime, :creator, :createTime, :operator, :operateTime,")
                .append(" :deleteFlag, :remark, :productCategoryname, :productUnit, :productName, :stockType, :outOrgno )");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), list);
        return longs;
    }

    /**
     *  更改出库计划状态
     * @param applyNo 申请单编号
     * @param planStatus 状态
     * @param outRepositoryNo 出库仓库编号
     * @author weijb
     * @date 2018-08-11 12:55:41
     */
    public int updateOutStockPlanStatus(String applyNo, String planStatus, String outRepositoryNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET plan_status = :planStatus WHERE trade_no= :applyNo and out_repository_no= :outRepositoryNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("planStatus", planStatus);
        params.put("outRepositoryNo", outRepositoryNo);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 删除出库计划详情
     * @param applyNo 申请单编号
     * @exception
     * @author weijb
     * @Date 2018-08-10 17:37:32
     */
    public int deleteOutstockplanDetailByApplyNo(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET delete_flag = :deleteFlag  WHERE doc_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据申请单号查询出库计划
     * @param applyNo 申请单号
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-09 14:38:57
     */
    public List<BizOutstockplanDetail> queryOutstockplan(String applyNo, String outRepositoryNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("outRepositoryNo", outRepositoryNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,outstock_type,stock_id,product_no,product_type,trade_no,")
            .append("supplier_no,apply_detail_id,cost_price,sales_price,out_repository_no,")
            .append("plan_outstocknum,actual_outstocknum,plan_status,complete_time,")
            .append("creator,create_time,operator,operate_time,delete_flag,remark,version_no,")
            .append("product_categoryname FROM biz_outstockplan_detail WHERE trade_no= :applyNo AND out_repository_no = :outRepositoryNo");

        return queryListBean(BizOutstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据出库计划id查询版本号（乐观锁）
     * @param ids 出库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-10 16:43:38
     */
    public List<updatePlanStatusDTO> getVersionNoById(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);

        String sql = "SELECT id,version_no FROM biz_outstockplan_detail WHERE id IN(:ids)";

        return queryListBean(updatePlanStatusDTO.class, sql, params);
    }

    /**
     * 更改出库计划的实际出库数量
     * @param bizOutstockplanDetails 出库计划
     * @author liuduo
     * @date 2018-08-10 16:48:48
     */
    public void updateActualOutstocknum(List<BizOutstockplanDetail> bizOutstockplanDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET actual_outstocknum = :actualOutstocknum + IFNULL(actual_outstocknum,0),version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizOutstockplanDetails);
    }

    /**
     * 更新出库计划中的状态和完成时间
     * @param bizOutstockplanDetails 出库计划
     * @author liuduo
     * @date 2018-08-10 17:46:26
     */
    public void updatePlanStatus(List<BizOutstockplanDetail> bizOutstockplanDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_outstockplan_detail SET plan_status = :planStatus,complete_time = :completeTime,version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizOutstockplanDetails);
    }

    /**
     * 根据申请单号查询出库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    public List<BizOutstockplanDetail> queryOutstockplanList(String applyNo, String outRepositoryNo, String productType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("outRepositoryNo", outRepositoryNo);
        params.put("productType", productType);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bod.id,bod.outstock_type,bod.product_no,bod.product_name,bod.product_type,bod.product_categoryname,bod.plan_status,bod.out_orgno,")
            .append(" bod.supplier_no,bss.supplier_name,bod.plan_outstocknum,bod.cost_price,bod.sales_price,bod.product_unit,bod.out_repository_no,bod.stock_type,")
            .append(" bsss.storehouse_name FROM biz_outstockplan_detail AS bod")
            .append("  LEFT JOIN biz_service_supplier AS bss ON bss.supplier_code = bod.supplier_no")
            .append("  LEFT JOIN biz_service_storehouse AS bsss ON bsss.storehouse_code = bod.out_repository_no")
            .append(" WHERE bod.trade_no = :applyNo AND bod.product_type = :productType AND out_repository_no = :outRepositoryNo");

        return queryListBean(BizOutstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据申请单号查询出库仓库
     * @param applyNo 申请单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    public List<String> getByApplyNo(String applyNo, String orgCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT out_repository_no FROM biz_outstockplan_detail")
            .append("  WHERE trade_no = :applyNo AND out_orgno = :orgCode");

        return querySingColum(String.class, sql.toString(), params);
    }
}
