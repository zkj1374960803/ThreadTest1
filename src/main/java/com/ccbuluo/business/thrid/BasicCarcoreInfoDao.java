package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车辆基本信息表 dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarcoreInfoDao extends BaseDao<BasicCarcoreInfo> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车辆基本信息表实体
     * @param entity 车辆基本信息表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carcore_info ( car_number,vin_number,engine_number,")
            .append("beidou_number,carbrand_id,carseries_id,carmodel_id,produce_time,")
            .append("cusmanager_uuid,cusmanager_name,store_assigned,store_code,store_name,")
            .append("remark,car_status,create_time,creator,operate_time,operator,")
            .append("delete_flag ) VALUES (  :carNumber, :vinNumber, :engineNumber,")
            .append(" :beidouNumber, :carbrandId, :carseriesId, :carmodelId, :produceTime,")
            .append(" :cusmanagerUuid, :cusmanagerName, :storeAssigned, :storeCode,")
            .append(" :storeName, :remark, :carStatus, :createTime, :creator,")
            .append(" :operateTime, :operator, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车辆基本信息表实体
     * @param entity 车辆基本信息表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carcore_info SET car_number = :carNumber,")
            .append("vin_number = :vinNumber,engine_number = :engineNumber,")
            .append("beidou_number = :beidouNumber,carbrand_id = :carbrandId,")
            .append("carseries_id = :carseriesId,carmodel_id = :carmodelId,")
            .append("produce_time = :produceTime,cusmanager_uuid = :cusmanagerUuid,")
            .append("cusmanager_name = :cusmanagerName,store_assigned = :storeAssigned,")
            .append("store_code = :storeCode,store_name = :storeName,remark = :remark,")
            .append("car_status = :carStatus,create_time = :createTime,creator = :creator,")
            .append("operate_time = :operateTime,operator = :operator,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车辆基本信息表详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarcoreInfo getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,car_number,vin_number,engine_number,beidou_number,")
            .append("carbrand_id,carseries_id,carmodel_id,produce_time,cusmanager_uuid,")
            .append("cusmanager_name,store_assigned,store_code,store_name,remark,")
            .append("car_status,create_time,creator,operate_time,operator,delete_flag")
            .append(" FROM basic_carcore_info WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarcoreInfo.class, sql.toString(), params);
    }

    /**
     * 删除车辆基本信息表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carcore_info WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
