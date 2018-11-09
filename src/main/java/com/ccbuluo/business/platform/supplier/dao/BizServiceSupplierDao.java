package com.ccbuluo.business.platform.supplier.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.supplier.dto.*;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
     * @author zhangkangjian
     * @date 2018-07-03 09:14:06
     */
    public int update(EditSupplierDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_supplier SET ")
            .append("supplier_name = :supplierName,linkman = :linkman,")
            .append("supplier_phone = :supplierPhone,supplier_address = :supplierAddress,")
            .append("supplier_status = :supplierStatus,supplier_nature = :supplierNature,")
            .append("establish_time = :establishTime,province_name = :provinceName,")
            .append("province_code = :provinceCode,city_name = :cityName,")
            .append("city_code = :cityCode,area_name = :areaName,area_code = :areaCode,")
            .append("operator = :operator,major_product = :majorProduct,")
            .append("operate_time = :operateTime WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取供应商详情
     * @param id  供应商id
     * @return ResultSupplierListDTO 供应商详情
     * @author zhangkangjian
     * @date 2018-07-03 09:14:06
     */
    public ResultFindSupplierDetailDTO getById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,supplier_code,supplier_name,linkman,supplier_phone,")
            .append("supplier_address,supplier_status,supplier_nature,establish_time,")
            .append("province_name,province_code,city_name,city_code,area_name,area_code,")
            .append("major_product ")
            .append(" FROM biz_service_supplier WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(ResultFindSupplierDetailDTO.class, sql.toString(), params);
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

    /**
     * 查询供应商的id根据手机号
     * @param supplierPhone 供应商的手机号
     * @return List<String> 供应商的ids
     * @author zhangkangjian
     * @date 2018-07-03 15:31:00
     */
    public List<String> querySupplierIdByPhone(String supplierPhone) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("supplierPhone", supplierPhone);
        String sql = "SELECT a.id FROM biz_service_supplier a WHERE a.supplier_phone = :supplierPhone ";
        return querySingColum(String.class, sql, map);
    }

    /**
     * 查询供应商的id根据名称
     * @param supplierCode 供应商的code
     * @return List<String> 供应商的ids
     * @author zhangkangjian
     * @date 2018-07-03 15:40:42
     */
    public QuerySupplierInfoDTO querySupplierInfoByCode(String supplierCode) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("supplierCode", supplierCode);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.supplier_name AS 'supplierName'")
            .append(" FROM biz_service_supplier a WHERE a.supplier_code = :supplierCode ");
        return findForBean(QuerySupplierInfoDTO.class, sql.toString(), map);
    }
    /**
     * 查询ids
     * @param value
     * @param fields 字段
     * @param tableName 表名称
     * @return List<String> ids
     * @author zhangkangjian
     * @date 2018-07-03 16:04:08
     */
    public List<Long> queryIds(String value, String fields, String tableName) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("value", value);
        String sql = "SELECT id FROM " + tableName+ "  WHERE " + fields + " = :value ";
        return querySingColum(Long.class, sql, map);
    }

    /**
     * 更新供应商的启用停用状态
     * @param id 供应商的id
     * @param supplierStatus 供应商的状态
     * @author zhangkangjian
     * @date 2018-07-03 17:19:15
     */
    public void updateSupplierStatus(Long id, Integer supplierStatus) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        map.put("supplierStatus", supplierStatus);
        String sql = " UPDATE biz_service_supplier a SET a.supplier_status = :supplierStatus WHERE a.id = :id ";
        updateForMap(sql, map);
    }
    /**
     * 查询供应商列表
     * @param querySupplierListDTO 查询条件
     * @return List<ResultSupplierListDTO> 供应商列表
     * @author zhangkangjian
     * @date 2018-07-04 09:59:56
     */
    public Page<ResultSupplierListDTO> querySupplierList(QuerySupplierListDTO querySupplierListDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,supplier_code,supplier_name, ")
            .append(" linkman,supplier_phone,supplier_address, ")
            .append(" supplier_status,province_name,city_name,area_name ")
            .append(" FROM biz_service_supplier ")
            .append(" WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(querySupplierListDTO.getProvinceName())){
            sql.append(" AND province_name = :provinceName ");
        }
        if(StringUtils.isNotBlank(querySupplierListDTO.getCityName())){
            sql.append(" AND city_name = :cityName ");
        }
        if(StringUtils.isNotBlank(querySupplierListDTO.getAreaName())){
            sql.append(" AND area_name = :areaName ");
        }
        if(querySupplierListDTO.getSupplierStatus() != null && querySupplierListDTO.getSupplierStatus() != -1){
            sql.append(" AND supplier_status = :supplierStatus ");
        }
        if(StringUtils.isNotBlank(querySupplierListDTO.getKeyword())){
            String keyword = querySupplierListDTO.getKeyword();
            querySupplierListDTO.setKeyword(Constants.PER_CENT + keyword + Constants.PER_CENT);
            sql.append(" AND (supplier_name LIKE :keyword OR linkman like :keyword OR supplier_phone like :keyword ) ");
        }
        sql.append(" order by operate_time desc ");
        SqlParameterSource param = new BeanPropertySqlParameterSource(querySupplierListDTO);
        return queryPageForBean(ResultSupplierListDTO.class, sql.toString(), param, querySupplierListDTO.getOffset(), querySupplierListDTO.getPageSize());
    }

    /**
     * 查询供应商零配件关联关系
     * @param queryRelSupplierProductDTO 查询的条件
     * @return  Page<RelSupplierProduct> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-01 15:07:58
     */
    public Page<QueryRelSupplierProductDTO> querySupplierProduct(QueryRelSupplierProductDTO queryRelSupplierProductDTO) {
        String sql = " SELECT a.id, a.supplier_code,a.product_code,a.product_type FROM rel_supplier_product a WHERE a.supplier_code = :supplierCode AND a.product_type = :productType group by a.supplier_code,a.product_code,a.product_type ";
        return queryPageForBean(QueryRelSupplierProductDTO.class, sql, queryRelSupplierProductDTO, queryRelSupplierProductDTO.getOffset(), queryRelSupplierProductDTO.getPageSize());
    }
    /**
     * 查询供应商物料关联关系
     * @param queryRelSupplierProductDTO 查询的条件
     * @return  Page<RelSupplierProduct> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-01 15:07:58
     */
    public Page<QueryRelSupplierProductDTO> queryEquipmentProduct(QueryRelSupplierProductDTO queryRelSupplierProductDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,b.`equip_name` as 'productName',c.`type_name` as 'categoryName' FROM rel_supplier_product a LEFT JOIN biz_service_equipment b ON a.`product_code` = b.`equip_code` LEFT JOIN biz_service_equiptype c ON b.`equiptype_id` = c.`id` ")
            .append(" WHERE a.`supplier_code` = :supplierCode AND a.product_type = :productType group by a.supplier_code,a.product_code,a.product_type");
        return queryPageForBean(QueryRelSupplierProductDTO.class, sql.toString(), queryRelSupplierProductDTO, queryRelSupplierProductDTO.getOffset(), queryRelSupplierProductDTO.getPageSize());
    }

    /**
     * 删除供应商关系
     * @param supplierProduct 删除条件
     * @author zhangkangjian
     * @date 2018-08-01 20:13:18
     */
    public void deleteSupplierProduct(RelSupplierProduct supplierProduct) {
        String sql = "DELETE FROM rel_supplier_product WHERE supplier_code = :supplierCode AND product_code = :productCode AND product_type = :productType ";
        updateForBean(sql, supplierProduct);
    }
    /**
     * 查询供应商关系
     * @param id 关系id
     * @return RelSupplierProduct
     * @author zhangkangjian
     * @date 2018-08-02 11:24:53
     */
    public RelSupplierProduct findRelSupplierProduct(Long id) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        String sql = "SELECT a.supplier_code,a.product_code,a.product_type FROM rel_supplier_product a WHERE a.id = :id";
        return findForBean(RelSupplierProduct.class, sql, map);
    }

    /**
     * 根据商品的code查询供应商的信息（下拉框）
     * @param code 商品的code
     * @param type 商品类型（注：FITTINGS零配件，EQUIPMENT物料）
     * @return StatusDto<List < RelSupplierProduct>>
     * @author zhangkangjian
     * @date 2018-08-07 15:32:17
     */
    public List<QuerySupplierInfoDTO> querySupplierInfo(String code, String type) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("productCode", code);
        map.put("productType", type);
        map.put("supplierStatus", Constants.DELETE_FLAG_DELETE);
    StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  a.supplier_code,b.supplier_name ")
            .append(" FROM rel_supplier_product a ")
            .append(" LEFT JOIN biz_service_supplier b ON a.supplier_code = b.supplier_code AND a.product_type = :productType ")
            .append(" WHERE b.supplier_status = :supplierStatus ");
        if(StringUtils.isNotBlank(code)){
            sql.append(" AND a.product_code = :productCode ");
        }
        sql.append("GROUP BY a.supplier_code");
        return queryListBean(QuerySupplierInfoDTO.class, sql.toString(), map);
}

    /**
     * 根据物料code查询是否已经关联
     * @param equipCode 物料code
     * @return 是否已经关联
     * @author liuduo
     * @date 2018-08-23 11:14:29
     */
    public Boolean getSupplier(String equipCode) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("equipCode", equipCode);

        String sql = "SELECT COUNT(id) > 0 FROM rel_supplier_product WHERE product_code = :equipCode";

        return findForObject(sql, map, Boolean.class);
    }

    /**
     * 获取供应商详情
     * @param supplierCode  供应商code
     * @return ResultSupplierListDTO 供应商详情
     * @author zhangkangjian
     * @date 2018-07-03 09:14:06
     */
    public ResultFindSupplierDetailDTO getByCode(String supplierCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,supplier_code,supplier_name,linkman,supplier_phone,")
                .append("supplier_address,supplier_status,supplier_nature,establish_time,")
                .append("province_name,province_code,city_name,city_code,area_name,area_code,")
                .append("major_product ")
                .append(" FROM biz_service_supplier WHERE supplier_code = :supplierCode");
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierCode", supplierCode);
        return super.findForBean(ResultFindSupplierDetailDTO.class, sql.toString(), params);
    }
}
