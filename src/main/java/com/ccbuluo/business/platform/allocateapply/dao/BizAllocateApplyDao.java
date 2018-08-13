package com.ccbuluo.business.platform.allocateapply.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
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
 * 物料和零配件调拨的申请 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizAllocateApplyDao extends BaseDao<BizAllocateApply> {
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
    public int saveEntity(BizAllocateApply entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocate_apply ( apply_no,applyorg_no,applyer,")
            .append("instock_orgno,in_repository_no,outstock_orgtype,outstock_orgno,")
            .append("apply_processor,process_time,process_type,apply_status,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark ) VALUES ( ")
            .append(" :applyNo, :applyorgNo, :applyer, :instockOrgno, :inRepositoryNo,")
            .append(" :outstockOrgtype, :outstockOrgno, :applyProcessor, :processTime,")
            .append(" :processType, :applyStatus, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 物料和零配件调拨的申请实体
     * @param entity 物料和零配件调拨的申请实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizAllocateApply entity) {
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
    public BizAllocateApply getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,apply_no,applyorg_no,applyer,instock_orgno,")
            .append("in_repository_no,outstock_orgtype,outstock_orgno,apply_processor,")
            .append("process_time,process_type,apply_status,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark FROM biz_allocate_apply WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizAllocateApply.class, sql.toString(), params);
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
    public void batchInsertForapplyDetailList(List<BizAllocateapplyDetail> allocateapplyDetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocateapply_detail ( apply_no,product_no,")
            .append("product_type,product_categoryname,apply_num,unit,sell_price,")
            .append("cost_price,supplier_no,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :applyNo, :productNo, :productType,")
            .append(" :productCategoryname, :applyNum, :unit, :sellPrice, :costPrice,")
            .append(" :supplierNo, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag, :remark )");
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
        sql.append(" SELECT a.process_orgtype,a.process_orgno,a.apply_type,a.apply_no,a.applyorg_no,a.apply_status,a.applyer_name,b.storehouse_name,b.storehouse_address, ")
            .append(" a.create_time,a.process_type,b.servicecenter_code as 'instockOrgno',a.outstock_orgno,a.in_repository_no as 'inRepositoryNo' ")
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
            .append("  LEFT JOIN biz_service_equipment c ON a.product_no = c.equip_code AND a.product_type = 'EQUIPMENT' ")
            .append(" WHERE a.delete_flag = :deleteFlag AND a.apply_no = :applyNo ");
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
    public Page<QueryAllocateApplyListDTO> findApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userOrgCode", userOrgCode);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.apply_no,a.applyer_name,a.create_time,a.process_type,a.apply_status ")
            .append(" FROM biz_allocate_apply a WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(userOrgCode)){
            sql.append(" AND a.applyorg_no = :userOrgCode ");
        }
        if(StringUtils.isNotBlank(processType)){
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            sql.append(" AND a.apply_no = :applyNo ");
        }
        return queryPageForBean(QueryAllocateApplyListDTO.class, sql.toString(), map, offset, pageSize);
    }
    /**
     * 查询申请列表
     * @param userOrgCode 用户机构
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    public Page<QueryAllocateApplyListDTO> findProcessApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize, String userOrgCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userOrgCode", userOrgCode);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.apply_no,a.applyer_name,a.create_time,a.process_type,a.apply_status ")
            .append(" FROM biz_allocate_apply a WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(userOrgCode)){
            sql.append(" AND a.outstock_orgno = :userOrgCode ");
        }
        if(StringUtils.isNotBlank(processType)){
            sql.append(" AND a.process_type = :processType ");
        }
        if(StringUtils.isNotBlank(applyStatus)){
            sql.append(" AND a.apply_status = :applyStatus ");
        }
        if(StringUtils.isNotBlank(applyNo)){
            sql.append(" AND a.apply_no = :applyNo ");
        }
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
        String sql = " SELECT a.version_no FROM biz_allocate_apply a WHERE a.applyorg_no = :applyNo ";
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
        sql.append(" UPDATE biz_allocate_apply SET apply_processor = :applyProcessor,process_time = :processTime ");
        if(StringUtils.isNotBlank(processApplyDTO.getOutstockOrgno())){
            sql.append(" ,outstock_orgno = outstockOrgno ");
        }
        if(StringUtils.isNotBlank(processApplyDTO.getProcessType())){
            sql.append(" ,process_type = :processType ");
        }
        if (StringUtils.isNotBlank(processApplyDTO.getOutstockOrgType())){
            sql.append(" ,outstock_orgtype = :outstockOrgType ");
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
        sql.append(" UPDATE biz_allocateapply_detail SET apply_num = :applyNum,sell_price = :sellPrice WHERE id = :id ");
        batchUpdateForListBean(sql.toString(), processApplyDetailDTO);
    }
    /**
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    public Page<FindStockListDTO> findStockList(FindStockListDTO findStockListDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.product_no,sum(a.valid_stock) as 'total',b.product_name,b.product_categoryname,b.unit ")
            .append(" FROM biz_stock_detail a ")
            .append(" LEFT JOIN (SELECT product_no,unit,product_name,product_categoryname FROM biz_instockorder_detail GROUP BY product_no) b ON a.product_no = b.product_no ")
            .append(" GROUP BY a.product_no ");
        return queryPageForBean(FindStockListDTO.class, sql.toString(), findStockListDTO, findStockListDTO.getOffset(), findStockListDTO.getPageSize());
    }
}
