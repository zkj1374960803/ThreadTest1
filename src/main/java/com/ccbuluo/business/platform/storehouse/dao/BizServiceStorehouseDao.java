package com.ccbuluo.business.platform.storehouse.dao;

import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-07-03 09:14:06
 * @version V1.0.0
 */
@Repository
public class BizServiceStorehouseDao extends BaseDao<BizServiceStorehouse> {
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
     * @date 2018-07-03 09:14:06
     */
    public int saveEntity(BizServiceStorehouse entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_storehouse ( storehouse_code,storehouse_name,")
            .append("storehouse_acreage,servicecenter_code,storehouse_status,")
            .append("province_code,province_name,city_code,city_name,area_code,area_name,")
            .append("creator,create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :storehouseCode, :storehouseName, :storehouseAcreage,")
            .append(" :servicecenterCode, :storehouseStatus, :provinceCode, :provinceName,")
            .append(" :cityCode, :cityName, :areaCode, :areaName, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int update(BizServiceStorehouse entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_storehouse SET storehouse_code = :storehouseCode,")
            .append("storehouse_name = :storehouseName,")
            .append("storehouse_acreage = :storehouseAcreage,")
            .append("servicecenter_code = :servicecenterCode,")
            .append("storehouse_status = :storehouseStatus,province_code = :provinceCode,")
            .append("province_name = :provinceName,city_code = :cityCode,")
            .append("city_name = :cityName,area_code = :areaCode,area_name = :areaName,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public BizServiceStorehouse getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,storehouse_code,storehouse_name,storehouse_acreage,")
            .append("servicecenter_code,storehouse_status,province_code,province_name,")
            .append("city_code,city_name,area_code,area_name,creator,create_time,operator,")
            .append("operate_time,delete_flag FROM biz_service_storehouse WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceStorehouse.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_storehouse WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
