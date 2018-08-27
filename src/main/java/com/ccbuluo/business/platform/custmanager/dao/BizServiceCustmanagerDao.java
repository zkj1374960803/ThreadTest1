package com.ccbuluo.business.platform.custmanager.dao;

import com.ccbuluo.business.platform.allocateapply.dto.QueryCustManagerListDTO;
import com.ccbuluo.business.platform.custmanager.dto.QueryUserListDTO;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户经理 dao
 * @author zhangkangjian
 * @date 2018-07-18 11:16:11
 * @version V 1.0.0
 */
@Repository
public class BizServiceCustmanagerDao extends BaseDao<BizServiceCustmanager> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 客户经理实体
     * @param entity 客户经理实体
     * @return int 影响条数
     * @author zhangkangjian
     * @date 2018-07-18 11:16:11
     */
    public int saveBizServiceCustmanager(BizServiceCustmanager entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_custmanager ( office_phone,receiving_address,")
            .append("user_uuid,remark,creator,operator,")
            .append("delete_flag,servicecenter_code ) VALUES (  :officePhone, :receivingAddress, :userUuid,")
            .append(" :remark, :creator,  :operator,  :deleteFlag, :servicecenterCode ")
            .append(" )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 客户经理实体
     * @param entity 客户经理实体
     * @return 影响条数
     * @author zhangkangjian
     * @date 2018-07-18 11:16:11
     */
    public int updateBizServiceCustmanager(BizServiceCustmanager entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_custmanager SET office_phone = :officePhone,")
            .append("receiving_address = :receivingAddress, ")
            .append("operator = :operator,operate_time = :operateTime,servicecenter_code = :servicecenterCode WHERE user_uuid = :userUuid");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取客户经理详情
     * @param id  id
     * @author zhangkangjian
     * @date 2018-07-18 11:16:11
     */
    public BizServiceCustmanager getBizServiceCustmanagerById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsc.id,bsc.office_phone,bsc.receiving_address,bsc.user_uuid,")
            .append("bsc.remark,bsc.creator,bsc.create_time,bsc.operator,bsc.operate_time,")
            .append("bsc.delete_flag FROM biz_service_custmanager AS bsc WHERE bsc.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceCustmanager.class, sql.toString(), params);
    }

    /**
     * 删除客户经理
     * @param id  id
     * @return 影响条数
     * @author zhangkangjian
     * @date 2018-07-18 11:16:11
     */
    public int deleteBizServiceCustmanagerById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_custmanager WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 查询客户经理
     * @param useruudis 用户uuid
     * @return List<QueryUserListDTO> 用户列表
     * @author zhangkangjian
     * @date 2018-07-19 10:30:54
     */
    public List<QueryUserListDTO> queryCustManager(List<String> useruudis) {
        if(useruudis == null || useruudis.size() == 0 ){
            return  Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsc.id,bsc.office_phone,bsc.receiving_address,bsc.user_uuid as 'useruuid',")
            .append("bsc.remark,bsc.creator,bsc.create_time,bsc.operator,bsc.operate_time,")
            .append("bsc.delete_flag FROM biz_service_custmanager AS bsc WHERE bsc.user_uuid in (:useruuid) ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("useruuid", useruudis);
        return super.queryListBean(QueryUserListDTO.class, sql.toString(), params);
    }


    /**
     *  获取客户经理详情
     * @param useruuid 用户uuid
     * @return BizServiceCustmanager 客户经理信息
     * @author zhangkangjian
     * @date 2018-07-19 15:39:23
     */
    public BizServiceCustmanager queryCustManagerByUuid(String useruuid) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsc.id,bsc.office_phone,bsc.receiving_address,bsc.user_uuid as 'useruuid',")
            .append("bsc.servicecenter_code,bsc.creator,bsc.create_time,bsc.operator,bsc.operate_time,")
            .append("bsc.delete_flag FROM biz_service_custmanager AS bsc WHERE bsc.user_uuid= :useruuid");
        Map<String, Object> params = Maps.newHashMap();
        params.put("useruuid", useruuid);
        return super.findForBean(BizServiceCustmanager.class, sql.toString(), params);
    }

    public List<String> queryIds(String value, String fields, String tableName) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("value", value);
        String sql = "SELECT user_uuid FROM " + tableName + "  WHERE " + fields + " = :value ";
        return querySingColum(String.class, sql, map);
    }

    /**
     * 查询客户经理列表(创建申请)
     * @param queryCustManagerListDTO 查询条件
     * @return Page<QueryCustManagerListDTO> 分页的客户经理列表
     * @author zhangkangjian
     * @date 2018-08-09 19:03:17
     */
    public Page<QueryCustManagerListDTO> queryCustManagerList(QueryCustManagerListDTO queryCustManagerListDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.user_uuid as 'useruuid', a.office_phone,c.cusmanager_name as 'name',c.vin_number,b.servicecenter_code as 'serviceCenter',b.storehouse_code as 'inRepositoryNo' ")
            .append(" FROM biz_service_custmanager a  ")
            .append(" LEFT JOIN biz_service_storehouse b ON a.servicecenter_code = b.servicecenter_code ")
            .append(" LEFT JOIN biz_service_maintaincar c ON a.user_uuid = c.cusmanager_uuid ")
            .append(" WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(queryCustManagerListDTO.getName())){
            sql.append(" AND (c.cusmanager_name like concat(:name,'%')  OR c.vin_number like concat(:name,'%'))");
        }
        if(StringUtils.isNotBlank(queryCustManagerListDTO.getServiceCenter())){
            sql.append(" AND b.servicecenter_code = :serviceCenter");
        }
        sql.append(" group by a.user_uuid ");
        return queryPageForBean(QueryCustManagerListDTO.class, sql.toString(), queryCustManagerListDTO, queryCustManagerListDTO.getOffset(), queryCustManagerListDTO.getPageSize());
    }

}
