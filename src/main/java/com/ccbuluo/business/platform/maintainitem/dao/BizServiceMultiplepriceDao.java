package com.ccbuluo.business.platform.maintainitem.dao;

import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务项目 各地区对基本定价的倍数 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizServiceMultiplepriceDao extends BaseDao<BizServiceMultipleprice> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 服务项目 各地区对基本定价的倍数实体
     * @param entity 服务项目 各地区对基本定价的倍数实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int saveEntity(BizServiceMultipleprice entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_multipleprice ( maintainitem_code,multiple,")
            .append("province_code,province_name,city_code,city_name,remark,creator,")
            .append("create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :maintainitemCode, :multiple, :provinceCode, :provinceName,")
            .append(" :cityCode, :cityName, :remark, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 服务项目 各地区对基本定价的倍数实体
     * @param entity 服务项目 各地区对基本定价的倍数实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int update(BizServiceMultipleprice entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_multipleprice SET ")
            .append("maintainitem_code = :maintainitemCode,multiple = :multiple,")
            .append("province_code = :provinceCode,province_name = :provinceName,")
            .append("city_code = :cityCode,city_name = :cityName,remark = :remark,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取服务项目 各地区对基本定价的倍数详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public BizServiceMultipleprice getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,maintainitem_code,multiple,province_code,province_name,")
            .append("city_code,city_name,remark,creator,create_time,operator,operate_time,")
            .append("delete_flag FROM biz_service_multipleprice WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceMultipleprice.class, sql.toString(), params);
    }

    /**
     * 删除服务项目 各地区对基本定价的倍数
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_multipleprice WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
