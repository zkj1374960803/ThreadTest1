package com.ccbuluo.business.platform.carposition.dao;

import com.ccbuluo.business.entity.BizCarPosition;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
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
        sql.append("INSERT INTO biz_car_posation ( car_vin,detail_address,province_code,")
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
        sql.append("UPDATE biz_car_posation SET car_vin = :carVin,")
            .append("detail_address = :detailAddress,province_code = :provinceCode,")
            .append("province_name = :provinceName,city_code = :cityCode,")
            .append("city_name = :cityName,area_code = :areaCode,area_name = :areaName,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
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
            .append("bcp.operate_time,bcp.delete_flag,bcp.remark FROM biz_car_posation")
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
        sql.append("DELETE  FROM biz_car_posation WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
