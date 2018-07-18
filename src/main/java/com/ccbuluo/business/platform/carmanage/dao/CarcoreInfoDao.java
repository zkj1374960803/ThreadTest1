package com.ccbuluo.business.platform.carmanage.dao;

import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author weijb
 * @date 2018-07-13 17:29:31
 * @version V1.0.0
 */
@Repository
public class CarcoreInfoDao extends BaseDao<CarcoreInfo> {
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
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public int saveEntity(CarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO carcore_info ( car_number,vin_number,engine_number,")
            .append("beidou_number,carbrand_id,carseries_id,carmodel_id,produce_time,")
            .append("remark,creator,create_time,operator,operate_time,delete_flag")
            .append(" ) VALUES (  :carNumber, :vinNumber, :engineNumber, :beidouNumber,")
            .append(" :carbrandId, :carseriesId, :carmodelId, :produceTime, :remark,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public int update(CarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE carcore_info SET car_number = :carNumber,")
            .append("vin_number = :vinNumber,engine_number = :engineNumber,")
            .append("beidou_number = :beidouNumber,carbrand_id = :carbrandId,")
            .append("carseries_id = :carseriesId,carmodel_id = :carmodelId,")
            .append("produce_time = :produceTime,remark = :remark,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public CarcoreInfo getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,car_number,vin_number,engine_number,beidou_number,")
            .append("carbrand_id,carseries_id,carmodel_id,produce_time,remark,creator,")
            .append("create_time,operator,operate_time,delete_flag FROM carcore_info")
            .append(" WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(CarcoreInfo.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM carcore_info WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
