package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 客服经理 上门维修 使用的维修车 实体表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizServiceMaintaincarDao extends BaseDao<BizServiceMaintaincar> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 客服经理 上门维修 使用的维修车 实体表实体
     * @param entity 客服经理 上门维修 使用的维修车 实体表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BizServiceMaintaincar entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_maintaincar ( mend_code,vin_number,")
            .append("car_status,carbrand_id,carseries_id,carmodel_id,cusmanager_uuid,")
            .append("cusmanager_name,beidou_number,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag ) VALUES (  :mendCode, :vinNumber,")
            .append(" :carStatus, :carbrandId, :carseriesId, :carmodelId, :cusmanagerUuid,")
            .append(" :cusmanagerName, :beidouNumber, :remark, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 客服经理 上门维修 使用的维修车 实体表实体
     * @param entity 客服经理 上门维修 使用的维修车 实体表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BizServiceMaintaincar entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintaincar SET mend_code = :mendCode,")
            .append("vin_number = :vinNumber,car_status = :carStatus,")
            .append("carbrand_id = :carbrandId,carseries_id = :carseriesId,")
            .append("carmodel_id = :carmodelId,cusmanager_uuid = :cusmanagerUuid,")
            .append("cusmanager_name = :cusmanagerName,beidou_number = :beidouNumber,")
            .append("remark = :remark,creator = :creator,create_time = :createTime,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取客服经理 上门维修 使用的维修车 实体表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BizServiceMaintaincar getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,mend_code,vin_number,car_status,carbrand_id,carseries_id,")
            .append("carmodel_id,cusmanager_uuid,cusmanager_name,beidou_number,remark,")
            .append("creator,create_time,operator,operate_time,delete_flag")
            .append(" FROM biz_service_maintaincar WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceMaintaincar.class, sql.toString(), params);
    }

    /**
     * 删除客服经理 上门维修 使用的维修车 实体表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_maintaincar WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
