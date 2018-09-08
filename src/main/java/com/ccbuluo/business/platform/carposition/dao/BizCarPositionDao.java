package com.ccbuluo.business.platform.carposition.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizCarPosition;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 车辆停放的位置 dao
 * @author Ryze
 * @date 2018-09-03 15:38:39
 * @version V 1.0.0
 */
@Repository
public class BizCarPositionDao extends BaseDao<BizCarPosition> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 车辆停放的位置实体
     * @param entity 车辆停放的位置实体
     * @return int 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int saveBizCarPosition(BizCarPosition entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_car_position ( car_vin,detail_address,province_code,")
            .append("province_name,city_code,city_name,area_code,area_name,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark ) VALUES ( ")
            .append(" :carVin, :detailAddress, :provinceCode, :provinceName, :cityCode,")
            .append(" :cityName, :areaCode, :areaName, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车辆停放的位置实体
     * @param entity 车辆停放的位置实体
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int updateBizCarPosition(BizCarPosition entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_car_position SET ")
            .append("detail_address = :detailAddress,province_code = :provinceCode,")
            .append("province_name = :provinceName,city_code = :cityCode,")
            .append("city_name = :cityName,area_code = :areaCode,area_name = :areaName,")
            .append("operator = :operator,operate_time = :operateTime WHERE car_vin= :carVin");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取车辆停放的位置详情
     * @param id  id
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public BizCarPosition getBizCarPosationById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bcp.id,bcp.car_vin,bcp.detail_address,bcp.province_code,")
            .append("bcp.province_name,bcp.city_code,bcp.city_name,bcp.area_code,")
            .append("bcp.area_name,bcp.creator,bcp.create_time,bcp.operator,")
            .append("bcp.operate_time,bcp.delete_flag,bcp.remark FROM biz_car_position")
            .append(" AS bcp WHERE bcp.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizCarPosition.class, sql.toString(), params);
    }

    /**
     * 删除车辆停放的位置
     * @param id  id
     * @return 影响条数
     * @author Ryze
     * @date 2018-09-03 15:38:39
     */
    public int deleteBizCarPositionById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_car_position WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据车辆vin码查询车辆停放位置
     * @param carVin 车辆vin码
     * @return 车辆停放位置
     * @author liuduo
     * @date 2018-09-05 10:18:06
     */
    public BizCarPosition getByCarVin(String carVin) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("carVin", carVin);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bcp.id,bcp.car_vin,bcp.detail_address,bcp.province_name,")
            .append(" bcp.city_name,bcp.area_name FROM biz_car_position AS bcp WHERE bcp.car_vin= :carVin");

        return findForBean(BizCarPosition.class, sql.toString(), params);
    }
}
