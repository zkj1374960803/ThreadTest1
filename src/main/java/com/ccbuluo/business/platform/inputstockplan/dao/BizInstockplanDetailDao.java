package com.ccbuluo.business.platform.inputstockplan.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.outstock.dto.updatePlanStatusDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizInstockplanDetailDao extends BaseDao<BizInstockplanDetail> {
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
    public int saveEntity(BizInstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instockplan_detail ( instock_type,product_no,")
            .append("product_type,product_categoryname,trade_no,supplier_no,")
            .append("instock_repository_no,cost_price,plan_instocknum,actual_instocknum,")
            .append("complete_status,complete_time,outstock_planid,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :instockType,")
            .append(" :productNo, :productType, :productCategoryname, :tradeNo,")
            .append(" :supplierNo, :instockRepositoryNo, :costPrice, :planInstocknum,")
            .append(" :actualInstocknum, :completeStatus, :completeTime, :outstockPlanid,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark")
            .append(" )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizInstockplanDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET instock_type = :instockType,")
            .append("product_no = :productNo,product_type = :productType,")
            .append("product_categoryname = :productCategoryname,trade_no = :tradeNo,")
            .append("supplier_no = :supplierNo,")
            .append("instock_repository_no = :instockRepositoryNo,cost_price = :costPrice,")
            .append("plan_instocknum = :planInstocknum,")
            .append("actual_instocknum = :actualInstocknum,")
            .append("complete_status = :completeStatus,complete_time = :completeTime,")
            .append("outstock_planid = :outstockPlanid,creator = :creator,")
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
    public BizInstockplanDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("plan_instocknum,actual_instocknum,complete_status,complete_time,")
            .append("outstock_planid,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_instockplan_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizInstockplanDetail.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_instockplan_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 批量新增入库计划详情
     * @param list 入库计划详情
     * @exception
     * @author weijb
     * @Date 2018-08-10 17:37:32
     */
    public List<Long> batchInsertInstockplanDetail(List<BizInstockplanDetail> list){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instockplan_detail ( instock_type,product_no,")
                .append("product_type,product_categoryname,trade_no,supplier_no,instock_orgno,")
                .append("instock_repository_no,cost_price,plan_instocknum,actual_instocknum,")
                .append("complete_status,complete_time,outstock_planid,creator,create_time,")
                .append("operator,operate_time,delete_flag,remark,product_unit,product_name,stock_type ) VALUES (  :instockType,")
                .append(" :productNo, :productType, :productCategoryname, :tradeNo,")
                .append(" :supplierNo, :instockOrgno, :instockRepositoryNo, :costPrice, :planInstocknum,")
                .append(" :actualInstocknum, :completeStatus, :completeTime, :outstockPlanid,")
                .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark, :productUnit, :productName, :stockType")
                .append(" )");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), list);
        return longs;
    }

    /**
     * 根据申请单编号查询入库计划
     * @param applyNo 申请单编号
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-08 11:14:56
     */
    public List<BizInstockplanDetail> queryListByApplyNo(String applyNo, String inRepositoryNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("completeStatus", Constants.CHECKED);
        params.put("inRepositoryNo", inRepositoryNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("plan_instocknum,actual_instocknum,complete_status,complete_time,")
            .append("outstock_planid,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_instockplan_detail WHERE trade_no= :applyNo")
            .append(" AND complete_status = :completeStatus AND instock_repository_no = :inRepositoryNo");

        return super.queryListBean(BizInstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据入库计划id查询版本号
     * @param ids 入库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    public List<updatePlanStatusDTO> getVersionNoById(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);

        String sql = "SELECT id,version_no FROM biz_instockplan_detail WHERE id IN(:ids)";

        return queryListBean(updatePlanStatusDTO.class, sql, params);
    }

    /**
     * 更新入库计划中的实际入库数量
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-08 20:17:42
     */
    public void updateActualInstockNum(List<BizInstockplanDetail> bizInstockplanDetailList) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizInstockplanDetailList", bizInstockplanDetailList);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET actual_instocknum = :actualInstocknum + actual_instocknum,version_no = version_no+1")
            .append(" WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizInstockplanDetailList);
    }

    /**
     * 修改入库计划的完成状态
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    public void updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetailList) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizInstockplanDetailList", bizInstockplanDetailList);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET complete_status = :completeStatus,version_no = version_no+1,")
            .append(" complete_time = :completeTime WHERE id = :id AND :versionNo > version_no");

        batchUpdateForListBean(sql.toString(), bizInstockplanDetailList);
    }

    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    public List<BizInstockplanDetail> queryInstockplan(String applyNo, String productType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("productType", productType);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bid.id,bid.instock_type,bid.product_no,bid.product_name,bid.product_type,bid.product_categoryname,bid.product_unit,")
            .append(" bid.trade_no,bid.supplier_no,bid.cost_price,bid.plan_instocknum,bss.supplier_name FROM biz_instockplan_detail AS bid")
            .append(" LEFT JOIN biz_service_supplier AS bss ON bss.supplier_code = bid.supplier_no")
            .append("  WHERE bid.trade_no= :applyNo AND bid.product_type = :productType");

        return queryListBean(BizInstockplanDetail.class, sql.toString(), params);
    }
    /**
     *  更改入库计划状态
     * @param applyNo 申请单编号
     * @param completeStatus 状态
     * @param instockRepositoryNo 入库仓库编号
     * @author weijb
     * @date 2018-08-11 12:55:41
     */
    public int updateInStockPlanStatus(String applyNo, String completeStatus, String instockRepositoryNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET complete_status = :completeStatus WHERE trade_no= :applyNo and instock_repository_no = :instockRepositoryNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("completeStatus", completeStatus);
        params.put("instockRepositoryNo", instockRepositoryNo);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 删除入库计划详情
     * @param applyNo 申请单编号
     * @exception
     * @author weijb
     * @Date 2018-08-10 17:37:32
     */
    public int batchInsertInstockplanDetail(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET delete_flag = :deleteFlag  WHERE doc_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }
}
