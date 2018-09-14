package com.ccbuluo.business.platform.inputstockplan.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;

import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dto.PerpayAmountDTO;
import com.ccbuluo.business.platform.outstock.dto.UpdatePlanStatusDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public List<BizInstockplanDetail> queryListByApplyNo(String applyNo, String status,  String inRepositoryNo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_name,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("IFNULL(plan_instocknum,0) AS planInstocknum,IFNULL(actual_instocknum,0) AS actualInstocknum,complete_status,complete_time,")
            .append("outstock_planid")
            .append(" FROM biz_instockplan_detail WHERE 1 = 1");
        if(StringUtils.isNotBlank(applyNo)){
            params.put("applyNo", applyNo);
            sql.append(" AND trade_no= :applyNo ");
        }
        if(StringUtils.isNotBlank(status)){
            params.put("completeStatus", status);
            sql.append(" AND complete_status = :completeStatus ");
        }
        if (StringUtils.isNotBlank(inRepositoryNo)){
            params.put("inRepositoryNo", inRepositoryNo);
            sql.append("  AND instock_repository_no = :inRepositoryNo ");
        }
        return super.queryListBean(BizInstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据id查询入库计划
     * @param id 申请单编号
     * @return 入库计划
     * @author zhangkangjian
     * @date 2018-08-08 11:14:56
     */
    public BizInstockplanDetail queryListById(Long id) {
        if(Objects.isNull(id)){
            return new BizInstockplanDetail();
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_name,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("IFNULL(plan_instocknum,0) AS planInstocknum,IFNULL(actual_instocknum,0) AS actualInstocknum,complete_status,complete_time,")
            .append("outstock_planid,stock_type,product_unit ")
            .append(" FROM biz_instockplan_detail WHERE id = :id");
        return findForBean(BizInstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据入库计划id查询版本号
     * @param id 入库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    public Long getVersionNoById(Long id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);

        String sql = "SELECT version_no FROM biz_instockplan_detail WHERE id = :id";

        return findForObject(sql, params, Long.class);
    }

    /**
     * 更新入库计划中的实际入库数量
     * @param bizInstockplanDetail 入库计划
     * @author liuduo
     * @date 2018-08-08 20:17:42
     */
    public int updateActualInstockNum(BizInstockplanDetail bizInstockplanDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET actual_instocknum = :actualInstocknum + IFNULL(actual_instocknum,0),cost_price = :costPrice,")
            .append(" version_no = version_no+1,operator = :operator,operate_time = :operateTime WHERE id = :id AND :versionNo > version_no");

        return updateForBean(sql.toString(), bizInstockplanDetail);
    }

    /**
     * 修改入库计划的完成状态
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    public int updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetailList) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizInstockplanDetailList", bizInstockplanDetailList);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET complete_status = :completeStatus,version_no = version_no+1,operator = :operator,")
            .append(" operate_time = :operateTime,complete_time = :completeTime WHERE id = :id AND :versionNo > version_no");

        return batchUpdateForListBean(sql.toString(), bizInstockplanDetailList);
    }

    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    public List<BizInstockplanDetail> queryInstockplan(String applyNo, String inRepositoryNo, String productType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("inRepositoryNo", inRepositoryNo);
        params.put("productType", productType);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bid.id,bid.instock_type,bid.product_no,bid.product_name,bid.product_type,bid.product_categoryname,bid.product_unit,")
            .append(" bid.trade_no,bid.supplier_no,bid.seller_orgno,bid.cost_price,bid.stock_type,IFNULL(bid.plan_instocknum,0) AS planInstocknum,")
            .append(" IFNULL(bid.actual_instocknum,0) AS actualInstocknum,bss.supplier_name FROM biz_instockplan_detail AS bid")
            .append(" LEFT JOIN biz_service_supplier AS bss ON bss.supplier_code = bid.supplier_no")
            .append("  WHERE bid.trade_no= :applyNo AND bid.product_type = :productType AND instock_repository_no = :inRepositoryNo");

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
        sql.append("UPDATE biz_instockplan_detail SET delete_flag = :deleteFlag  WHERE trade_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据申请单号查询入库仓库
     * @param applyNo 入库单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    public List<String> getByApplyNo(String applyNo, String orgCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("orgCode", orgCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT instock_repository_no FROM biz_instockplan_detail")
            .append("  WHERE trade_no = :applyNo AND instock_orgno = :orgCode");

        return querySingColum(String.class, sql.toString(), params);
    }

    /**
     * 根据申请单编号查询平台的入库计划（平台方）
     * @param applyNo 申请单编号
     * @return 入库计划
     * @author weijb
     * @date 2018-08-20 17:14:56
     */
    public List<BizInstockplanDetail> getInstockplanDetailByApplyNo(String applyNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("instockOrgno", BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_name,product_type,product_categoryname,")
                .append("trade_no,supplier_no,instock_repository_no,cost_price,")
                .append("IFNULL(plan_instocknum,0) AS planInstocknum,IFNULL(actual_instocknum,0) AS actualInstocknum,complete_status,complete_time,")
                .append("outstock_planid,product_unit,stock_type")
                .append(" FROM biz_instockplan_detail WHERE trade_no= :applyNo AND instock_orgno= :instockOrgno");
        return super.queryListBean(BizInstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据申请单号和入库仓库查询入库计划
     * @param applyNo 申请单号
     * @param inRepositoryNo 入库仓库
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    public List<BizInstockplanDetail> queryListByApplyNoAndInReNo(String applyNo, String inRepositoryNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("inRepositoryNo", inRepositoryNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_type,product_no,product_name,product_type,product_categoryname,")
            .append("trade_no,supplier_no,instock_repository_no,cost_price,")
            .append("IFNULL(plan_instocknum,0) AS planInstocknum,IFNULL(actual_instocknum,0) AS actualInstocknum,complete_status,complete_time,")
            .append("outstock_planid")
            .append(" FROM biz_instockplan_detail WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(applyNo)){
            sql.append(" AND trade_no= :applyNo ");
        }
        if(StringUtils.isNotBlank(inRepositoryNo)){
            sql.append(" AND instock_repository_no = :inRepositoryNo ");
        }
        return super.queryListBean(BizInstockplanDetail.class, sql.toString(), params);
    }

    /**
     * 根据入库计划id查询版本号
     * @param ids 入库计划id
     * @return 入库计划的版本号（乐观锁）
     * @author liuduo
     * @date 2018-08-28 15:03:22
     */
    public List<UpdatePlanStatusDTO> getVersionNoByIds(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);

        String sql = "SELECT id,version_no FROM biz_instockplan_detail WHERE id IN(:ids)";

        return queryListBean(UpdatePlanStatusDTO.class, sql, params);
    }

    /**
     * 根据申请单号更改入库计划状态
     * @param applyNo 申请单编号
     * @exception
     * @author weijb
     * @Date 2018-08-10 17:37:32
     */
    public int updateCompleteStatus(String applyNo){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockplan_detail SET complete_status = :completeStatus  WHERE complete_status = :status and trade_no= :applyNo ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("completeStatus", StockPlanStatusEnum.DOING.toString());
        params.put("status", StockPlanStatusEnum.NOTEFFECTIVE.toString());
        return super.updateForMap(sql.toString(), params);
    }

}
