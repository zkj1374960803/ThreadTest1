package com.ccbuluo.business.platform.allocateapply.dao;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.InstockTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.supplier.dto.QuerySupplierInfoDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料和零配件调拨的申请 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizAllocateApplyDao extends BaseDao<AllocateApplyDTO> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 物料和零配件调拨的申请实体
     * @param entity 物料和零配件调拨的申请实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(AllocateApplyDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocate_apply ( applyer_name,apply_type,apply_no,applyorg_no,applyer,process_orgtype,process_orgno,")
            .append("instock_orgno,in_repository_no,outstock_orgtype,outstock_orgno,")
            .append("apply_processor,process_time,process_type,apply_status,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark,refund_address ) VALUES (:applyerName,:applyType,:applyNo, :applyorgNo, :applyer,:processOrgtype,:processOrgno, ")
            .append(" :instockOrgno, :inRepositoryNo,")
            .append(" :outstockOrgtype, :outstockOrgno, :applyProcessor, :processTime,")
            .append(" :processType, :applyStatus, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark,:refundAddress )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 物料和零配件调拨的申请实体
     * @param entity 物料和零配件调拨的申请实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(AllocateApplyDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_allocate_apply SET apply_no = :applyNo,")
            .append("applyorg_no = :applyorgNo,applyer = :applyer,")
            .append("instock_orgno = :instockOrgno,in_repository_no = :inRepositoryNo,")
            .append("outstock_orgtype = :outstockOrgtype,outstock_orgno = :outstockOrgno,")
            .append("apply_processor = :applyProcessor,process_time = :processTime,")
            .append("process_type = :processType,apply_status = :applyStatus,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取物料和零配件调拨的申请详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public AllocateApplyDTO getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,apply_no,applyorg_no,applyer,instock_orgno,")
            .append("in_repository_no,outstock_orgtype,outstock_orgno,apply_processor,")
            .append("process_time,process_type,apply_status,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark FROM biz_allocate_apply WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(AllocateApplyDTO.class, sql.toString(), params);
    }


    /**
     * 查询申请单的总金额
     * @param queryPurchaseListDTO 查询条件
     * @return Page<QueryPurchaseListDTO> 分页信息
     * @author zhangkangjian
     * @date 2018-08-07 11:55:41
     */
    public Page<QueryPurchaseListDTO> queryAllocateApplyByCode(QueryPurchaseListDTO queryPurchaseListDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.apply_no,UNIX_TIMESTAMP(a.create_time) AS 'applyTime',a.apply_status, SUM(IFNULL(b.total_price,0)) AS 'totalPurchase' ")
            .append(" FROM biz_allocate_apply a  ")
            .append(" LEFT JOIN biz_allocate_tradeorder b ON a.apply_no = b.apply_no  AND b.delete_flag = :deleteFlag ")
            .append(" LEFT JOIN (SELECT a.apply_no,a.product_type FROM biz_allocateapply_detail a GROUP BY a.apply_no) c on  a.apply_no  = c.apply_no ")
            .append(" WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(queryPurchaseListDTO.getApplyStatus())){
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(queryPurchaseListDTO.getApplyNo())){
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(queryPurchaseListDTO.getApplyType())){
            sql.append(" AND a.apply_type = :applyType ");
        }
        if(StringUtils.isNotBlank(queryPurchaseListDTO.getProductType())){
            sql.append(" AND c.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.create_time DESC ");
        return queryPageForBean(QueryPurchaseListDTO.class, sql.toString(), queryPurchaseListDTO, queryPurchaseListDTO.getOffset(), queryPurchaseListDTO.getPageSize());
    }

    /**
     * 删除物料和零配件调拨的申请
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_allocate_apply WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 批量插入申请单详情表
     * @param allocateapplyDetailList
     * @author zhangkangjian
     * @date 2018-08-08 10:45:24
     */
    public void batchInsertForapplyDetailList(List<AllocateapplyDetailDTO> allocateapplyDetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocateapply_detail ( product_name,apply_no,product_no,")
            .append("product_type,product_categoryname,apply_num,unit,sell_price,")
            .append("cost_price,supplier_no,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark,stock_type ) VALUES (  :productName,:applyNo, :productNo, :productType,")
            .append(" :productCategoryname, :applyNum, :unit, :sellPrice, :costPrice,")
            .append(" :supplierNo, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark,:stockType )");
        batchInsertForListBean(sql.toString(), allocateapplyDetailList);

    }

    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    public FindAllocateApplyDTO findDetail(String applyNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.process_memo,a.process_orgtype,a.process_orgno,a.apply_type,a.apply_no,a.applyorg_no,a.apply_status,a.applyer_name,b.storehouse_name,b.storehouse_address, ")
            .append(" a.create_time,a.process_type,b.servicecenter_code as 'instockOrgno',a.outstock_orgno,a.in_repository_no as 'inRepositoryNo',a.refund_address,a.remark ")
            .append(" FROM biz_allocate_apply a LEFT JOIN biz_service_storehouse b ON a.in_repository_no = b.storehouse_code ")
            .append(" WHERE a.apply_no = :applyNo ");
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyNo", applyNo);
        return findForBean(FindAllocateApplyDTO.class, sql.toString(), map);
    }
    /**
     * 查询申请单的详单
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    public List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetail(String applyNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.apply_no,a.product_no,a.product_type,a.product_categoryname, ")
            .append("  a.apply_num,a.unit,a.sell_price,a.cost_price,a.supplier_no,b.supplier_name,c.equip_name as 'productName' ")
            .append("  FROM biz_allocateapply_detail a LEFT JOIN  biz_service_supplier b ON a.supplier_no = b.supplier_code ")
            .append("  LEFT JOIN biz_service_equipment c ON a.product_no = c.equip_code  ")
            .append(" WHERE a.delete_flag = :deleteFlag AND a.apply_no = :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return queryListBean(QueryAllocateapplyDetailDTO.class, sql.toString(), params);
    }
    /**
     * 查询申请列表
     * @param userOrgCode 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findApplyList(String productType, List<String> orgCodes, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.process_orgno,a.instock_orgno,a.applyorg_no,a.apply_no,a.applyer_name,a.create_time,a.apply_type,a.apply_status,a.process_type,a.process_orgtype as 'orgType',a.outstock_orgno ")
            .append(" FROM biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ON a.apply_no = b.apply_no WHERE ")
            .append(" a.process_type IN (:processTypeList) ");
        map.put("processTypeList", List.of(BizAllocateApply.ProcessTypeEnum.TRANSFER.name()));
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND (a.applyorg_no = :userOrgCode or a.instock_orgno = :userOrgCode) ");
        }
        if(orgCodes != null && orgCodes.size() > 0){
            map.put("orgCodes", orgCodes);
            sql.append(" AND a.applyorg_no in (:orgCodes)  ");
        }
        if(StringUtils.isNotBlank(processType)){
            map.put("processType", processType);
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            map.put("applyStatus", applyStatus);
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND b.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.operate_time DESC");
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 问题件查询申请列表
     * @param userOrgCode 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findProblemApplyList(String productType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.applyorg_no,a.apply_no,a.applyer_name,a.create_time,a.apply_type,a.apply_status,a.process_type,a.process_orgtype as 'orgType',a.outstock_orgno ")
                .append(" FROM biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ON a.apply_no = b.apply_no WHERE (a.apply_type='REFUND' or a.apply_type='BARTER') ");
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND (a.applyorg_no = :userOrgCode or a.instock_orgno = :userOrgCode) ");
        }
        if(StringUtils.isNotBlank(processType)){
            map.put("processType", processType);
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            map.put("applyStatus", applyStatus);
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND b.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.operate_time DESC");
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }
    /**
     * 查询处理列表
     * @param userOrgCode 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findProcessApplyList(List<String> orgCode, String productType,List<String> orgCodesByOrgType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("processType", InstockTypeEnum.TRANSFER.name());
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.process_orgno,a.instock_orgno,a.outstock_orgno,a.applyorg_no,a.apply_no,a.applyer_name,a.create_time,a.apply_type,a.process_type,a.apply_status ")
            .append(" FROM biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ON a.apply_no = b.apply_no WHERE ")
            .append(" a.process_type IN (:processTypeList) AND a.process_type = :processType ");
        map.put("processTypeList", List.of(BizAllocateApply.ProcessTypeEnum.TRANSFER.name(), BizAllocateApply.ProcessTypeEnum.PURCHASE.name()));
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND (a.outstock_orgno = :userOrgCode or process_orgno = :userOrgCode) ");
        }
        if(orgCodesByOrgType != null && orgCodesByOrgType.size() > 0){
            map.put("orgCodesByOrgType", orgCodesByOrgType);
            sql.append(" AND a.applyorg_no in (:orgCodesByOrgType)  ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            map.put("applyStatus", applyStatus);
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo) && (orgCode == null || orgCode.size() == 0)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(applyNo) && orgCode != null && orgCode.size() > 0){
            map.put("applyNo", applyNo);
            map.put("orgCode", orgCode);
            sql.append(" AND (a.apply_no = :applyNo OR a.applyorg_no in (:orgCode)) ");
        }

        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND b.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.operate_time DESC");
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 查询处理列表
     * @param userOrgCode 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findProcessHandleList(String processType, String productType,List<String> orgCodesByOrgType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.applyorg_no,a.apply_no,a.applyer_name,a.create_time,a.apply_type,a.process_type,a.apply_status ")
                .append(" FROM biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ON a.apply_no = b.apply_no WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(processType)){
            map.put("processType", processType);
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND (a.outstock_orgno = :userOrgCode or process_orgno = :userOrgCode) ");
        }
        if(orgCodesByOrgType != null && orgCodesByOrgType.size() > 0){
            map.put("orgCodesByOrgType", orgCodesByOrgType);
            sql.append(" AND a.applyorg_no in (:orgCodesByOrgType)  ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            map.put("applyStatus", applyStatus);
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND b.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.operate_time DESC");
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 查询问题件处理列表
     * @param processType 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author weijb
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findProblemProcessHandleList(String processType, String productType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.applyorg_no,a.apply_no,a.applyer_name,a.create_time,a.apply_type,a.process_type,a.apply_status,a.in_repository_no,UNIX_TIMESTAMP(a.operate_time) as 'operateTime' ")
                .append(" FROM biz_allocate_apply a LEFT JOIN biz_allocateapply_detail b ON a.apply_no = b.apply_no WHERE (a.apply_type='REFUND' or a.apply_type='BARTER') ");
        if(StringUtils.isNotBlank(processType)){
            map.put("processType", processType);
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            map.put("applyStatus", applyStatus);
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            map.put("applyNo", applyNo);
            sql.append(" AND a.apply_no = :applyNo ");
        }
        if(StringUtils.isNotBlank(productType)){
            map.put("productType", productType);
            sql.append(" AND b.product_type = :productType ");
        }
        sql.append(" GROUP BY a.apply_no order by a.operate_time DESC");
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 查询乐观锁的值
     * @param applyNo 申请单的编号
     * @return Long
     * @author zhangkangjian
     * @date 2018-08-10 11:54:41
     */
    public Long findVersionNo(String applyNo) {
        String sql = " SELECT a.version_no FROM biz_allocate_apply a WHERE a.apply_no = :applyNo ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("applyNo", applyNo);
        return namedParameterJdbcTemplate.queryForObject(sql, map, Long.class);
    }
    /**
     *
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-10 12:01:05
     */
    public void updateAllocateApply(ProcessApplyDTO processApplyDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_allocate_apply SET  create_time = now()");
        if(StringUtils.isNotBlank(processApplyDTO.getOutstockOrgno())){
            sql.append(" ,outstock_orgno = :outstockOrgno ");
        }
        if(StringUtils.isNotBlank(processApplyDTO.getApplyProcessor())){
            sql.append(" ,apply_processor = :applyProcessor ");
        }
        if(processApplyDTO.getProcessTime() != null){
            sql.append(" ,process_time = :processTime ");
        }
        if(StringUtils.isNotBlank(processApplyDTO.getProcessType())){
            sql.append(" ,process_type = :processType ");
        }
        if (StringUtils.isNotBlank(processApplyDTO.getOutstockOrgType())){
            sql.append(" ,outstock_orgtype = :outstockOrgType ");
        }
        if (StringUtils.isNotBlank(processApplyDTO.getApplyType())){
            sql.append(" ,apply_type = :applyType ");
        }
        if (StringUtils.isNotBlank(processApplyDTO.getApplyStatus())){
            sql.append(" ,apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(processApplyDTO.getProcessMemo())){
            sql.append(" ,process_memo = :processMemo ");
        }
        sql.append(" WHERE version_no = :versionNo AND apply_no = :applyNo ");
        updateForBean(sql.toString(), processApplyDTO);
    }

    /**
     * 更新申请单详单
     * @param processApplyDetailDTO 详单列表
     * @author zhangkangjian
     * @date 2018-08-10 15:20:45
     */
    public void batchUpdateForApplyDetail(List<ProcessApplyDetailDTO> processApplyDetailDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_allocateapply_detail SET");
        sql.append("  supplier_no = :supplierNo, apply_num = :applyNum,sell_price = :sellPrice WHERE id = :id ");
        batchUpdateForListBean(sql.toString(), processApplyDetailDTO);
    }
    /**
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @param productCode
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    public Page<FindStockListDTO> findStockList(FindStockListDTO findStockListDTO, List<String> productCode, List<String> orgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.product_no,sum(ifnull(a.valid_stock,0) + ifnull(a.occupy_stock,0)) as 'total',a.product_name as 'productName',a.product_categoryname as 'productCategoryname',a.product_unit as 'unit'")
            .append(" FROM biz_stock_detail a ")
            .append(" WHERE 1 = 1 ");
        if(productCode != null && productCode.size() > 0){
            map.put("productCode", productCode);
            sql.append(" AND  a.product_no in (:productCode)");
        }
        String productNo = findStockListDTO.getProductNo();
        if(StringUtils.isNotBlank(productNo)){
            map.put("productNo", productNo);
            sql.append(" AND  a.product_no like concat('%',:productNo,'%') ");
        }
        if(orgCode != null && orgCode.size() > 0){
            map.put("orgCode", orgCode);
            sql.append(" AND  a.org_no in (:orgCode) ");
        }
        String orgNo = findStockListDTO.getOrgNo();
        if(StringUtils.isNotBlank(orgNo)){
            map.put("orgNo", orgNo);
            sql.append(" AND  a.org_no = :orgNo ");
        }
        sql.append(" GROUP BY a.product_no ")
        .append(" HAVING  sum(ifnull(a.valid_stock,0) + ifnull(a.occupy_stock,0) + ifnull(a.problem_stock,0) + ifnull(a.damaged_stock,0) + ifnull(a.transit_stock,0) + ifnull(a.freeze_stock,0)) > 0");
        return queryPageForBean(FindStockListDTO.class, sql.toString(), map, findStockListDTO.getOffset(), findStockListDTO.getPageSize());
    }

    /**
     * 修改申请单状态
     * @param applyNo 申请单号
     * @param status 申请单状态
     * @author liuduo
     * @date 2018-08-10 13:44:37
     */
    public void updateApplyOrderStatus(String applyNo, String status) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", status);
        params.put("applyNo", applyNo);

        String sql = "UPDATE biz_allocate_apply SET apply_status = :status WHERE apply_no = :applyNo";

        updateForMap(sql, params);
    }

    /**
     * 根据类型查询申请单
     * @param productType 商品类型
     * @param orgCode 当前登录人机构
     * @param status 申请单状态
     * @return 状态为等待收货的申请单
     * @author liuduo
     * @date 2018-08-11 12:56:39
     */
    public List<String> queryApplyNo(String productType, String orgCode, String status) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productType", productType);
        params.put("orgCode", orgCode);
        params.put("status", status);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT baa.apply_no FROM biz_allocate_apply AS baa LEFT JOIN biz_instockplan_detail AS bid ON bid.trade_no = baa.apply_no ")
            .append("  WHERE bid.complete_status = :status AND bid.product_type = :productType AND bid.instock_orgno = :orgCode AND bid.delete_flag = :deleteFlag");

        return querySingColum(String.class, sql.toString(), params);
    }
    /**
     * 查询库存的数量
     * @param productNo 组织架构code
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-13 19:47:32
     */
    public Page<QueryOrgDTO> findStockNum(String productNo,List<String> orgCodes, Integer offset, Integer pageSize) {
        String sql = "SELECT a.org_no as 'orgCode',SUM(a.valid_stock) as 'stockNum' FROM biz_stock_detail a WHERE a.product_no = :productNo AND a.org_no in (:orgCodes) GROUP BY a.org_no having SUM(a.valid_stock) > 0";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productNo", productNo);
        map.put("orgCodes", orgCodes);
        return queryPageForBean(QueryOrgDTO.class, sql, map, offset, pageSize);
    }

    /**
     * 根据编号获取物料和零配件调拨的申请详情
     * @param applyNo  code
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizAllocateApply getByNo(String applyNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,apply_no,applyorg_no,applyer,instock_orgno,apply_type,")
                .append("in_repository_no,outstock_orgtype,outstock_orgno,apply_processor,")
                .append("process_time,process_type,apply_status,creator,create_time,operator,")
                .append("operate_time,delete_flag,remark FROM biz_allocate_apply WHERE apply_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("applyNo", applyNo);
        return super.findForBean(BizAllocateApply.class, sql.toString(), params);
    }
    /**
     * 查询库存的数量
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-15 14:30:27
     */
    public Map<String,Object> queryStockQuantity(String outstockOrgno, String sellerOrgno) {
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.product_no as productNo, SUM(ifnull(a.valid_stock,0)) as validStock ")
            .append(" FROM biz_stock_detail a ")
            .append(" WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(outstockOrgno)){
            map.put("outstockOrgno", outstockOrgno);
            sql.append( "AND a.org_no = :outstockOrgno ");
        }
        if(StringUtils.isNotBlank(sellerOrgno)){
            map.put("sellerOrgno", sellerOrgno);
            sql.append( "AND a.seller_orgno = :sellerOrgno");
        }
        sql.append(" GROUP BY a.product_no ");
        List<Map<String, Object>> maps = queryListMap(sql.toString(), map);
        map.clear();
        maps.forEach(a ->{
            String productNo = (String) a.get("productNo");
            Object validStock =  a.get("validStock");
            map.put(productNo,validStock);
        });
        return map;
    }

    /**
     * 根据类型查询物料的code
     * @param equiptypeId 物料的类型
     * @return List<BasicCarpartsProductDTO>
     * @author zhangkangjian
     * @date 2018-08-16 14:01:23
     */
    public List<BasicCarpartsProductDTO> findEquipmentCode(Integer equiptypeId) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("equiptypeId", equiptypeId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.equip_code AS 'carpartsCode' FROM biz_service_equipment a WHERE 1 = 1 ");
        if(equiptypeId != null && equiptypeId > 0){
            sql.append(" AND a.equiptype_id = :equiptypeId ");
        }
        return queryListBean(BasicCarpartsProductDTO.class, sql.toString(), map);
    }

    /**
     * 问题件申请查询(创建问题件，查询问题件列表)
     * @param orgCode 机构的code
     * @return StatusDto<List<StockBizStockDetailDTO>>
     * @author zhangkangjian
     * @date 2018-08-22 14:37:40
     */
    public List<StockBizStockDetailDTO> queryProblemStockList(String orgCode, String productType, String supplierNo) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT a.id,a.product_no,a.product_name,a.product_type,a.product_unit,SUM(a.problem_stock) AS problem_stock,a.product_categoryname,a.supplier_no,b.supplier_name,a.cost_price")
            .append(" FROM biz_stock_detail a LEFT JOIN biz_service_supplier b ON a.supplier_no = b.supplier_code where 1 = 1");
        // 组织机构code
        if (StringUtils.isNotBlank(orgCode)) {
            param.put("orgCode", orgCode);
            sql.append(" AND a.org_no = :orgCode ");
        }
        if (StringUtils.isNotBlank(productType)) {
            param.put("productType", productType);
            sql.append(" AND a.product_type = :productType ");
        }
        if(StringUtils.isNotBlank(supplierNo)){
            param.put("supplierNo", supplierNo);
            sql.append(" AND a.supplier_no = :supplierNo ");
        }
        sql.append(" GROUP BY a.product_no having SUM(a.problem_stock) > 0 ORDER BY a.create_time DESC");
        return queryListBean(StockBizStockDetailDTO.class, sql.toString(), param);
    }

    /**
     * 查询商品所有的库存列表
     * @param productCodeList 商品的code
     * @param userOrgCode
     * @return List<FindStockListDTO>
     * @author zhangkangjian
     * @date 2018-09-03 11:52:05
     */
    public List<FindStockListDTO> findAllStockList(List<String> productCodeList, String userOrgCode) {
        if(productCodeList == null || productCodeList.size() == 0){
            return Collections.emptyList();
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("productCodeList", productCodeList);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.product_no,a.product_name,a.product_categoryname,a.product_unit as 'unit', ")
            .append(" SUM(IFNULL(a.valid_stock,0) + IFNULL(a.occupy_stock,0) + IFNULL(a.problem_stock,0)) AS 'total' ")
            .append(" FROM biz_stock_detail a WHERE a.product_no IN (:productCodeList) ");
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND a.org_no = :userOrgCode ");
        }
        sql.append(" GROUP BY a.product_no ");
        return queryListBean(FindStockListDTO.class, sql.toString(), map);
    }

    /**
     * 查看所有物料调拨库存
     * @param findStockListDTO 查询条件
     * @param userOrgCode
     * @return StatusDto<Page < FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-31 14:47:54
     */
    public Page<FindStockListDTO> findAllEquipmentStockList(FindStockListDTO findStockListDTO, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userOrgCode", userOrgCode);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.equip_code AS 'productNo', a.equip_name AS 'productName',a.equip_unit AS 'unit',c.type_name AS 'productCategoryname', ")
            .append(" SUM(IFNULL(b.valid_stock,0) + IFNULL(b.occupy_stock,0)) AS 'total' ")
            .append(" FROM biz_service_equipment a ")
            .append(" LEFT JOIN biz_stock_detail b ON a.equip_code = b.product_no AND b.org_no = :userOrgCode ")
            .append(" LEFT JOIN biz_service_equiptype c ON c.id = a.equiptype_id ")
            .append(" WHERE 1 = 1 ");
        Integer equiptypeId = findStockListDTO.getEquiptypeId();
        if(equiptypeId != null && equiptypeId > 0){
            map.put("equiptypeId", equiptypeId);
            sql.append(" AND a.equiptype_id = :equiptypeId ");
        }
        String productNo = findStockListDTO.getProductNo();
        if(StringUtils.isNotBlank(productNo)){
            map.put("productNo", productNo);
            sql.append(" AND a.equip_code = :productNo ");
        }
        sql.append(" group by a.equip_code ");
        return queryPageForBean(FindStockListDTO.class, sql.toString(), map, findStockListDTO.getOffset(), findStockListDTO.getPageSize());
    }

    /**
     * 根据商品类型查询申请单号
     * @param productType 商品类型
     * @param orgCode 机构code
     * @param status 申请单状态
     * @return 申请单号
     * @author liuduo
     * @date 2018-09-13 14:06:06
     */
    public List<String> queryOutStockApplyNo(String productType, String orgCode, String status) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("productType", productType);
        params.put("orgCode", orgCode);
        params.put("status", status);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT baa.apply_no from biz_allocate_apply as baa left join biz_outstockplan_detail as bod on bod.trade_no = baa.apply_no")
            .append("  WHERE bod.out_orgno = :orgCode AND bod.plan_status = :status AND bod.product_type = :productType AND bod.delete_flag = :deleteFlag");

        return querySingColum(String.class, sql.toString(), params);
    }

    /**
     * 批量更新采购商品数据
     * @param purchaseProductInfo 采购单的数据
     * @author zhangkangjian
     * @date 2018-09-13 16:25:39
     */
    public void batchupdatePurchaseProductInfo(List<PurchaseProductInfo> purchaseProductInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE biz_allocateapply_detail SET");
        sql.append(" sell_price = :sellPrice WHERE id = :id ");
        batchUpdateForListBean(sql.toString(), purchaseProductInfo);
    }

    /**
     * 查询问题件的供应商
     *
     * @param orgCode     机构的编号
     * @param productType 商品的编号
     * @return StatusDto<List < QuerySupplierInfoDTO>> 供应商列表
     * @author zhangkangjian
     * @date 2018-11-01 10:06:36
     */
    public List<QuerySupplierInfoDTO> queryProblemSupplier(String orgCode, String productType) {
        Map<String, Object> param = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.supplier_no AS 'supplierCode',b.supplier_name AS 'supplierName', ")
            .append(" CONCAT(b.province_name,b.city_name,b.area_name,b.supplier_address) AS 'address' ")
            .append(" FROM biz_stock_detail a LEFT JOIN biz_service_supplier b ON a.supplier_no = b.supplier_code ")
            .append(" WHERE a.problem_stock > 0 ");
        if (StringUtils.isNotBlank(orgCode)) {
            param.put("orgCode", orgCode);
            sql.append(" AND a.org_no = :orgCode ");
        }
        if (StringUtils.isNotBlank(productType)) {
            param.put("productType", productType);
            sql.append(" AND a.product_type = :productType ");
        }
        sql.append(" GROUP BY a.supplier_no ");
        return queryListBean(QuerySupplierInfoDTO.class, sql.toString(), param);

    }
}
