package com.ccbuluo.business.platform.supplier.dao;

import com.ccbuluo.business.entity.BizServiceSupplier;
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
public class BizServiceSupplierDao extends BaseDao<BizServiceSupplier> {
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
    public int saveEntity(BizServiceSupplier entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_supplier ( supplier_code,supplier_name,")
            .append("linkman,supplier_phone,supplier_address,supplier_status,")
            .append("supplier_nature,establish_time,province_name,province_code,city_name,")
            .append("city_code,area_name,area_code,major_product,creator,create_time,")
            .append("operator,operate_time,delete_flag ) VALUES (  :supplierCode,")
            .append(" :supplierName, :linkman, :supplierPhone, :supplierAddress,")
            .append(" :supplierStatus, :supplierNature, :establishTime, :provinceName,")
            .append(" :provinceCode, :cityName, :cityCode, :areaName, :areaCode,")
            .append(" :majorProduct, :creator, :createTime, :operator, :operateTime,")
            .append(" :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int update(BizServiceSupplier entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_supplier SET supplier_code = :supplierCode,")
            .append("supplier_name = :supplierName,linkman = :linkman,")
            .append("supplier_phone = :supplierPhone,supplier_address = :supplierAddress,")
            .append("supplier_status = :supplierStatus,supplier_nature = :supplierNature,")
            .append("establish_time = :establishTime,province_name = :provinceName,")
            .append("province_code = :provinceCode,city_name = :cityName,")
            .append("city_code = :cityCode,area_name = :areaName,area_code = :areaCode,")
            .append("major_product = :majorProduct,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public BizServiceSupplier getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,supplier_code,supplier_name,linkman,supplier_phone,")
            .append("supplier_address,supplier_status,supplier_nature,establish_time,")
            .append("province_name,province_code,city_name,city_code,area_name,area_code,")
            .append("major_product,creator,create_time,operator,operate_time,delete_flag")
            .append(" FROM biz_service_supplier WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceSupplier.class, sql.toString(), params);
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
        sql.append("DELETE  FROM biz_service_supplier WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
