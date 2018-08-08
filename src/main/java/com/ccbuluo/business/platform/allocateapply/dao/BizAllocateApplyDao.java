package com.ccbuluo.business.platform.allocateapply.dao;

import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
}
