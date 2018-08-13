package com.ccbuluo.business.platform.allocateapply.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import com.ccbuluo.dao.BaseDao;
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
public class BizAllocateapplyDetailDao extends BaseDao<BizAllocateapplyDetail> {
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
    public int saveEntity(BizAllocateapplyDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_allocateapply_detail ( apply_no,product_no,")
            .append("product_type,product_categoryname,apply_num,unit,sell_price,")
            .append("cost_price,supplier_no,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :applyNo, :productNo, :productType,")
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
    public int update(BizAllocateapplyDetail entity) {
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
    public BizAllocateapplyDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,apply_no,product_no,product_type,product_categoryname,")
            .append("apply_num,unit,sell_price,cost_price,supplier_no,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark")
            .append(" FROM biz_allocateapply_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizAllocateapplyDetail.class, sql.toString(), params);
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
                .append("bad.operator,bad.operate_time,baa.instock_orgno,baa.outstock_orgno,baa.process_type,baa.process_orgtype,bad.product_name ")
                .append(" FROM biz_allocateapply_detail bad LEFT JOIN biz_allocate_apply baa on bad.apply_no=baa.apply_no WHERE bad.delete_flag = :deleteFlag AND  bad.apply_no= :applyNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("applyNo", applyNo);
        return super.queryListBean(AllocateapplyDetailBO.class, sql.toString(), params);
    }
}
